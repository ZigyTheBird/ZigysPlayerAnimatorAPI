package com.zigythebird.playeranimatorapi.modifier;

import com.zigythebird.playeranimatorapi.playeranims.CustomModifierLayer;
import com.zigythebird.playeranimatorapi.playeranims.PlayerAnimations;
import dev.kosmx.playerAnim.api.layered.modifier.SpeedModifier;

/**Set the length of the anim to the desired Length*/
public class LengthModifier extends SpeedModifier {

    public LengthModifier(CustomModifierLayer layer, float desiredLength) {
        super(1);
        if (desiredLength > 0) {
            this.speed = PlayerAnimations.animLengthsMap.get(layer.data.animationID()) / desiredLength;
        }
    }
}
