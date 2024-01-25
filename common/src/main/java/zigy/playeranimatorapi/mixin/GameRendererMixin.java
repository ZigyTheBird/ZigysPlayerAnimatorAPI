package zigy.playeranimatorapi.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.kosmx.playerAnim.api.TransformType;
import dev.kosmx.playerAnim.core.util.Vec3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import zigy.playeranimatorapi.playeranims.CustomModifierLayer;
import zigy.playeranimatorapi.playeranims.PlayerAnimations;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Inject(method = "renderLevel", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/client/Camera;setup(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/world/entity/Entity;ZZF)V"))
    private static void computeCameraAngles(float partialTicks, long finishTimeNano, PoseStack matrixStack, CallbackInfo ci) {
        CustomModifierLayer layer = PlayerAnimations.getModifierLayer(Minecraft.getInstance().player);
        if (layer != null && layer.cameraAnimEnabled) {
            Vec3f pos = layer.get3DTransform("head", TransformType.POSITION, partialTicks, Vec3f.ZERO);
            Vec3f rot = layer.get3DTransform("head", TransformType.ROTATION, partialTicks, Vec3f.ZERO);

            matrixStack.pushPose();
            matrixStack.translate(pos.getX(), pos.getY(), pos.getZ());
            matrixStack.mulPose(Axis.ZP.rotation(rot.getZ()));    //roll
            matrixStack.mulPose(Axis.YP.rotation(rot.getY()));    //pitch
            matrixStack.mulPose(Axis.XP.rotation(rot.getX()));    //yaw
        }
    }
}
