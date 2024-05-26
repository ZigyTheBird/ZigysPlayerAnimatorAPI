package zigy.playeranimatorapi.mixin;

import net.minecraft.client.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Camera.class)
public interface CameraAccessor {
    @Invoker
    void callSetPosition(double x, double y, double z);

    @Accessor
    float getEyeHeightOld();

    @Accessor
    float getEyeHeight();
}
