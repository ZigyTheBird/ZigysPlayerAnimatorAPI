package com.zigythebird.playeranimatorapi.gecko;

import com.zigythebird.playeranimatorapi.ModInit;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.GeckoLibServices;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.constant.dataticket.SerializableDataTicket;
import software.bernie.geckolib.util.GeckoLibUtil;
import software.bernie.geckolib.util.RenderUtil;

public class AnimatablePlayerLayer implements GeoAnimatable {

    private final AbstractClientPlayer player;

    public AnimatablePlayerLayer(AbstractClientPlayer player) {
        this.player = player;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, ModInit.MOD_ID, state -> PlayState.STOP).setOverrideEasingTypeFunction((azurePlayer) -> ModGeckoUtilsClient.getEasingTypeForID(player)));
    }

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Nullable
    public <D> D getAnimData(SerializableDataTicket<D> dataTicket) {
        return getAnimatableInstanceCache().getManagerForId(player.getId()).getData(dataTicket);
    }

    public <D> void setAnimData(Entity relatedEntity, long instanceId, SerializableDataTicket<D> dataTicket, D data) {
        if (relatedEntity.level().isClientSide()) {
            getAnimatableInstanceCache().getManagerForId(instanceId).setData(dataTicket, data);
        }
        else {
            syncAnimData(instanceId, dataTicket, data, relatedEntity);
        }
    }

    public <D> void syncAnimData(long instanceId, SerializableDataTicket<D> dataTicket, D data, Entity entityToTrack) {
        GeckoLibServices.NETWORK.syncSingletonAnimData(instanceId, dataTicket, data, entityToTrack);
    }

    public  <D> void triggerAnim(Entity relatedEntity, long instanceId, @Nullable String controllerName, String animName) {
        if (relatedEntity.level().isClientSide()) {
            getAnimatableInstanceCache().getManagerForId(instanceId).tryTriggerAnimation(controllerName, animName);
        }
        else {
            GeckoLibServices.NETWORK.triggerSingletonAnim(getClass().getName(), relatedEntity, instanceId, controllerName, animName);
        }
    }

    @Override
    public double getTick(Object object) {
        return RenderUtil.getCurrentTick();
    }

    public AbstractClientPlayer getPlayer() {
        return player;
    }
}
