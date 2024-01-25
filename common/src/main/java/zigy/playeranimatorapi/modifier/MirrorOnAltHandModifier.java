package zigy.playeranimatorapi.modifier;

import dev.kosmx.playerAnim.api.layered.modifier.MirrorModifier;
import net.minecraft.client.Minecraft;
import zigy.playeranimatorapi.playeranims.CustomModifierLayer;

public class MirrorOnAltHandModifier extends MirrorModifier {

    CustomModifierLayer layer;

    public MirrorOnAltHandModifier(CustomModifierLayer layer) {
        super();
    }

    public boolean isEnabled() {
        return layer.player == Minecraft.getInstance().player && Minecraft.getInstance().options.mainHand().get().getId() == 0;
    }
}