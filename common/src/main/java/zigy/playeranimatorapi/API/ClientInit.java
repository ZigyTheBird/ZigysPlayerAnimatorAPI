package zigy.playeranimatorapi.API;

import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import zigy.playeranimatorapi.data.PlayerAnimationData;
import zigy.playeranimatorapi.modifier.CommonModifier;
import zigy.playeranimatorapi.playeranims.ConditionalAnimations;
import zigy.playeranimatorapi.playeranims.CustomModifierLayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static zigy.playeranimatorapi.playeranims.PlayerAnimations.animationLayerId;

public class ClientInit {

    public static final ResourceLocation wave = new ResourceLocation("playeranimatorapi", "wave");
    public static final ResourceLocation MIRROR = new ResourceLocation("player-animator", "mirror");
    public static final List<CommonModifier> MODIFIERS = new ArrayList<>(){{add(new CommonModifier(MIRROR, null));}};

    public static void clientInit() {
        ConditionalAnimations.addModConditions("examplemod", ClientInit::getAnimationForCurrentConditions);
    }

    public static ResourceLocation getAnimationForCurrentConditions(PlayerAnimationData data) {
        AbstractClientPlayer player = (AbstractClientPlayer) Minecraft.getInstance().level.getPlayerByUUID(data.playerUUID());
        CustomModifierLayer animationContainer = (CustomModifierLayer) PlayerAnimationAccess.getPlayerAssociatedData(player).get(animationLayerId);

        ResourceLocation currentAnim = animationContainer.currentAnim;
        ResourceLocation baseAnim = data.animationID();
        ResourceLocation useAnim = data.animationID().withPath(data.animationID().getPath() + "_use");

        Map<ResourceLocation, KeyframeAnimation> animations = PlayerAnimationRegistry.getAnimations();

        if (player.isUsingItem() && currentAnim != useAnim && animations.containsKey(useAnim)) {
            return useAnim;
        }

        return baseAnim;
    }
}
