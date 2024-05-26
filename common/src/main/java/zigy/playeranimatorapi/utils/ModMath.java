package zigy.playeranimatorapi.utils;

import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class ModMath {
    public static Vec3 moveInLocalSpace(Vec3 input, float xRot, float yRot) {
        Vec3 result = Vec3.directionFromRotation(xRot, yRot).scale(input.x);
        result = result.add(Vec3.directionFromRotation(Mth.wrapDegrees(xRot - 90), yRot).scale(input.y));
        result = result.add(Vec3.directionFromRotation(xRot, Mth.wrapDegrees(yRot - 90)).scale(input.z));
        return result;
    }
}
