package com.zigythebird.playeranimatorapi.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.zigythebird.playeranimatorapi.ModInitClient;
import com.zigythebird.playeranimatorapi.azure.PlayerAnimationModel;
import com.zigythebird.playeranimatorapi.azure.PlayerAnimationRenderer;
import com.zigythebird.playeranimatorapi.data.PlayerParts;
import com.zigythebird.playeranimatorapi.playeranims.CustomModifierLayer;
import com.zigythebird.playeranimatorapi.playeranims.PlayerAnimations;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin_azureOnly<T extends LivingEntity, M extends EntityModel<T>> {

    @Unique
    private PlayerAnimationRenderer zigysPlayerAnimatorAPI$animationRenderer;

    @Shadow
    protected M model;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void constructor(EntityRendererProvider.Context context, EntityModel model, float shadowRadius, CallbackInfo ci) {
        if (model instanceof PlayerModel<?>) {
            zigysPlayerAnimatorAPI$animationRenderer = new PlayerAnimationRenderer();
        }
    }

    @Inject(method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At(value = "INVOKE", shift = At.Shift.AFTER, target = "Lnet/minecraft/client/model/EntityModel;setupAnim(Lnet/minecraft/world/entity/Entity;FFFFF)V"), cancellable = true)
    private void render(T entity, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight, CallbackInfo ci) {
        if (entity instanceof Player) {
            CustomModifierLayer animationContainer = PlayerAnimations.getModifierLayer((AbstractClientPlayer) entity);
            PlayerModel playerModel = ((PlayerModel) (this.model));

            if (animationContainer != null && animationContainer.isActive()) {
                PlayerParts parts = animationContainer.data.parts();

                if (!parts.body.isVisible) {
                    ModInitClient.renderingGUI = false;
                    ci.cancel();
                    return;
                }

                if ((!entity.equals(Minecraft.getInstance().player) || !Minecraft.getInstance().options.getCameraType().isFirstPerson() || ModInitClient.renderingGUI) && ((PlayerAnimationModel)(zigysPlayerAnimatorAPI$animationRenderer.getGeoModel())).allResourcesExist(((AbstractClientPlayer) entity).playeranimatorapi$getAnimatablePlayerLayer())) {
                    matrixStack.pushPose();
                    matrixStack.scale(-1.0F, -1.0F, 1.0F);
                    matrixStack.scale(1/0.9375F, 1/0.9375F, 1/0.9375F);
                    matrixStack.translate(0.0F, -1.501F, 0.0F);
                    zigysPlayerAnimatorAPI$animationRenderer.setPlayerModel(playerModel);
                    zigysPlayerAnimatorAPI$animationRenderer.render(matrixStack, ((AbstractClientPlayer) entity).playeranimatorapi$getAnimatablePlayerLayer(), buffer, null, null, packedLight);
                    matrixStack.popPose();
                }

                playerModel.head.zigysPlayerAnimatorAPI$setIsVisible(parts.head.isVisible);
                playerModel.body.zigysPlayerAnimatorAPI$setIsVisible(parts.torso.isVisible);
                playerModel.rightArm.zigysPlayerAnimatorAPI$setIsVisible(parts.rightArm.isVisible);
                playerModel.leftArm.zigysPlayerAnimatorAPI$setIsVisible(parts.leftArm.isVisible);
                playerModel.rightLeg.zigysPlayerAnimatorAPI$setIsVisible(parts.rightLeg.isVisible);
                playerModel.leftLeg.zigysPlayerAnimatorAPI$setIsVisible(parts.leftLeg.isVisible);
                playerModel.hat.zigysPlayerAnimatorAPI$setIsVisible(parts.head.isVisible);
                playerModel.jacket.zigysPlayerAnimatorAPI$setIsVisible(parts.torso.isVisible);
                playerModel.rightSleeve.zigysPlayerAnimatorAPI$setIsVisible(parts.rightArm.isVisible);
                playerModel.leftSleeve.zigysPlayerAnimatorAPI$setIsVisible(parts.leftArm.isVisible);
                playerModel.rightPants.zigysPlayerAnimatorAPI$setIsVisible(parts.rightLeg.isVisible);
                playerModel.leftPants.zigysPlayerAnimatorAPI$setIsVisible(parts.leftLeg.isVisible);
            } else {
                playerModel.head.zigysPlayerAnimatorAPI$setIsVisible(true);
                playerModel.body.zigysPlayerAnimatorAPI$setIsVisible(true);
                playerModel.rightArm.zigysPlayerAnimatorAPI$setIsVisible(true);
                playerModel.leftArm.zigysPlayerAnimatorAPI$setIsVisible(true);
                playerModel.rightLeg.zigysPlayerAnimatorAPI$setIsVisible(true);
                playerModel.leftLeg.zigysPlayerAnimatorAPI$setIsVisible(true);
                playerModel.hat.zigysPlayerAnimatorAPI$setIsVisible(true);
                playerModel.jacket.zigysPlayerAnimatorAPI$setIsVisible(true);
                playerModel.rightSleeve.zigysPlayerAnimatorAPI$setIsVisible(true);
                playerModel.leftSleeve.zigysPlayerAnimatorAPI$setIsVisible(true);
                playerModel.rightPants.zigysPlayerAnimatorAPI$setIsVisible(true);
                playerModel.leftPants.zigysPlayerAnimatorAPI$setIsVisible(true);
            }

            ModInitClient.renderingGUI = false;
        }
    }
}
