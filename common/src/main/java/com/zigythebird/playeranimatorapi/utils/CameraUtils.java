package com.zigythebird.playeranimatorapi.utils;

import com.zigythebird.playeranimatorapi.data.PlayerPart;
import com.zigythebird.playeranimatorapi.mixin.CameraAccessor;
import com.zigythebird.playeranimatorapi.modifier.AbstractCameraModifier;
import com.zigythebird.playeranimatorapi.playeranims.CustomModifierLayer;
import com.zigythebird.playeranimatorapi.playeranims.PlayerAnimations;
import dev.kosmx.playerAnim.api.TransformType;
import dev.kosmx.playerAnim.core.util.Vec3f;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;

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
            for (Object object : layer.cameraModifiers) {
                AbstractCameraModifier modifier = (AbstractCameraModifier) object;
                Vec3f transform = modifier.get3DCameraTransform(renderer, camera, TransformType.POSITION, (float) partialTicks, Vec3f.ZERO).scale((float) 1 /16);
                Quaternionf rotation = new Quaternionf(0.0F, 0.0F, 0.0F, 1.0F);
                Vec3f angles = computeCameraAngles(renderer, camera, partialTicks);
                if (angles == null) {
                    return;
                }
                PlayerPart head = layer.data.parts().head;
                if (!head.yaw || !head.pitch) {
                    float xRot = head.yaw ? angles.getX() : camera.getXRot();
                    float yRot = head.pitch ? angles.getY() : camera.getYRot();
                    angles = new Vec3f(xRot, yRot, angles.getZ());
                }
                rotation.rotationYXZ(-angles.getY() * 0.017453292F, angles.getX() * 0.017453292F, angles.getZ() * 0.017453292F);
                Vector3f forwards = new Vector3f(0.0F, 0.0F, 1.0F);
                Vector3f up = new Vector3f(0.0F, 1.0F, 0.0F);
                Vector3f left = new Vector3f(1.0F, 0.0F, 0.0F);
                forwards.set(0.0F, 0.0F, 1.0F).rotate(rotation);
                up.set(0.0F, 1.0F, 0.0F).rotate(rotation);
                left.set(1.0F, 0.0F, 0.0F).rotate(rotation);
                double d = (double)forwards.x() * transform.getX() + (double)up.x() * transform.getY() + (double)left.x() * transform.getZ();
                double e = (double)forwards.y() * transform.getX() + (double)up.y() * transform.getY() + (double)left.y() * transform.getZ();
                double f = (double)forwards.z() * transform.getX() + (double)up.z() * transform.getY() + (double)left.z() * transform.getZ();
                ((CameraAccessor)camera).callSetPosition(camera.getPosition().z + f, camera.getPosition().y - e, camera.getPosition().x - d);
            }
        }
    }
}
