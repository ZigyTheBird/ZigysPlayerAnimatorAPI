package com.zigythebird.playeranimatorapi.mixin;

import dev.kosmx.playerAnim.api.layered.AnimationStack;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.core.util.Pair;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.ArrayList;

@Mixin(AnimationStack.class)
public interface AnimationStackAccessor {
    @Accessor
    ArrayList<Pair<Integer, IAnimation>> getLayers();
}
