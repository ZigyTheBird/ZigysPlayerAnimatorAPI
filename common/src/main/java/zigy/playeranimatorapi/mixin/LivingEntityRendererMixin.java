package zigy.playeranimatorapi.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import zigy.playeranimatorapi.data.PlayerParts;
import zigy.playeranimatorapi.playeranims.CustomModifierLayer;

import static zigy.playeranimatorapi.playeranims.PlayerAnimations.animationLayerId;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> {

    @Shadow
    protected M model;

    @Inject(method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At("HEAD"))
    private void render(T entity, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight, CallbackInfo ci) {
        if (entity instanceof Player) {
            CustomModifierLayer animationContainer = (CustomModifierLayer) PlayerAnimationAccess.getPlayerAssociatedData((AbstractClientPlayer) entity).get(animationLayerId);
            PlayerModel playerModel = ((PlayerModel)(this.model));
            PlayerParts parts = animationContainer.data.parts();

            if (animationContainer.isActive()) {

                if (!parts.body.isVisible) {
                    return;
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
            }
            else {
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
        }
    }
}
