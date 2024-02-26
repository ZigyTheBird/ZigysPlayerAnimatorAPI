package zigy.playeranimatorapi.azure;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mod.azure.azurelib.cache.object.BakedGeoModel;
import mod.azure.azurelib.cache.object.GeoBone;
import mod.azure.azurelib.renderer.GeoEntityRenderer;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import zigy.playeranimatorapi.misc.PlayerModelInterface;

public class PlayerAnimationRenderer extends GeoEntityRenderer<AbstractClientPlayer> implements PlayerModelInterface {

    private PlayerModel playerModel;

    public PlayerAnimationRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new PlayerAnimationModel());
    }

    @Override
    public void preRender(PoseStack poseStack, AbstractClientPlayer player, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);

        matchPlayerModel(model, playerModel.head, "head");
        matchPlayerModel(model, playerModel.body, "torso");
        matchPlayerModel(model, playerModel.rightArm, "right_arm");
        matchPlayerModel(model, playerModel.leftArm, "left_arm");
        matchPlayerModel(model, playerModel.rightLeg, "right_leg");
        matchPlayerModel(model, playerModel.leftLeg, "left_leg");
    }

    public void setPlayerModel(PlayerModel model) {
        this.playerModel = model;
    }

    public void matchPlayerModel(BakedGeoModel model, ModelPart part, String name) {
        if (model.getBone(name).isPresent()) {
            GeoBone bone = model.getBone(name).get();

            bone.setPosX(part.x);
            bone.setPosY(part.y);
            bone.setPosZ(part.z);
            bone.setRotX(-part.xRot);
            bone.setRotY(-part.yRot);
            bone.setRotZ(part.zRot);
        }
    }
}
