package com.zigythebird.playeranimatorapi.mixin.gecko;

import com.zigythebird.playeranimatorapi.gecko.AnimatablePlayerLayer;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.animation.keyframe.AnimationPoint;
import software.bernie.geckolib.animation.keyframe.BoneAnimationQueue;
import software.bernie.geckolib.animation.state.BoneSnapshot;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.GeoModel;

import java.util.*;

@Mixin(AnimationProcessor.class)
public abstract class AnimationProcessorMixin_geckoOnly<T extends GeoAnimatable> {

    @Shadow(remap = false) protected abstract Map<String, BoneSnapshot> updateBoneSnapshots(Map<String, BoneSnapshot> snapshots);

    @Shadow(remap = false) public boolean reloadAnimations;

    @Shadow(remap = false) protected abstract void resetBoneTransformationMarkers();

    @Shadow(remap = false) @Final private Map<String, GeoBone> bones;

    @Shadow(remap = false) public abstract Collection<GeoBone> getRegisteredBones();

    @Inject(method = "tickAnimation", at = @At("HEAD"), cancellable = true, remap = false)
    private void inject(T animatable, GeoModel<T> model, AnimatableManager<T> animatableManager, double animTime, AnimationState<T> state, boolean crashWhenCantFindBone, CallbackInfo ci) {
        if (animatable instanceof AnimatablePlayerLayer) {
            List<GeoBone> disabledBones = new ArrayList<>();

            addDisabled(disabledBones, "body", model);
            addDisabled(disabledBones, "head", model);
            addDisabled(disabledBones, "torso", model);
            addDisabled(disabledBones, "right_arm", model);
            addDisabled(disabledBones, "left_arm", model);
            addDisabled(disabledBones, "right_leg", model);
            addDisabled(disabledBones, "left_leg", model);

            Map<String, BoneSnapshot> boneSnapshots = updateBoneSnapshots(animatableManager.getBoneSnapshotCollection());

            for (AnimationController<T> controller : animatableManager.getAnimationControllers().values()) {
                if (this.reloadAnimations) {
                    controller.forceAnimationReset();
                    controller.getBoneAnimationQueues().clear();
                }

                ((AnimationControllerAccessor_geckoOnly)controller).setIsJustStarting(animatableManager.isFirstTick());

                state.withController(controller);
                controller.process(model, state, this.bones, boneSnapshots, animTime, crashWhenCantFindBone);

                for (BoneAnimationQueue boneAnimation : controller.getBoneAnimationQueues().values()) {
                    GeoBone bone = boneAnimation.bone();
                    if (disabledBones.contains(bone)) {
                        continue;
                    }
                    BoneSnapshot snapshot = boneSnapshots.get(bone.getName());
                    BoneSnapshot initialSnapshot = bone.getInitialSnapshot();

                    AnimationPoint rotXPoint = boneAnimation.rotationXQueue().poll();
                    AnimationPoint rotYPoint = boneAnimation.rotationYQueue().poll();
                    AnimationPoint rotZPoint = boneAnimation.rotationZQueue().poll();
                    AnimationPoint posXPoint = boneAnimation.positionXQueue().poll();
                    AnimationPoint posYPoint = boneAnimation.positionYQueue().poll();
                    AnimationPoint posZPoint = boneAnimation.positionZQueue().poll();
                    AnimationPoint scaleXPoint = boneAnimation.scaleXQueue().poll();
                    AnimationPoint scaleYPoint = boneAnimation.scaleYQueue().poll();
                    AnimationPoint scaleZPoint = boneAnimation.scaleZQueue().poll();
                    EasingType easingType = (EasingType) ((AnimationControllerAccessor_geckoOnly)controller).getOverrideEasingTypeFunction().apply(animatable);

                    if (rotXPoint != null && rotYPoint != null && rotZPoint != null) {
                        bone.setRotX((float)EasingType.lerpWithOverride(rotXPoint, easingType) + initialSnapshot.getRotX());
                        bone.setRotY((float)EasingType.lerpWithOverride(rotYPoint, easingType) + initialSnapshot.getRotY());
                        bone.setRotZ((float)EasingType.lerpWithOverride(rotZPoint, easingType) + initialSnapshot.getRotZ());
                        snapshot.updateRotation(bone.getRotX(), bone.getRotY(), bone.getRotZ());
                        snapshot.startRotAnim();
                        bone.markRotationAsChanged();
                    }

                    if (posXPoint != null && posYPoint != null && posZPoint != null) {
                        bone.setPosX((float)EasingType.lerpWithOverride(posXPoint, easingType));
                        bone.setPosY((float)EasingType.lerpWithOverride(posYPoint, easingType));
                        bone.setPosZ((float)EasingType.lerpWithOverride(posZPoint, easingType));
                        snapshot.updateOffset(bone.getPosX(), bone.getPosY(), bone.getPosZ());
                        snapshot.startPosAnim();
                        bone.markPositionAsChanged();
                    }

                    if (scaleXPoint != null && scaleYPoint != null && scaleZPoint != null) {
                        bone.setScaleX((float)EasingType.lerpWithOverride(scaleXPoint, easingType));
                        bone.setScaleY((float)EasingType.lerpWithOverride(scaleYPoint, easingType));
                        bone.setScaleZ((float)EasingType.lerpWithOverride(scaleZPoint, easingType));
                        snapshot.updateScale(bone.getScaleX(), bone.getScaleY(), bone.getScaleZ());
                        snapshot.startScaleAnim();
                        bone.markScaleAsChanged();
                    }
                }
            }

            this.reloadAnimations = false;
            double resetTickLength = animatable.getBoneResetTime();

            for (GeoBone bone : getRegisteredBones()) {
                if (!bone.hasRotationChanged()) {
                    BoneSnapshot initialSnapshot = bone.getInitialSnapshot();
                    BoneSnapshot saveSnapshot = boneSnapshots.get(bone.getName());

                    if (saveSnapshot.isRotAnimInProgress())
                        saveSnapshot.stopRotAnim(animTime);

                    double percentageReset = Math.min((animTime - saveSnapshot.getLastResetRotationTick()) / resetTickLength, 1);

                    bone.setRotX((float)Mth.lerp(percentageReset, saveSnapshot.getRotX(), initialSnapshot.getRotX()));
                    bone.setRotY((float)Mth.lerp(percentageReset, saveSnapshot.getRotY(), initialSnapshot.getRotY()));
                    bone.setRotZ((float)Mth.lerp(percentageReset, saveSnapshot.getRotZ(), initialSnapshot.getRotZ()));

                    if (percentageReset >= 1)
                        saveSnapshot.updateRotation(bone.getRotX(), bone.getRotY(), bone.getRotZ());
                }

                if (!bone.hasPositionChanged()) {
                    BoneSnapshot initialSnapshot = bone.getInitialSnapshot();
                    BoneSnapshot saveSnapshot = boneSnapshots.get(bone.getName());

                    if (saveSnapshot.isPosAnimInProgress())
                        saveSnapshot.stopPosAnim(animTime);

                    double percentageReset = Math.min((animTime - saveSnapshot.getLastResetPositionTick()) / resetTickLength, 1);

                    bone.setPosX((float)Mth.lerp(percentageReset, saveSnapshot.getOffsetX(), initialSnapshot.getOffsetX()));
                    bone.setPosY((float)Mth.lerp(percentageReset, saveSnapshot.getOffsetY(), initialSnapshot.getOffsetY()));
                    bone.setPosZ((float)Mth.lerp(percentageReset, saveSnapshot.getOffsetZ(), initialSnapshot.getOffsetZ()));

                    if (percentageReset >= 1)
                        saveSnapshot.updateOffset(bone.getPosX(), bone.getPosY(), bone.getPosZ());
                }

                if (!bone.hasScaleChanged()) {
                    BoneSnapshot initialSnapshot = bone.getInitialSnapshot();
                    BoneSnapshot saveSnapshot = boneSnapshots.get(bone.getName());

                    if (saveSnapshot.isScaleAnimInProgress())
                        saveSnapshot.stopScaleAnim(animTime);

                    double percentageReset = Math.min((animTime - saveSnapshot.getLastResetScaleTick()) / resetTickLength, 1);

                    bone.setScaleX((float)Mth.lerp(percentageReset, saveSnapshot.getScaleX(), initialSnapshot.getScaleX()));
                    bone.setScaleY((float)Mth.lerp(percentageReset, saveSnapshot.getScaleY(), initialSnapshot.getScaleY()));
                    bone.setScaleZ((float)Mth.lerp(percentageReset, saveSnapshot.getScaleZ(), initialSnapshot.getScaleZ()));

                    if (percentageReset >= 1)
                        saveSnapshot.updateScale(bone.getScaleX(), bone.getScaleY(), bone.getScaleZ());
                }
            }

            resetBoneTransformationMarkers();
            ((AnimatableManagerAccessor_geckoOnly)animatableManager).callFinishFirstTick();
            ci.cancel();
        }
    }

    @Unique
    private static void addDisabled(List<GeoBone> list, String bone, GeoModel model) {
        if (model.getBone(bone).isPresent()) {
            list.add((GeoBone) model.getBone(bone).get());
        }
    }
}
