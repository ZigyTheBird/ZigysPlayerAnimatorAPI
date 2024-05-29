package com.zigythebird.playeranimatorapi.modifier;

import com.zigythebird.playeranimatorapi.playeranims.CustomModifierLayer;
import dev.kosmx.playerAnim.api.layered.modifier.MirrorModifier;
import net.minecraft.client.Minecraft;

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