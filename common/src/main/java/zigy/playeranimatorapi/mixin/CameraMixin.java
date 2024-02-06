package zigy.playeranimatorapi.mixin;

import net.minecraft.client.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import zigy.playeranimatorapi.misc.CameraInterface;

@Mixin(Camera.class)
public class CameraMixin implements CameraInterface {

    @Shadow
    private float yRot;

    @Shadow
    private float xRot;

    @Override
    public void API$setAnglesInternal(float yaw, float pitch) {
        this.yRot = yaw;
        this.xRot = pitch;
    }
}
