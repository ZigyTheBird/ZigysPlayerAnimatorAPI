package com.zigythebird.playeranimatorapi.utils;


import com.zigythebird.playeranimatorapi.modifier.AbstractCameraModifier;
import com.zigythebird.playeranimatorapi.playeranims.CustomModifierLayer;
import com.zigythebird.playeranimatorapi.playeranims.PlayerAnimations;
import dev.kosmx.playerAnim.api.TransformType;
import dev.kosmx.playerAnim.core.util.Vec3f;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CameraUtils {

    public static @Nullable Vec3f computeCameraAngles(Camera camera, double partialTicks) {
        CustomModifierLayer layer = PlayerAnimations.getModifierLayer(Minecraft.getInstance().player);
        if (layer != null && layer.isActive() && layer.cameraAnimEnabled) {
            Vec3f rot = new Vec3f(camera.getXRot(), camera.getYRot(), 0).scale(0.017453292F);
            for (Object object : layer.cameraModifiers) {
                AbstractCameraModifier modifier = (AbstractCameraModifier) object;
                rot = modifier.get3DCameraTransform(camera, TransformType.ROTATION,(float) partialTicks, rot);
            }
            return rot.scale(1/0.017453292F);
        }
        return null;
    }

    public static Vec3f computeCameraLocation(@NotNull Camera camera, double partialTicks) {
        CustomModifierLayer layer = PlayerAnimations.getModifierLayer(Minecraft.getInstance().player);
        if (layer != null && layer.isActive() && layer.cameraAnimEnabled) {
            Vec3f transform = Vec3f.ZERO;
            for (Object object : layer.cameraModifiers) {
                AbstractCameraModifier modifier = (AbstractCameraModifier) object;
                transform = modifier.get3DCameraTransform(camera, TransformType.POSITION, (float) partialTicks, transform).scale((float) 1 /16);
            }
            return transform;
        }
        return null;
    }
}
