package zigy.playeranimatorapi.events;

import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import zigy.playeranimatorapi.PlayerAnimatorAPIMod;
import zigy.playeranimatorapi.playeranims.CustomModifierLayer;
import zigy.playeranimatorapi.playeranims.PlayerAnimations;

@Environment(EnvType.CLIENT)
public class ClientPlayerTickEvent {

    private static final ResourceLocation animationLayerId = new ResourceLocation(PlayerAnimatorAPIMod.MOD_ID, "factory");

    public static void tick(Player player) {
        if (player.level() != null && player.level().isClientSide()) {
            CustomModifierLayer animationContainer = (CustomModifierLayer) PlayerAnimationAccess.getPlayerAssociatedData((AbstractClientPlayer) player).get(animationLayerId);

            boolean isActive = animationContainer.isActive();

            if (isActive) {
                ResourceLocation currentAnim = animationContainer.currentAnim;
                ResourceLocation normalAnim = animationContainer.data.normalAnimationID();
                ResourceLocation crouchedAnim = animationContainer.data.crouchedAnimationID();
                ResourceLocation swimmingAnim = animationContainer.data.swimmingAnimationID();

                if (currentAnim != null && animationContainer.tick > 1 / animationContainer.speedModifier) {
                    if (player.isCrouching() && currentAnim == normalAnim) {
                        PlayerAnimations.playAnimation((AbstractClientPlayer) player, animationContainer.data, false);
                    } else if ((player.isVisuallyCrawling() || player.isVisuallySwimming()) && (currentAnim == crouchedAnim || currentAnim == normalAnim)) {
                        PlayerAnimations.playAnimation((AbstractClientPlayer) player, animationContainer.data, false);
                    } else if (!player.isCrouching() && !player.isVisuallyCrawling() && !player.isVisuallyCrawling() && (currentAnim == crouchedAnim || currentAnim == swimmingAnim)) {
                        PlayerAnimations.playAnimation((AbstractClientPlayer) player, animationContainer.data, false);
                    }
                }
            }
        }
    }
}
