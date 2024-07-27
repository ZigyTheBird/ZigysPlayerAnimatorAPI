package com.zigythebird.playeranimatorapi.mixin.azure;

import com.zigythebird.playeranimatorapi.azure.PlayerAnimationModel;
import com.zigythebird.playeranimatorapi.azure.PlayerAnimationRenderer;
import com.zigythebird.playeranimatorapi.misc.AzurePlayerInterface;
import com.zigythebird.playeranimatorapi.misc.GetAzureModelRendererInterface;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerModel.class)
public class PlayerAzureModelMixin_azureOnly<T extends LivingEntity> implements GetAzureModelRendererInterface {

    @Unique
    private PlayerAnimationRenderer zigysPlayerAnimatorAPI$renderer = null;

    @Inject(method = "setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V", at = @At("TAIL"))
    private void inject(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
        PlayerAnimationRenderer renderer = playeranimatorapi$getRenderer();
        if (renderer != null && entity instanceof Player && entity.equals(Minecraft.getInstance().player) && ((PlayerAnimationModel)renderer.getGeoModel()).allResourcesExist(((AzurePlayerInterface) entity).playeranimatorapi$getAnimatablePlayerLayer())) {
            renderer.setupAnim(renderer.getGeoModel().getBakedModel(renderer.getGeoModel().getModelResource(((AzurePlayerInterface) entity).playeranimatorapi$getAnimatablePlayerLayer())));
        }
    }

    @Override
    public PlayerAnimationRenderer playeranimatorapi$getRenderer() {
        return zigysPlayerAnimatorAPI$renderer;
    }

    @Override
    public void playeranimatorapi$setRenderer(PlayerAnimationRenderer renderer) {
        zigysPlayerAnimatorAPI$renderer = renderer;
    }
}
