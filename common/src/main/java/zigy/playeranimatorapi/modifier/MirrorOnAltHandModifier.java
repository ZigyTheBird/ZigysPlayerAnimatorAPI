package zigy.playeranimatorapi.modifier;

import dev.kosmx.playerAnim.api.layered.modifier.MirrorModifier;
import net.minecraft.client.Minecraft;
import zigy.playeranimatorapi.playeranims.CustomModifierLayer;

/**Apply the mirror modifier only if the client has their main arm set as the left one*/
public class MirrorOnAltHandModifier extends MirrorModifier {

    CustomModifierLayer layer;

    public MirrorOnAltHandModifier(CustomModifierLayer layer) {
        super();
        this.layer = layer;
    }

    public boolean isEnabled() {
        return layer.player == Minecraft.getInstance().player && Minecraft.getInstance().options.mainHand().get().getId() == 0;
    }
}