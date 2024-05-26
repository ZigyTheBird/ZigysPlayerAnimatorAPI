package zigy.playeranimatorapi.fabric.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.kosmx.playerAnim.core.util.Vec3f;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import zigy.playeranimatorapi.fabric.interfaces.CameraInterface;
import zigy.playeranimatorapi.utils.CameraUtils;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Shadow @Final private Camera mainCamera;

    @Inject(method = "renderLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Camera;setup(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/world/entity/Entity;ZZF)V", shift = At.Shift.AFTER))
    private void computeCameraAngles(float partialTicks, long finishTimeNano, PoseStack matrixStack, CallbackInfo ci) {
        CameraUtils.computeCameraLocation(((GameRenderer)(Object)this), this.mainCamera, partialTicks);
        Vec3f vec = CameraUtils.computeCameraAngles(((GameRenderer)(Object)this), this.mainCamera, partialTicks);
        if (vec != null) {
            ((CameraInterface)mainCamera).PAAPI$setAnglesInternal(vec.getX(), vec.getY());
            if (vec.getZ() != 0) {
                matrixStack.mulPose(Axis.ZP.rotationDegrees(vec.getZ()));
            }
        }
    }
}

