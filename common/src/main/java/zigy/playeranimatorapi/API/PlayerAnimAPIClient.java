package zigy.playeranimatorapi.API;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import zigy.playeranimatorapi.data.PlayerAnimationData;
import zigy.playeranimatorapi.data.PlayerParts;
import zigy.playeranimatorapi.modifier.CommonModifier;
import zigy.playeranimatorapi.playeranims.PlayerAnimations;

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
                -1, -1, false, false, true);
    }

    /**For gameplay like player animations for items.*/
    public static void playPlayerAnim(AbstractClientPlayer player, ResourceLocation animationID, PlayerParts parts, List<CommonModifier> modifiers, boolean important) {
        playPlayerAnim(player, animationID, parts, modifiers, -1, -1, false, important, true);
    }

    /**Play player animations with the PlayerAnimationData class.*/
    public static void playPlayerAnim(AbstractClientPlayer player, PlayerAnimationData data) {
        PlayerAnimations.playAnimation(player, data);
    }

    /**Play player animations with full customizability.*/
    public static void playPlayerAnim(AbstractClientPlayer player, ResourceLocation animationID, PlayerParts parts, List<CommonModifier> modifiers,
                                      int fadeLength, int easeID, boolean firstPersonEnabled, boolean important, boolean replaceTick) {

        PlayerAnimations.playAnimation(player, new PlayerAnimationData(player.getUUID(), animationID, parts, modifiers,
                fadeLength, easeID, firstPersonEnabled, important), replaceTick);
    }

    /**Stop a player animation*/
    public static void stopPlayerAnim(AbstractClientPlayer player, ResourceLocation animationID) {
        PlayerAnimations.stopAnimation(player, animationID);
    }
}
