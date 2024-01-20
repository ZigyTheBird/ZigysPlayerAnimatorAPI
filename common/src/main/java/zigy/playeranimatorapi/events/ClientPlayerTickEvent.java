package zigy.playeranimatorapi.events;

import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import zigy.playeranimatorapi.PlayerAnimatorAPIMod;
import zigy.playeranimatorapi.data.PlayerAnimationData;
import zigy.playeranimatorapi.playeranims.ConditionalAnimations;
import zigy.playeranimatorapi.playeranims.CustomModifierLayer;
import zigy.playeranimatorapi.playeranims.PlayerAnimations;

@Environment(EnvType.CLIENT)
public class ClientPlayerTickEvent {

    public static ResourceLocation playerAnimation = new ResourceLocation(PlayerAnimatorAPIMod.MOD_ID, "player");

    public static void tick(Player player) {
        if (player.level().isClientSide()) {
            CustomModifierLayer animationContainer = PlayerAnimations.getModifierLayer((AbstractClientPlayer) player);

            if (animationContainer != null) {
                if (animationContainer.isActive()) {
                    PlayerAnimationData data = animationContainer.data;

                    ResourceLocation currentAnim = animationContainer.currentAnim;

                    if (currentAnim != null && animationContainer.tick > 1 / animationContainer.speedModifier
                            && !ConditionalAnimations.getAnimationForCurrentConditions(data).equals(currentAnim)) {
                        PlayerAnimations.playAnimation((AbstractClientPlayer) player, data, true);
                    }
                } else if (player.equals(Minecraft.getInstance().player) && PlayerAnimationRegistry.getAnimations().containsKey(playerAnimation)) {
//                    PlayerAnimAPIClient.playPlayerAnim((AbstractClientPlayer) player, playerAnimation, PlayerParts.allExceptHeadRot(), 10, -1,
//                            -1, false, false, false, true);
                }
            }
        }
    }
}
