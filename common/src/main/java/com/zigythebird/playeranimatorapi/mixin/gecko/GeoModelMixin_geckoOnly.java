package com.zigythebird.playeranimatorapi.mixin.gecko;


import com.zigythebird.playeranimatorapi.gecko.AnimatablePlayerLayer;
import com.zigythebird.playeranimatorapi.gecko.PlayerAnimationModel;
import com.zigythebird.playeranimatorapi.mixin.KeyframeAnimationPlayerAccessor;
import com.zigythebird.playeranimatorapi.playeranims.CustomModifierLayer;
import com.zigythebird.playeranimatorapi.playeranims.PlayerAnimations;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationProcessor;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.GeoModel;

@Mixin(GeoModel.class)
public abstract class GeoModelMixin_geckoOnly<T extends GeoAnimatable> {

    @Shadow(remap = false) private long lastRenderedInstance;

    @Shadow(remap = false) private double lastGameTickTime;

    @Shadow(remap = false) private double animTime;

    @Shadow(remap = false) public abstract boolean crashIfBoneMissing();

    @Shadow(remap = false) public abstract AnimationProcessor<T> getAnimationProcessor();

    @Inject(method = "handleAnimations", at = @At("HEAD"), cancellable = true, remap = false)
    private void inject(T animatable, long instanceId, AnimationState<T> animationState, float partialTick, CallbackInfo ci) {
        if ((Object)this instanceof PlayerAnimationModel) {
            Minecraft mc = Minecraft.getInstance();
            AnimatableManager<T> animatableManager = animatable.getAnimatableInstanceCache().getManagerForId(instanceId);
            Double currentTick = animationState.getData(DataTickets.TICK);
            CustomModifierLayer modifierLayer = PlayerAnimations.getModifierLayer(((AnimatablePlayerLayer)(animatable)).getPlayer());

            if (animatableManager.getFirstTickTime() == -1)
                animatableManager.startedAt(currentTick + mc.getTimer().getGameTimeDeltaTicks() - modifierLayer.animPlayer.getTick() - ((KeyframeAnimationPlayerAccessor)modifierLayer.animPlayer).getTickDelta());

            if (currentTick == null) {
                LivingEntity livingEntity = (LivingEntity) animatable;
                currentTick = (double) livingEntity.tickCount;
            }

            if (animatableManager.getFirstTickTime() == -1)
                animatableManager.startedAt(currentTick + mc.getTimer().getGameTimeDeltaTicks());

            double currentFrameTime = currentTick - animatableManager.getFirstTickTime();
            boolean isReRender = !animatableManager.isFirstTick() && currentFrameTime == animatableManager.getLastUpdateTime();

            if (isReRender && instanceId == this.lastRenderedInstance)
                return;

            if (!isReRender && (!mc.isPaused() || animatable.shouldPlayAnimsWhileGamePaused())) {
                animatableManager.updatedAt(currentFrameTime);

                double lastUpdateTime = animatableManager.getLastUpdateTime();
                this.animTime += lastUpdateTime - this.lastGameTickTime;
                this.lastGameTickTime = lastUpdateTime;
            }

            animationState.animationTick = this.animTime;
            AnimationProcessor<T> processor = getAnimationProcessor();

            processor.preAnimationSetup(animationState, this.animTime);

            if (!processor.getRegisteredBones().isEmpty())
                processor.tickAnimation(animatable, (GeoModel)(Object)this, animatableManager, this.animTime, animationState, crashIfBoneMissing());

            ci.cancel();
        }
    }
}
