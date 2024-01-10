package zigy.playeranimatorapi.events;

import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import zigy.playeranimatorapi.PlayerAnimatorAPIMod;
import zigy.playeranimatorapi.data.PlayerAnimationData;
import zigy.playeranimatorapi.playeranims.CustomModifierLayer;
import zigy.playeranimatorapi.playeranims.PlayerAnimations;

import java.util.Map;

@Environment(EnvType.CLIENT)
public class ClientPlayerTickEvent {

    private static final ResourceLocation animationLayerId = new ResourceLocation(PlayerAnimatorAPIMod.MOD_ID, "factory");

    public static void tick(Player player) {
        if (player.level() != null && player.level().isClientSide()) {
            CustomModifierLayer animationContainer = (CustomModifierLayer) PlayerAnimationAccess.getPlayerAssociatedData((AbstractClientPlayer) player).get(animationLayerId);

            boolean isActive = animationContainer.isActive();

            if (isActive) {
                PlayerAnimationData data = animationContainer.data;

                ResourceLocation currentAnim = animationContainer.currentAnim;
                ResourceLocation normalAnim = data.animationID();
                ResourceLocation crouchedAnim = data.animationID().withPath(data.animationID().getPath() + "_crouch");
                ResourceLocation crawlingAnim = data.animationID().withPath(data.animationID().getPath() + "_crawl");
                ResourceLocation swimmingAnim = data.animationID().withPath(data.animationID().getPath() + "_swim");

                Map<ResourceLocation, KeyframeAnimation> animations = PlayerAnimationRegistry.getAnimations();

                if (currentAnim != null && animationContainer.tick > 1 / animationContainer.speedModifier) {
                    if (player.isCrouching() && currentAnim != crawlingAnim && animations.containsKey(crouchedAnim)) {
                        PlayerAnimations.playAnimation((AbstractClientPlayer) player, animationContainer.data, false);
                    } else if (player.isVisuallyCrawling() && currentAnim != crawlingAnim && animations.containsKey(crawlingAnim)) {
                        PlayerAnimations.playAnimation((AbstractClientPlayer) player, animationContainer.data, false);
                    } else if (player.isVisuallySwimming() && currentAnim != swimmingAnim && animations.containsKey(swimmingAnim)) {
                        PlayerAnimations.playAnimation((AbstractClientPlayer) player, animationContainer.data, false);
                    } else if (currentAnim != normalAnim) {
                        PlayerAnimations.playAnimation((AbstractClientPlayer) player, animationContainer.data, false);
                    }
                }
            }
        }
    }
}
