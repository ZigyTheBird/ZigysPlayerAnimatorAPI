package com.zigythebird.playeranimatorapi.API;

import com.zigythebird.multiloaderutils.misc.ModLoader;
import com.zigythebird.multiloaderutils.utils.Platform;
import com.zigythebird.playeranimatorapi.compatibility.FlashbackCompat;
import com.zigythebird.playeranimatorapi.compatibility.ReplayModCompat;
import com.zigythebird.playeranimatorapi.data.PlayerAnimationData;
import com.zigythebird.playeranimatorapi.data.PlayerParts;
import com.zigythebird.playeranimatorapi.example.FirstPersonExample;
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

    public static void playPlayerAnim(AbstractClientPlayer player, ResourceLocation animationID) {
        playPlayerAnim(player, animationID, PlayerParts.allEnabled, null, 1000);
    }

    public static void playPlayerAnim(AbstractClientPlayer player, ResourceLocation animationID, PlayerParts parts, List<CommonModifier> modifiers, int priority) {
        playPlayerAnim(player, animationID, parts, modifiers, -1, -1, priority, 0);
    }

    public static void playPlayerAnim(AbstractClientPlayer player, PlayerAnimationData data) {
        boolean isFabric = Platform.getLoader().equals(ModLoader.Fabric);

        if ((isFabric && Platform.isModLoaded("replaymod")) ||
                Platform.isModLoaded("reforgedplaymod")) {
            ReplayModCompat.playPlayerAnim(player, data.animationID(), data.parts(), data.modifiers(), data.fadeLength(), data.easeID(), data.priority(), data.startTick());
        }

        if (isFabric && Platform.isModLoaded("flashback"))
            FlashbackCompat.playPlayerAnim(player, data.animationID(), data.parts(), data.modifiers(), data.fadeLength(), data.easeID(), data.priority(), data.startTick());

        PlayerAnimations.playAnimation(player, data);
    }

    public static void playPlayerAnim(AbstractClientPlayer player, ResourceLocation animationID, PlayerParts parts, List<CommonModifier> modifiers,
                                      int fadeLength, int easeID, int priority, boolean firstPersonEnabled, boolean replaceTick) {
        if (firstPersonEnabled) {
            modifiers.add(FirstPersonExample.FIRST_PERSON_MODIFIER);
        }

        int startTick = replaceTick ? 0 : -1;

        playPlayerAnim(player, animationID, parts, modifiers, fadeLength, easeID, priority, startTick);
    }

    public static void playPlayerAnim(AbstractClientPlayer player, ResourceLocation animationID, PlayerParts parts, List<CommonModifier> modifiers,
                                      int fadeLength, int easeID, int priority, int startTick) {
        boolean isFabric = Platform.getLoader().equals(ModLoader.Fabric);

        if ((isFabric && Platform.isModLoaded("replaymod")) ||
                Platform.isModLoaded("reforgedplaymod")) {
            ReplayModCompat.playPlayerAnim(player, animationID, parts, modifiers, fadeLength, easeID, priority, startTick);
        }

        if (isFabric && Platform.isModLoaded("flashback"))
            FlashbackCompat.playPlayerAnim(player, animationID, parts, modifiers, fadeLength, easeID, priority, startTick);

        PlayerAnimations.playAnimation(player, new PlayerAnimationData(player.getUUID(), animationID, parts, modifiers,
                fadeLength, easeID, priority, 0));
    }

    public static void stopPlayerAnim(AbstractClientPlayer player, ResourceLocation animationID) {
        if (Platform.isModLoaded("replaymod") && Platform.getLoader().equals(ModLoader.Fabric)) {
            ReplayModCompat.stopPlayerAnim(player, animationID);
        }
        PlayerAnimations.stopAnimation(player, animationID);
    }
}
