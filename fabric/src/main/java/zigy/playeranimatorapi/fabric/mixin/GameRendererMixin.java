package zigy.playeranimatorapi.fabric.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import zigy.playeranimatorapi.utils.ComputeCameraAngles;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Shadow @Final private Camera mainCamera;

    @Inject(method = "renderLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Camera;setup(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/world/entity/Entity;ZZF)V", shift = At.Shift.AFTER))
    private void computeCameraAngles(float partialTicks, long finishTimeNano, PoseStack matrixStack, CallbackInfo ci) {
        ComputeCameraAngles.computeCameraAngles(((GameRenderer)(Object)this), this.mainCamera, partialTicks);
    }
}

