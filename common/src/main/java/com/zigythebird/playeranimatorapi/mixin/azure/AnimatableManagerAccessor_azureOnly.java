package com.zigythebird.playeranimatorapi.mixin.azure;

import mod.azure.azurelib.core.animation.AnimatableManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(AnimatableManager.class)
public interface AnimatableManagerAccessor_azureOnly {
    @Invoker(remap = false)
    void callFinishFirstTick();
}
