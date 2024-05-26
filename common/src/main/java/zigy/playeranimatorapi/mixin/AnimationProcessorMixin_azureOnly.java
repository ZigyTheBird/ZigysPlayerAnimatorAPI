package zigy.playeranimatorapi.mixin;

import mod.azure.azurelib.common.internal.common.core.animatable.GeoAnimatable;
import mod.azure.azurelib.common.internal.common.core.animatable.model.CoreGeoBone;
import mod.azure.azurelib.common.internal.common.core.animatable.model.CoreGeoModel;
import mod.azure.azurelib.common.internal.common.core.animation.*;
import mod.azure.azurelib.common.internal.common.core.keyframe.AnimationPoint;
import mod.azure.azurelib.common.internal.common.core.keyframe.BoneAnimationQueue;
import mod.azure.azurelib.common.internal.common.core.state.BoneSnapshot;
import mod.azure.azurelib.common.internal.common.core.utils.Interpolations;
import net.minecraft.client.player.AbstractClientPlayer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;

@Mixin(AnimationProcessor.class)
public abstract class AnimationProcessorMixin_azureOnly<T extends GeoAnimatable> {

    @Shadow(remap = false) protected abstract Map<String, BoneSnapshot> updateBoneSnapshots(Map<String, BoneSnapshot> snapshots);

    @Shadow(remap = false) public boolean reloadAnimations;

    @Shadow(remap = false) @Final private Map<String, CoreGeoBone> bones;

    @Shadow(remap = false) protected abstract void resetBoneTransformationMarkers();

    @Shadow(remap = false) public abstract Collection<CoreGeoBone> getRegisteredBones();

    @Inject(method = "tickAnimation", at = @At("HEAD"), cancellable = true, remap = false)
    private void inject(T animatable, CoreGeoModel<T> model, AnimatableManager<T> animatableManager, double animTime, AnimationState<T> event, boolean crashWhenCantFindBone, CallbackInfo ci) {
        if (animatable instanceof AbstractClientPlayer) {
            List<CoreGeoBone> disabledBones = new ArrayList<>();
            Map<String, BoneSnapshot> boneSnapshots = this.updateBoneSnapshots(animatableManager.getBoneSnapshotCollection());
            Iterator var9 = animatableManager.getAnimationControllers().values().iterator();

            addDisabled(disabledBones, "body", model);
            addDisabled(disabledBones, "head", model);
            addDisabled(disabledBones, "torso", model);
            addDisabled(disabledBones, "right_arm", model);
            addDisabled(disabledBones, "left_arm", model);
            addDisabled(disabledBones, "rightItem", model);
            addDisabled(disabledBones, "leftItem", model);
            addDisabled(disabledBones, "right_leg", model);
            addDisabled(disabledBones, "left_leg", model);

            Iterator var11;
            BoneSnapshot saveSnapshot;
            while(var9.hasNext()) {
                AnimationController<T> controller = (AnimationController) var9.next();
                if (this.reloadAnimations) {
                    controller.forceAnimationReset();
                    controller.getBoneAnimationQueues().clear();
                }

                ((AnimationControllerAccessor_azureOnly)controller).setIsJustStarting(animatableManager.isFirstTick());
                event.withController(controller);
                controller.process(model, event, this.bones, boneSnapshots, animTime, crashWhenCantFindBone);
                var11 = controller.getBoneAnimationQueues().values().iterator();

                while (var11.hasNext()) {
                    BoneAnimationQueue boneAnimation = (BoneAnimationQueue) var11.next();
                    CoreGeoBone bone = boneAnimation.bone();
                    if (disabledBones.contains(bone)) {
                        continue;
                    }
                    saveSnapshot = (BoneSnapshot) boneSnapshots.get(bone.getName());
                    BoneSnapshot initialSnapshot = bone.getInitialSnapshot();
                    AnimationPoint rotXPoint = (AnimationPoint) boneAnimation.rotationXQueue().poll();
                    AnimationPoint rotYPoint = (AnimationPoint) boneAnimation.rotationYQueue().poll();
                    AnimationPoint rotZPoint = (AnimationPoint) boneAnimation.rotationZQueue().poll();
                    AnimationPoint posXPoint = (AnimationPoint) boneAnimation.positionXQueue().poll();
                    AnimationPoint posYPoint = (AnimationPoint) boneAnimation.positionYQueue().poll();
                    AnimationPoint posZPoint = (AnimationPoint) boneAnimation.positionZQueue().poll();
                    AnimationPoint scaleXPoint = (AnimationPoint) boneAnimation.scaleXQueue().poll();
                    AnimationPoint scaleYPoint = (AnimationPoint) boneAnimation.scaleYQueue().poll();
                    AnimationPoint scaleZPoint = (AnimationPoint) boneAnimation.scaleZQueue().poll();
                    EasingType easingType = (EasingType) ((AnimationControllerAccessor_azureOnly)controller).getOverrideEasingTypeFunction().apply(animatable);
                    if (rotXPoint != null && rotYPoint != null && rotZPoint != null) {
                        bone.setRotX((float) EasingType.lerpWithOverride(rotXPoint, easingType) + initialSnapshot.getRotX());
                        bone.setRotY((float) EasingType.lerpWithOverride(rotYPoint, easingType) + initialSnapshot.getRotY());
                        bone.setRotZ((float) EasingType.lerpWithOverride(rotZPoint, easingType) + initialSnapshot.getRotZ());
                        saveSnapshot.updateRotation(bone.getRotX(), bone.getRotY(), bone.getRotZ());
                        saveSnapshot.startRotAnim();
                        bone.markRotationAsChanged();
                    }

                    if (posXPoint != null && posYPoint != null && posZPoint != null) {
                        bone.setPosX((float) EasingType.lerpWithOverride(posXPoint, easingType));
                        bone.setPosY((float) EasingType.lerpWithOverride(posYPoint, easingType));
                        bone.setPosZ((float) EasingType.lerpWithOverride(posZPoint, easingType));
                        saveSnapshot.updateOffset(bone.getPosX(), bone.getPosY(), bone.getPosZ());
                        saveSnapshot.startPosAnim();
                        bone.markPositionAsChanged();
                    }

                    if (scaleXPoint != null && scaleYPoint != null && scaleZPoint != null) {
                        bone.setScaleX((float) EasingType.lerpWithOverride(scaleXPoint, easingType));
                        bone.setScaleY((float) EasingType.lerpWithOverride(scaleYPoint, easingType));
                        bone.setScaleZ((float) EasingType.lerpWithOverride(scaleZPoint, easingType));
                        saveSnapshot.updateScale(bone.getScaleX(), bone.getScaleY(), bone.getScaleZ());
                        saveSnapshot.startScaleAnim();
                        bone.markScaleAsChanged();
                    }
                }
            }

            this.reloadAnimations = false;
            double resetTickLength = animatable.getBoneResetTime();
            var11 = this.getRegisteredBones().iterator();

            while(var11.hasNext()) {
                CoreGeoBone bone = (CoreGeoBone)var11.next();
                BoneSnapshot initialSnapshot;
                double percentageReset;
                if (disabledBones.contains(bone)) {
                    continue;
                }
                if (!bone.hasRotationChanged()) {
                    initialSnapshot = bone.getInitialSnapshot();
                    saveSnapshot = (BoneSnapshot)boneSnapshots.get(bone.getName());
                    if (saveSnapshot.isRotAnimInProgress()) {
                        saveSnapshot.stopRotAnim(animTime);
                    }

                    percentageReset = Math.min((animTime - saveSnapshot.getLastResetRotationTick()) / resetTickLength, 1.0);
                    bone.setRotX((float) Interpolations.lerp((double)saveSnapshot.getRotX(), (double)initialSnapshot.getRotX(), percentageReset));
                    bone.setRotY((float) Interpolations.lerp((double)saveSnapshot.getRotY(), (double)initialSnapshot.getRotY(), percentageReset));
                    bone.setRotZ((float) Interpolations.lerp((double)saveSnapshot.getRotZ(), (double)initialSnapshot.getRotZ(), percentageReset));
                    if (percentageReset >= 1.0) {
                        saveSnapshot.updateRotation(bone.getRotX(), bone.getRotY(), bone.getRotZ());
                    }
                }

                if (!bone.hasPositionChanged()) {
                    initialSnapshot = bone.getInitialSnapshot();
                    saveSnapshot = (BoneSnapshot)boneSnapshots.get(bone.getName());
                    if (saveSnapshot.isPosAnimInProgress()) {
                        saveSnapshot.stopPosAnim(animTime);
                    }

                    percentageReset = Math.min((animTime - saveSnapshot.getLastResetPositionTick()) / resetTickLength, 1.0);
                    bone.setPosX((float)Interpolations.lerp((double)saveSnapshot.getOffsetX(), (double)initialSnapshot.getOffsetX(), percentageReset));
                    bone.setPosY((float)Interpolations.lerp((double)saveSnapshot.getOffsetY(), (double)initialSnapshot.getOffsetY(), percentageReset));
                    bone.setPosZ((float)Interpolations.lerp((double)saveSnapshot.getOffsetZ(), (double)initialSnapshot.getOffsetZ(), percentageReset));
                    if (percentageReset >= 1.0) {
                        saveSnapshot.updateOffset(bone.getPosX(), bone.getPosY(), bone.getPosZ());
                    }
                }

                if (!bone.hasScaleChanged()) {
                    initialSnapshot = bone.getInitialSnapshot();
                    saveSnapshot = (BoneSnapshot)boneSnapshots.get(bone.getName());
                    if (saveSnapshot.isScaleAnimInProgress()) {
                        saveSnapshot.stopScaleAnim(animTime);
                    }

                    percentageReset = Math.min((animTime - saveSnapshot.getLastResetScaleTick()) / resetTickLength, 1.0);
                    bone.setScaleX((float)Interpolations.lerp((double)saveSnapshot.getScaleX(), (double)initialSnapshot.getScaleX(), percentageReset));
                    bone.setScaleY((float)Interpolations.lerp((double)saveSnapshot.getScaleY(), (double)initialSnapshot.getScaleY(), percentageReset));
                    bone.setScaleZ((float)Interpolations.lerp((double)saveSnapshot.getScaleZ(), (double)initialSnapshot.getScaleZ(), percentageReset));
                    if (percentageReset >= 1.0) {
                        saveSnapshot.updateScale(bone.getScaleX(), bone.getScaleY(), bone.getScaleZ());
                    }
                }
            }

            this.resetBoneTransformationMarkers();
            ((AnimatableManagerAccessor_azureOnly)animatableManager).callFinishFirstTick();
            ci.cancel();
        }
    }

    @Unique
    private static void addDisabled(List<CoreGeoBone> list, String bone, CoreGeoModel model) {
        if (model.getBone(bone).isPresent()) {
            list.add((CoreGeoBone) model.getBone(bone).get());
        }
    }
}
