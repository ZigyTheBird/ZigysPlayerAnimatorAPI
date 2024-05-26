package zigy.playeranimatorapi.mixin;

import mod.azure.azurelib.common.internal.common.core.animatable.GeoAnimatable;
import mod.azure.azurelib.common.internal.common.core.animation.AnimationController;
import mod.azure.azurelib.common.internal.common.core.animation.EasingType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.function.Function;

@Mixin(AnimationController.class)
public interface AnimationControllerAccessor_azureOnly<T extends GeoAnimatable> {

    @Accessor(remap = false)
    Function<T, EasingType> getOverrideEasingTypeFunction();

    @Accessor(remap = false)
    void setIsJustStarting(boolean isJustStarting);
}
