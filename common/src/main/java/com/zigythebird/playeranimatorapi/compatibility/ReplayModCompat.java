package com.zigythebird.playeranimatorapi.compatibility;

import com.zigythebird.playeranimatorapi.data.PlayerParts;
import com.zigythebird.playeranimatorapi.modifier.CommonModifier;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.NotImplementedException;

import java.util.List;

public class ReplayModCompat {

    @ExpectPlatform
    public static void playPlayerAnim(AbstractClientPlayer player, ResourceLocation animationID, PlayerParts parts, List<CommonModifier> modifiers,
                                      int fadeLength, int easeID, int priority, boolean firstPersonEnabled, boolean replaceTick) {
        throw new NotImplementedException();
    }

    @ExpectPlatform
    public static void stopPlayerAnim(AbstractClientPlayer player, ResourceLocation animationID) {
        throw new NotImplementedException();
    }
}
