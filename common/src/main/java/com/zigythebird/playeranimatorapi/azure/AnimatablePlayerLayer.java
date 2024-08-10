package com.zigythebird.playeranimatorapi.azure;

import com.zigythebird.playeranimatorapi.ModInit;
import com.zigythebird.playeranimatorapi.playeranims.CustomModifierLayer;
import com.zigythebird.playeranimatorapi.playeranims.PlayerAnimations;
import com.zigythebird.playeranimatorapi.registry.AzureControllerRegistry;
import mod.azure.azurelib.core.animatable.GeoAnimatable;
import mod.azure.azurelib.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.core.animation.AnimatableManager;
import mod.azure.azurelib.core.animation.AnimationController;
import mod.azure.azurelib.core.object.PlayState;
import mod.azure.azurelib.network.SerializableDataTicket;
import mod.azure.azurelib.network.packet.AnimDataSyncPacket;
import mod.azure.azurelib.network.packet.AnimTriggerPacket;
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
        CustomModifierLayer<?> layer = PlayerAnimations.getModifierLayer(player);
        controllers.add(new AnimationController<>(this, ModInit.MOD_ID, state -> PlayState.CONTINUE).setOverrideEasingTypeFunction((azurePlayer) -> ModAzureUtilsClient.getEasingTypeForID(player)));
        controllers.add(new AnimationController<>(this, ModInit.MOD_ID, AzureControllerRegistry.getControllerForMod(0, layer)));
        controllers.add(new AnimationController<>(this, ModInit.MOD_ID, AzureControllerRegistry.getControllerForMod(1, layer)));
        controllers.add(new AnimationController<>(this, ModInit.MOD_ID, AzureControllerRegistry.getControllerForMod(2, layer)));
        controllers.add(new AnimationController<>(this, ModInit.MOD_ID, AzureControllerRegistry.getControllerForMod(3, layer)));
        controllers.add(new AnimationController<>(this, ModInit.MOD_ID, AzureControllerRegistry.getControllerForMod(4, layer)));
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

    public <D> void setAnimData(Entity relatedEntity, long instanceId, SerializableDataTicket<D> dataTicket, D data) {
        if (relatedEntity.level().isClientSide()) {
            getAnimatableInstanceCache().getManagerForId(instanceId).setData(dataTicket, data);
        }
        else {
            syncAnimData(instanceId, dataTicket, data, relatedEntity);
        }
    }

    public  <D> void syncAnimData(long instanceId, SerializableDataTicket<D> dataTicket, D data, Entity entityToTrack) {
        Services.NETWORK.sendToTrackingEntityAndSelf(
                new AnimDataSyncPacket<>(getClass().toString(), instanceId, dataTicket, data),
                entityToTrack
        );
    }

    public void triggerAnim(Entity relatedEntity, long instanceId, @Nullable String controllerName, String animName) {
        if (relatedEntity.level().isClientSide()) {
            getAnimatableInstanceCache().getManagerForId(instanceId).tryTriggerAnimation(controllerName, animName);
        } else {
            Services.NETWORK.sendToTrackingEntityAndSelf(
                    new AnimTriggerPacket(getClass().toString(), instanceId, controllerName, animName),
                    relatedEntity
            );
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
