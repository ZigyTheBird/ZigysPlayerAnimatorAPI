package zigy.playeranimatorapi.azure;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.kosmx.playerAnim.api.TransformType;
import dev.kosmx.playerAnim.core.util.Vec3f;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import mod.azure.azurelib.cache.object.BakedGeoModel;
import mod.azure.azurelib.cache.object.GeoBone;
import mod.azure.azurelib.renderer.GeoEntityRenderer;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.apache.commons.lang3.text.WordUtils;
import zigy.playeranimatorapi.playeranims.CustomModifierLayer;

import java.util.NoSuchElementException;

import static zigy.playeranimatorapi.playeranims.PlayerAnimations.animationLayerId;

public class PlayerAnimationRenderer extends GeoEntityRenderer<AbstractClientPlayer> {
    public PlayerAnimationRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new PlayerAnimationModel());
    }

    @Override
    public void preRender(PoseStack poseStack, AbstractClientPlayer player, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);

//        poseStack.pushPose();
//        poseStack.mulPose(Axis.YP.rotation((float) (-CustomModifierLayer.playerLookRightWayRot(player, partialTick) * 0.017453292)));
//        poseStack.popPose();

        try {
            matchPlayerModel(player, model, "body", partialTick);
            matchPlayerModel(player, model, "head", partialTick);
            matchPlayerModel(player, model, "torso", partialTick);
            matchPlayerModel(player, model, "right_arm", partialTick);
            matchPlayerModel(player, model, "left_arm", partialTick);
            matchPlayerModel(player, model, "right_leg", partialTick);
            matchPlayerModel(player, model, "left_leg", partialTick);
            matchPlayerModel(player, model, "right_item", partialTick);
            matchPlayerModel(player, model, "left_item", partialTick);
        } catch (NoSuchElementException | NullPointerException ignore) {
        }
    }

    public void matchPlayerModel(AbstractClientPlayer player, BakedGeoModel model, String name, float partialTick) {
        try {
            GeoBone bone = model.getBone(name).get();
            CustomModifierLayer animationContainer = (CustomModifierLayer) PlayerAnimationAccess.getPlayerAssociatedData(player).get(animationLayerId);

            Vec3f pos = animationContainer.get3DTransform(name, TransformType.POSITION, partialTick, Vec3f.ZERO);
            Vec3f rot = animationContainer.get3DTransform(name, TransformType.POSITION, partialTick, Vec3f.ZERO);

            bone.setPosX(pos.getX() + bone.getPosX());
            bone.setPosY(pos.getY() + bone.getPosY());
            bone.setPosZ(pos.getZ() + bone.getPosY());
            bone.setRotX(rot.getX());
            bone.setRotY(rot.getY());
            bone.setRotZ(rot.getZ());

        } catch (NoSuchElementException | NullPointerException e) {
            String[] split = name.split("_");
            if (split.length == 2) {
                matchPlayerModel(player, model, split[0] + WordUtils.capitalize(split[1]), partialTick);
            }
        }
    }
}
