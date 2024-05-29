package com.zigythebird.playeranimatorapi.azure;

import com.zigythebird.playeranimatorapi.ModInit;
import mod.azure.azurelib.core.animatable.GeoAnimatable;
import mod.azure.azurelib.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.core.animation.AnimatableManager;
import mod.azure.azurelib.core.animation.AnimationController;
import mod.azure.azurelib.core.object.PlayState;
import mod.azure.azurelib.network.SerializableDataTicket;
import mod.azure.azurelib.network.packet.EntityAnimDataSyncPacket;
import mod.azure.azurelib.network.packet.EntityAnimTriggerPacket;
import mod.azure.azurelib.platform.Services;
import mod.azure.azurelib.util.AzureLibUtil;
import mod.azure.azurelib.util.RenderUtils;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;

public class AnimatablePlayerLayer implements GeoAnimatable {

    private final AbstractClientPlayer player;

    public AnimatablePlayerLayer(AbstractClientPlayer player) {
        this.player = player;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, ModInit.MOD_ID, state -> PlayState.STOP).setOverrideEasingTypeFunction((azurePlayer) -> ModAzureUtilsClient.getEasingTypeForID(player)));
    }

    private final AnimatableInstanceCache cache = AzureLibUtil.createInstanceCache(this);

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Nullable
    public <D> D getAnimData(SerializableDataTicket<D> dataTicket) {
        return getAnimatableInstanceCache().getManagerForId(player.getId()).getData(dataTicket);
    }

    public  <D> void setAnimData(SerializableDataTicket<D> dataTicket, D data) {
        Entity entity = player;

        if (entity.level().isClientSide()) {
            getAnimatableInstanceCache().getManagerForId(entity.getId()).setData(dataTicket, data);
        } else {
            EntityAnimDataSyncPacket<D> entityAnimDataSyncPacket = new EntityAnimDataSyncPacket<>(entity.getId(), dataTicket, data);
            Services.NETWORK.sendToTrackingEntityAndSelf(entityAnimDataSyncPacket, entity);
        }
    }

    public void triggerAnim(@Nullable String controllerName, String animName) {
        Entity entity = player;

        if (entity.level().isClientSide()) {
            getAnimatableInstanceCache().getManagerForId(entity.getId()).tryTriggerAnimation(controllerName, animName);
        } else {
            EntityAnimTriggerPacket entityAnimTriggerPacket = new EntityAnimTriggerPacket(entity.getId(), controllerName, animName);
            Services.NETWORK.sendToTrackingEntityAndSelf(entityAnimTriggerPacket, entity);
        }
    }

    @Override
    public double getTick(Object object) {
        return RenderUtils.getCurrentTick();
    }

    public AbstractClientPlayer getPlayer() {
        return player;
    }
}