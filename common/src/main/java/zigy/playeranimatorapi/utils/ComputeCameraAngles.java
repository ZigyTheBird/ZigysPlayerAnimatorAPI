package zigy.playeranimatorapi.utils;

import dev.kosmx.playerAnim.core.util.Vec3f;
import net.minecraft.client.Camera;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import zigy.playeranimatorapi.modifier.AbstractCameraModifier;
import zigy.playeranimatorapi.playeranims.CustomModifierLayer;
import zigy.playeranimatorapi.playeranims.PlayerAnimations;

public class ComputeCameraAngles {

    public static void computeCameraAngles(GameRenderer renderer, Camera camera, double partialTicks) {
        CustomModifierLayer layer = PlayerAnimations.getModifierLayer(Minecraft.getInstance().player);
        if (layer != null && layer.isActive() && layer.cameraAnimEnabled && Minecraft.getInstance().options.getCameraType().equals(CameraType.FIRST_PERSON)) {
            Vec3f rot = Vec3f.ZERO;
            for (Object object : layer.cameraModifiers) {
                AbstractCameraModifier modifier = (AbstractCameraModifier) object;
                rot = modifier.get3DCameraRotation(renderer, (float) partialTicks, rot);
            }

            camera.API$setAnglesInternal(rot.getX(), rot.getY());
        }
    }
}
