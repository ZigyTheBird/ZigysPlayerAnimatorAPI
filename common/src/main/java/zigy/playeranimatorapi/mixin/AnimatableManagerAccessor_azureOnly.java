package zigy.playeranimatorapi.mixin;

import mod.azure.azurelib.common.internal.common.core.animation.AnimatableManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(AnimatableManager.class)
public interface AnimatableManagerAccessor_azureOnly {
    @Invoker(remap = false)
    void callFinishFirstTick();
}
