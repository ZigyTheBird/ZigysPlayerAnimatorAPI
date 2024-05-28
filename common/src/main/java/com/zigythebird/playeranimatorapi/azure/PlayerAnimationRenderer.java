package com.zigythebird.playeranimatorapi.azure;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.zigythebird.multiloaderutils.utils.Platform;
import com.zigythebird.playeranimatorapi.compatibility.PehkuiCompat;
import com.zigythebird.playeranimatorapi.misc.PlayerModelInterface;
import mod.azure.azurelib.common.api.client.renderer.GeoObjectRenderer;
import mod.azure.azurelib.common.internal.client.util.RenderUtils;
import mod.azure.azurelib.common.internal.common.cache.object.BakedGeoModel;
import mod.azure.azurelib.common.internal.common.cache.object.GeoBone;
import mod.azure.azurelib.common.internal.common.constant.DataTickets;
import mod.azure.azurelib.common.internal.common.core.animation.AnimationState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

public class PlayerAnimationRenderer extends GeoObjectRenderer<AnimatablePlayerLayer> implements PlayerModelInterface {

    public PlayerModel playerModel;

    private static final Vec3 head_offset = new Vec3(0, 2, 0);
    private static final Vec3 right_arm_offset = new Vec3(5, -1, 0);
    private static final Vec3 left_arm_offset = new Vec3(-5, -1, 0);
    private static final Vec3 right_leg_offset = new Vec3(1.9, -12, 0);
    private static final Vec3 left_leg_offset = new Vec3(-1.9, -12, 0);

    public PlayerAnimationRenderer() {
        super(new PlayerAnimationModel());
    }

    @Override
    public void preRender(PoseStack poseStack, AnimatablePlayerLayer animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.objectRenderTranslations = new Matrix4f(poseStack.last().pose());

        scaleModelForRender(this.scaleWidth, this.scaleHeight, poseStack, animatable, model, isReRender, partialTick, packedLight, packedOverlay);

        poseStack.pushPose();
        if (Platform.isModLoaded("pehkui")) {
            Vec2 scale = PehkuiCompat.getScale(animatable.getPlayer(), partialTick);
            poseStack.scale(scale.x, scale.y, scale.x);
        }
        poseStack.popPose();
    }

    @Override
    public void render(PoseStack poseStack, AnimatablePlayerLayer animatable, @Nullable MultiBufferSource bufferSource, @Nullable RenderType renderType, @Nullable VertexConsumer buffer, int packedLight) {
        if (renderType == null) {
            renderType = RenderType.entityTranslucent(model.getTextureResource(animatable));
        }
        super.render(poseStack, animatable, bufferSource, renderType, buffer, packedLight);
    }

    @Override
    public void actuallyRender(PoseStack poseStack, AnimatablePlayerLayer animatable, BakedGeoModel model, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        poseStack.pushPose();

        LivingEntity livingEntity = animatable.getPlayer();

        boolean shouldSit = animatable.getPlayer().isPassenger() && (animatable.getPlayer().getVehicle() != null);
        float lerpBodyRot = livingEntity == null ? 0 : Mth.rotLerp(partialTick, livingEntity.yBodyRotO,
                livingEntity.yBodyRot);
        float lerpHeadRot = livingEntity == null ? 0 : Mth.rotLerp(partialTick, livingEntity.yHeadRotO,
                livingEntity.yHeadRot);
        float netHeadYaw;

        if (shouldSit && animatable.getPlayer().getVehicle() instanceof LivingEntity livingentity) {
            lerpBodyRot = Mth.rotLerp(partialTick, livingentity.yBodyRotO, livingentity.yBodyRot);
            netHeadYaw = lerpHeadRot - lerpBodyRot;
            float clampedHeadYaw = Mth.clamp(Mth.wrapDegrees(netHeadYaw), -85, 85);
            lerpBodyRot = lerpHeadRot - clampedHeadYaw;

            if (clampedHeadYaw * clampedHeadYaw > 2500f) lerpBodyRot += clampedHeadYaw * 0.2f;
        }

        if (animatable.getPlayer().getPose() == Pose.SLEEPING && livingEntity != null) {
            Direction bedDirection = livingEntity.getBedOrientation();

            if (bedDirection != null) {
                float eyePosOffset = livingEntity.getEyeHeight(Pose.STANDING) - 0.1F;

                poseStack.translate(-bedDirection.getStepX() * eyePosOffset, 0,
                        -bedDirection.getStepZ() * eyePosOffset);
            }
        }

        float ageInTicks = animatable.getPlayer().tickCount + partialTick;

        applyRotations(animatable, poseStack, ageInTicks, lerpBodyRot, partialTick);

        if (!isReRender) {
            AnimationState<AnimatablePlayerLayer> animationState = new AnimationState<>(animatable, 0, 0, partialTick, false);
            long instanceId = getInstanceId(animatable);
            animationState.setData(DataTickets.TICK, animatable.getTick(animatable));

            this.model.addAdditionalStateData(animatable, instanceId, animationState::setData);
            this.model.handleAnimations(animatable, instanceId, animationState);
        }

        poseStack.translate(0, 0.01f, 0);

        this.modelRenderTranslations = new Matrix4f(poseStack.last().pose());

        if (!animatable.getPlayer().isInvisibleTo(Minecraft.getInstance().player)) {
            updateAnimatedTextureFrame(animatable);
            for (GeoBone group : model.topLevelBones()) {
                renderRecursively(poseStack, animatable, group, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
            }
        }
        poseStack.popPose();
    }

    protected void applyRotations(AnimatablePlayerLayer animatable, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTick) {
        Pose pose = animatable.getPlayer().getPose();
        LivingEntity livingEntity = animatable.getPlayer();

        if (this.isShaking(animatable.getPlayer())) {
            rotationYaw += (float) (Math.cos((double) animatable.getPlayer().tickCount * 3.25) * Math.PI * 0.4000000059604645);
        }

        if (pose != Pose.SLEEPING) poseStack.mulPose(Axis.YP.rotationDegrees(180f - rotationYaw));

        if (livingEntity != null && livingEntity.deathTime > 0) {
            float deathRotation = (livingEntity.deathTime + partialTick - 1f) / 20f * 1.6f;

            poseStack.mulPose(
                    Axis.ZP.rotationDegrees(Math.min(Mth.sqrt(deathRotation), 1) * getDeathMaxRotation(animatable.getPlayer())));
        } else if (livingEntity != null && livingEntity.isAutoSpinAttack()) {
            poseStack.mulPose(Axis.XP.rotationDegrees(-90f - livingEntity.getXRot()));
            poseStack.mulPose(Axis.YP.rotationDegrees((livingEntity.tickCount + partialTick) * -75f));
        } else if (livingEntity != null && pose == Pose.SLEEPING) {
            Direction bedOrientation = livingEntity.getBedOrientation();

            poseStack.mulPose(Axis.YP.rotationDegrees(
                    bedOrientation != null ? RenderUtils.getDirectionAngle(bedOrientation) : rotationYaw));
            poseStack.mulPose(Axis.ZP.rotationDegrees(getDeathMaxRotation(animatable.getPlayer())));
            poseStack.mulPose(Axis.YP.rotationDegrees(270f));
        } else {
            String name = animatable.getPlayer().getName().getString();

            if (!animatable.getPlayer().isModelPartShown(PlayerModelPart.CAPE)) return;

            if (name.equals("Dinnerbone") || name.equalsIgnoreCase("Grumm")) {
                poseStack.translate(0, animatable.getPlayer().getBbHeight() + 0.1f, 0);
                poseStack.mulPose(Axis.ZP.rotationDegrees(180f));
            }
        }
    }

    public boolean isShaking(AbstractClientPlayer entity) {
        return entity.isFullyFrozen();
    }

    protected float getDeathMaxRotation(AbstractClientPlayer animatable) {
        return 90f;
    }

    public void setPlayerModel(PlayerModel model) {
        this.playerModel = model;
    }

    public void setupAnim(BakedGeoModel model) {
        matchPlayerModel(model, playerModel.head, "head");
        matchPlayerModel(model, playerModel.body, "torso");
        matchPlayerModel(model, playerModel.rightArm, "right_arm");
        matchPlayerModel(model, playerModel.leftArm, "left_arm");
        matchPlayerModel(model, playerModel.rightLeg, "right_leg");
        matchPlayerModel(model, playerModel.leftLeg, "left_leg");
    }

    public void matchPlayerModel(BakedGeoModel model, ModelPart part, String name) {
        if (model.getBone(name).isPresent()) {
            GeoBone bone = model.getBone(name).get();
            Vec3 offset = getPositionOffsetForPart(name);

            bone.setPosX(-(part.x + (float) offset.x));
            bone.setPosY(-(part.y + (float) offset.y));
            bone.setPosZ(part.z + (float) offset.z);
            bone.setRotX(-part.xRot);
            bone.setRotY(-part.yRot);
            bone.setRotZ(part.zRot);
        }
    }

    public Vec3 getPositionOffsetForPart(String part) {
        switch (part) {
            case "head" -> {
                return head_offset;
            }
            case "right_arm" -> {
                return right_arm_offset;
            }
            case "left_arm" -> {
                return left_arm_offset;
            }
            case "right_leg" -> {
                return right_leg_offset;
            }
            case "left_leg" -> {
                return left_leg_offset;
            }
        }
        return Vec3.ZERO;
    }

    @Override
    public long getInstanceId(AnimatablePlayerLayer animatable) {
        return animatable.getPlayer().getId();
    }
}
