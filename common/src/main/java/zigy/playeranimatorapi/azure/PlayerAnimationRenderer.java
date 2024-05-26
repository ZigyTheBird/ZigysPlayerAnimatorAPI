package zigy.playeranimatorapi.azure;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mod.azure.azurelib.common.api.client.renderer.GeoEntityRenderer;
import mod.azure.azurelib.common.internal.common.cache.object.BakedGeoModel;
import mod.azure.azurelib.common.internal.common.cache.object.GeoBone;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import zigy.playeranimatorapi.compatibility.PehkuiCompat;
import zigy.playeranimatorapi.misc.PlayerModelInterface;
import zigy.zigysmultiloaderutils.utils.Platform;

public class PlayerAnimationRenderer extends GeoEntityRenderer<AbstractClientPlayer> implements PlayerModelInterface {

    public PlayerModel playerModel;

    private static final Vec3 head_offset = new Vec3(0, 2, 0);
    private static final Vec3 right_arm_offset = new Vec3(5, -1, 0);
    private static final Vec3 left_arm_offset = new Vec3(-5, -1, 0);
    private static final Vec3 right_leg_offset = new Vec3(1.9, -12, 0);
    private static final Vec3 left_leg_offset = new Vec3(-1.9, -12, 0);

    public PlayerAnimationRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new PlayerAnimationModel());
    }

    @Override
    public void preRender(PoseStack poseStack, AbstractClientPlayer player, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);

        poseStack.pushPose();
        if (Platform.isModLoaded("pehkui")) {
            Vec2 scale = PehkuiCompat.getScale(player, partialTick);
            poseStack.scale(scale.x, scale.y, scale.x);
        }
        poseStack.popPose();
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
}
