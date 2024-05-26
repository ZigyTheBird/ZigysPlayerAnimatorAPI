package zigy.playeranimatorapi.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
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
import zigy.playeranimatorapi.azure.ModAzureUtilsClient;
import zigy.playeranimatorapi.azure.PlayerAnimationModel;
import zigy.playeranimatorapi.azure.PlayerAnimationRenderer;
import zigy.playeranimatorapi.data.PlayerParts;
import zigy.playeranimatorapi.misc.PlayerModelInterface;
import zigy.playeranimatorapi.playeranims.CustomModifierLayer;
import zigy.playeranimatorapi.playeranims.PlayerAnimations;
import zigy.playeranimatorapi.registry.PlayerEffectsRendererRegistry;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin_azureOnly<T extends LivingEntity, M extends EntityModel<T>> {

    @Unique
    private PlayerAnimationRenderer animationRenderer;

    @Shadow
    protected M model;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void constructor(EntityRendererProvider.Context context, EntityModel model, float shadowRadius, CallbackInfo ci) {
        if (model instanceof PlayerModel<?>) {
            animationRenderer = new PlayerAnimationRenderer(context);
        }
    }

    @Inject(method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At("HEAD"))
    private void render2(T entity, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight, CallbackInfo ci) {
        ModAzureUtilsClient.currentPlayerRenderer = animationRenderer;
    }

    @Inject(method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At("TAIL"), cancellable = true)
    private void render(T entity, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight, CallbackInfo ci) {
        if (entity instanceof Player) {
            CustomModifierLayer animationContainer = PlayerAnimations.getModifierLayer((AbstractClientPlayer) entity);
            PlayerModel playerModel = ((PlayerModel) (this.model));

            if (animationContainer != null && animationContainer.isActive()) {
                PlayerParts parts = animationContainer.data.parts();
                Player player = ((Player)(Object)entity);

                if (!parts.body.isVisible) {
                    ci.cancel();
                }

                if (((PlayerAnimationModel)(animationRenderer.getGeoModel())).allResourcesExist((AbstractClientPlayer) entity)) {
                    animationRenderer.setPlayerModel(playerModel);
                    animationRenderer.render((AbstractClientPlayer) entity, entityYaw, partialTicks, matrixStack, buffer, packedLight);
//                    AnimatableManager<AbstractClientPlayer> manager = player.getAnimatableInstanceCache().getManagerForId(player.getId());
//                    AnimationController<AbstractClientPlayer> controller = manager.getAnimationControllers().get(ModInit.MOD_ID);
//                    if (controller.getCurrentRawAnimation() == null || !controller.getCurrentAnimation().animation().name().equals(ConditionalAnimations.getAnimationForCurrentConditions(animationContainer.data).getNamespace())) {
//                        controller.triggerableAnim(ConditionalAnimations.getAnimationForCurrentConditions(animationContainer.data).getPath(), RawAnimation.begin().then(ConditionalAnimations.getAnimationForCurrentConditions(animationContainer.data).getPath(), Animation.LoopType.DEFAULT));
//                        controller.tryTriggerAnimation(ConditionalAnimations.getAnimationForCurrentConditions(animationContainer.data).getPath());
//                    }
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

            for (EntityRenderer renderer : PlayerEffectsRendererRegistry.getRenderers()) {
                if (renderer instanceof PlayerModelInterface) {
                    ((PlayerModelInterface)renderer).setPlayerModel(playerModel);
                    renderer.render(entity, entityYaw, partialTicks, matrixStack, buffer, packedLight);
                }
            }
        }
    }
}
