package com.zigythebird.playeranimatorapi.events;

import com.zigythebird.multiloaderutils.utils.Platform;
import com.zigythebird.playeranimatorapi.data.PlayerAnimationData;
import com.zigythebird.playeranimatorapi.gecko.ModGeckoUtilsClient;
import com.zigythebird.playeranimatorapi.playeranims.ConditionalAnimations;
import com.zigythebird.playeranimatorapi.playeranims.CustomModifierLayer;
import com.zigythebird.playeranimatorapi.playeranims.PlayerAnimations;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class ClientPlayerTickEvent {

    public static void tick(Player player) {
        if (player.level().isClientSide()) {
            CustomModifierLayer animationContainer = PlayerAnimations.getModifierLayer((AbstractClientPlayer) player);

            if (animationContainer != null && animationContainer.isActive()) {
                PlayerAnimationData data = animationContainer.data;

                ResourceLocation currentAnim = animationContainer.currentAnim;

                if (currentAnim != null && !ConditionalAnimations.getAnimationForCurrentConditions(data).equals(currentAnim)) {
                    PlayerAnimations.playAnimation(player, data, -1);
                }

                if (Platform.isModLoaded("geckolib")) {
                    ModGeckoUtilsClient.tick((AbstractClientPlayer) player, animationContainer);
                }
            }
        }
    }
}
