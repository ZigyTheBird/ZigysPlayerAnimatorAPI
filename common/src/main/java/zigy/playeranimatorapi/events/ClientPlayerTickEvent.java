package zigy.playeranimatorapi.events;

import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import zigy.playeranimatorapi.azure.ModAzureUtilsClient;
import zigy.playeranimatorapi.data.PlayerAnimationData;
import zigy.playeranimatorapi.playeranims.ConditionalAnimations;
import zigy.playeranimatorapi.playeranims.CustomModifierLayer;
import zigy.playeranimatorapi.playeranims.PlayerAnimations;
import zigy.zigysmultiloaderutils.utils.Platform;

public class ClientPlayerTickEvent {

    public static void tick(Player player) {
        if (player.level().isClientSide()) {
            CustomModifierLayer animationContainer = PlayerAnimations.getModifierLayer((AbstractClientPlayer) player);

            if (animationContainer != null && animationContainer.isActive()) {
                PlayerAnimationData data = animationContainer.data;

                ResourceLocation currentAnim = animationContainer.currentAnim;

                if (currentAnim != null && !ConditionalAnimations.getAnimationForCurrentConditions(data).equals(currentAnim)) {
                    PlayerAnimations.playAnimation((AbstractClientPlayer) player, data, true);
                }

                if (Platform.isModLoaded("azurelib")) {
                    ModAzureUtilsClient.tick((AbstractClientPlayer) player, animationContainer);
                }
            }
        }
    }
}
