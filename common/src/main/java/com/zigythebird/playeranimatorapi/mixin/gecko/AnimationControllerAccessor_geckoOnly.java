package com.zigythebird.playeranimatorapi.mixin.gecko;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.EasingType;

import java.util.function.Function;

@Mixin(AnimationController.class)
public interface AnimationControllerAccessor_geckoOnly<T extends GeoAnimatable> {

    @Accessor(remap = false)
    Function<T, EasingType> getOverrideEasingTypeFunction();

    @Accessor(remap = false)
    void setIsJustStarting(boolean isJustStarting);
}
