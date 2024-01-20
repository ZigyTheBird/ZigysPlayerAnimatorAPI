package zigy.playeranimatorapi.playeranims;

import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import zigy.playeranimatorapi.data.PlayerAnimationData;

import java.util.Map;

import static zigy.playeranimatorapi.playeranims.PlayerAnimations.animationLayerId;

@Environment(EnvType.CLIENT)
public class ConditionalAnimations {

    public static ResourceLocation getAnimationForCurrentConditions(PlayerAnimationData data) {

        AbstractClientPlayer player = (AbstractClientPlayer) Minecraft.getInstance().level.getPlayerByUUID(data.playerUUID());
        CustomModifierLayer animationContainer = (CustomModifierLayer) PlayerAnimationAccess.getPlayerAssociatedData(player).get(animationLayerId);

        ResourceLocation currentAnim = animationContainer.currentAnim;
        ResourceLocation baseAnim = data.animationID();
        ResourceLocation walkingAnim = data.animationID().withPath(data.animationID().getPath() + "_run");
        ResourceLocation runningAnim = data.animationID().withPath(data.animationID().getPath() + "_run");
        ResourceLocation crouchedAnim = data.animationID().withPath(data.animationID().getPath() + "_crouch");
        ResourceLocation crawlingAnim = data.animationID().withPath(data.animationID().getPath() + "_crawl");
        ResourceLocation swimmingAnim = data.animationID().withPath(data.animationID().getPath() + "_swim");

        Map<ResourceLocation, KeyframeAnimation> animations = PlayerAnimationRegistry.getAnimations();

        if (player.isCrouching() && currentAnim != crawlingAnim && animations.containsKey(crouchedAnim)) {
            return crouchedAnim;
        } else if (player.isVisuallyCrawling() && currentAnim != crawlingAnim && animations.containsKey(crawlingAnim)) {
            return crawlingAnim;
        } else if (player.isVisuallySwimming() && currentAnim != swimmingAnim && animations.containsKey(swimmingAnim)) {
            return swimmingAnim;
        } else if (player.isSprinting() && currentAnim != runningAnim && animations.containsKey(runningAnim)) {
            return runningAnim;
        }

        return baseAnim;
    }
}
