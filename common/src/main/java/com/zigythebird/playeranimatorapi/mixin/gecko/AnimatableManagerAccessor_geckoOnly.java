package com.zigythebird.playeranimatorapi.mixin.gecko;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import software.bernie.geckolib.animation.AnimatableManager;

@Mixin(AnimatableManager.class)
public interface AnimatableManagerAccessor_geckoOnly {
    @Invoker(remap = false)
    void callFinishFirstTick();
}
