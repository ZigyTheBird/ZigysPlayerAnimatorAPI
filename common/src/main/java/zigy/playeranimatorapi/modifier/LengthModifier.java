package zigy.playeranimatorapi.modifier;

import dev.kosmx.playerAnim.api.layered.modifier.SpeedModifier;
import zigy.playeranimatorapi.playeranims.CustomModifierLayer;
import zigy.playeranimatorapi.playeranims.PlayerAnimations;

/**Set the length of the anim to the desired Length*/
public class LengthModifier extends SpeedModifier {

    public LengthModifier(CustomModifierLayer layer, float desiredLength) {
        super(1);
        if (desiredLength > 0) {
            this.speed = PlayerAnimations.animLengthsMap.get(layer.data.animationID()) / desiredLength;
        }
    }
}
