package zigy.playeranimatorapi.API;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import zigy.playeranimatorapi.data.PlayerAnimationData;
import zigy.playeranimatorapi.data.PlayerParts;
import zigy.playeranimatorapi.playeranims.PlayerAnimations;

/**
 * Class for playing player animations from client side.
 * Look at the GitHub wiki for information on what the method parameters do.
 */
@Environment(EnvType.CLIENT)
public class PlayerAnimAPIClient {

    /**For emotes.*/
    public static void playPlayerAnim(AbstractClientPlayer player, ResourceLocation animationID) {
        playPlayerAnim(player, animationID, PlayerParts.allEnabled,
                -1, -1, -1, false, false, false, true);
    }

    /**For gameplay like player animations for items.*/
    public static void playPlayerAnim(AbstractClientPlayer player, ResourceLocation animationID, PlayerParts parts, float desiredLength, boolean important) {
        playPlayerAnim(player, animationID, parts, -1,
                desiredLength, -1, important, true, true, true);
    }

    /**Play player animations with the PlayerAnimationData class.*/
    public static void playPlayerAnim(AbstractClientPlayer player, PlayerAnimationData data) {
        PlayerAnimations.playAnimation(player, data);
    }

    /**Play player animations with full customizability.*/
    public static void playPlayerAnim(AbstractClientPlayer player, ResourceLocation animationID, PlayerParts parts, int fadeLength, float desiredLength,
                                      int easeID, boolean firstPersonEnabled, boolean shouldMirror, boolean important, boolean replaceTick) {

        PlayerAnimations.playAnimation(player, new PlayerAnimationData(player.getUUID(), animationID, parts, fadeLength, desiredLength, easeID,
                        firstPersonEnabled, shouldMirror, important), parts, fadeLength, desiredLength,
                easeID, firstPersonEnabled, shouldMirror, replaceTick);
    }

    /**Stop a player animation*/
    public static void stopPlayerAnim(AbstractClientPlayer player, ResourceLocation animationID) {
        PlayerAnimations.stopAnimation(player, animationID);
    }
}
