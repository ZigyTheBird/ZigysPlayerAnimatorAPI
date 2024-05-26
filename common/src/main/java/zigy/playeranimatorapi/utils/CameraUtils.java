package zigy.playeranimatorapi.utils;

import dev.kosmx.playerAnim.api.TransformType;
import dev.kosmx.playerAnim.core.util.Vec3f;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import zigy.playeranimatorapi.mixin.CameraAccessor;
import zigy.playeranimatorapi.modifier.AbstractCameraModifier;
import zigy.playeranimatorapi.playeranims.CustomModifierLayer;
import zigy.playeranimatorapi.playeranims.PlayerAnimations;

public class CameraUtils {

    public static @Nullable Vec3f computeCameraAngles(GameRenderer renderer, Camera camera, double partialTicks) {
        CustomModifierLayer layer = PlayerAnimations.getModifierLayer(Minecraft.getInstance().player);
        if (layer != null && layer.isActive() && layer.cameraAnimEnabled) {
            Vec3f rot = new Vec3f(camera.getXRot(), camera.getYRot(), 0).scale(0.017453292F);
            for (Object object : layer.cameraModifiers) {
                AbstractCameraModifier modifier = (AbstractCameraModifier) object;
                rot = modifier.get3DCameraTransform(renderer, camera, TransformType.ROTATION,(float) partialTicks, rot);
            }

            return rot.scale(1/0.017453292F);
        }
        return null;
    }

    public static void computeCameraLocation(GameRenderer renderer, @NotNull Camera camera, double partialTicks) {
        CustomModifierLayer layer = PlayerAnimations.getModifierLayer(Minecraft.getInstance().player);
        if (layer != null && layer.isActive() && layer.cameraAnimEnabled) {
            Vec3f pos = new Vec3f((float) camera.getPosition().x, (float) camera.getPosition().y, (float) camera.getPosition().z);
            Vec3f previousPos = new Vec3f(pos.getX(), pos.getY(), pos.getZ());
            for (Object object : layer.cameraModifiers) {
                AbstractCameraModifier modifier = (AbstractCameraModifier) object;
                Vec3f transform = modifier.get3DCameraTransform(renderer, camera, TransformType.POSITION, (float) partialTicks, Vec3f.ZERO).scale((float) 1 /16);
                Vec3 localMovement = ModMath.moveInLocalSpace(new Vec3(-transform.getX(), -transform.getY(), transform.getZ()), camera.getXRot(), camera.getYRot());
                pos = pos.add(new Vec3f((float) localMovement.z, (float) localMovement.y, (float) localMovement.x));
            }
            if (!previousPos.equals(pos)) {
                ((CameraAccessor) camera).callSetPosition(pos.getX(), pos.getY(), pos.getZ());
            }
        }
    }
}
