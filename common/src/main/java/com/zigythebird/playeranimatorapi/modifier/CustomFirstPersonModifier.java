package com.zigythebird.playeranimatorapi.modifier;

import dev.kosmx.playerAnim.api.firstPerson.FirstPersonConfiguration;
import dev.kosmx.playerAnim.api.firstPerson.FirstPersonMode;
import dev.kosmx.playerAnim.api.layered.modifier.FirstPersonModifier;
import org.jetbrains.annotations.NotNull;

/**
 * firstPersonMode defaults to NONE.
 * firstPersonConfig defaults to only items.
 */
public class CustomFirstPersonModifier extends FirstPersonModifier {
    private final FirstPersonMode firstPersonMode;
    private final FirstPersonConfiguration firstPersonConfig;

    public CustomFirstPersonModifier(FirstPersonMode firstPersonMode, FirstPersonConfiguration firstPersonConfig) {
        this.firstPersonConfig = firstPersonConfig;
        this.firstPersonMode = firstPersonMode;
    }

    @Override
    public @NotNull FirstPersonConfiguration getFirstPersonConfiguration(float tickDelta) {
        return firstPersonConfig;
    }

    @Override
    public @NotNull FirstPersonMode getFirstPersonMode(float tickDelta) {
        return firstPersonMode;
    }
}
