package zigy.playeranimatorapi.mixin;

import mod.azure.azurelib.core.animatable.GeoAnimatable;
import mod.azure.azurelib.core.animation.AnimationController;
import mod.azure.azurelib.core.animation.EasingType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.function.Function;

@Mixin(AnimationController.class)
public interface AnimationControllerAccessor<T extends GeoAnimatable> {

    @Accessor(remap = false)
    Function<T, EasingType> getOverrideEasingTypeFunction();

    @Accessor(remap = false)
    void setIsJustStarting(boolean isJustStarting);
}
