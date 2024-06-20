package com.zigythebird.playeranimatorapi.API;

import com.zigythebird.multiloaderutils.misc.ModLoader;
import com.zigythebird.multiloaderutils.utils.Platform;
import com.zigythebird.playeranimatorapi.compatibility.ReplayModCompat;
import com.zigythebird.playeranimatorapi.data.PlayerAnimationData;
import com.zigythebird.playeranimatorapi.data.PlayerParts;
import com.zigythebird.playeranimatorapi.modifier.CommonModifier;
import com.zigythebird.playeranimatorapi.playeranims.PlayerAnimations;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

/**
 * Class for playing player animations from client side.
 * Look at the GitHub wiki for information on what the method parameters do.
 */
@Environment(EnvType.CLIENT)
public class PlayerAnimAPIClient {

    /**For emotes.*/
    public static void playPlayerAnim(AbstractClientPlayer player, ResourceLocation animationID) {
        playPlayerAnim(player, animationID, PlayerParts.allEnabled, null,
                -1, -1, 1000, false, true);
    }

    /**For gameplay like player animations for items.*/
    public static void playPlayerAnim(AbstractClientPlayer player, ResourceLocation animationID, PlayerParts parts, List<CommonModifier> modifiers, int priority) {
        playPlayerAnim(player, animationID, parts, modifiers, -1, -1, priority, false, true);
    }

    /**Play player animations with the PlayerAnimationData class.*/
    public static void playPlayerAnim(AbstractClientPlayer player, PlayerAnimationData data) {
        PlayerAnimations.playAnimation(player, data);
    }

    /**Play player animations with full customizability.*/
    public static void playPlayerAnim(AbstractClientPlayer player, ResourceLocation animationID, PlayerParts parts, List<CommonModifier> modifiers,
                                      int fadeLength, int easeID, int priority, boolean firstPersonEnabled, boolean replaceTick) {

        if (Platform.isModLoaded("replaymod") && Platform.getLoader().equals(ModLoader.Fabric)) {
            ReplayModCompat.playPlayerAnim(player, animationID, parts, modifiers, fadeLength, easeID, priority, firstPersonEnabled, replaceTick);
        }

        PlayerAnimations.playAnimation(player, new PlayerAnimationData(player.getUUID(), animationID, parts, modifiers,
                fadeLength, easeID, priority, firstPersonEnabled), replaceTick);
    }

    /**Stop a player animation*/
    public static void stopPlayerAnim(AbstractClientPlayer player, ResourceLocation animationID) {
        if (Platform.isModLoaded("replaymod") && Platform.getLoader().equals(ModLoader.Fabric)) {
            ReplayModCompat.stopPlayerAnim(player, animationID);
        }
        PlayerAnimations.stopAnimation(player, animationID);
    }
}
