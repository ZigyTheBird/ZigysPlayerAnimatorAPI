package zigy.playeranimatorapi.events;

import mod.azure.azurelib.core.animation.AnimatableManager;
import mod.azure.azurelib.core.animation.AnimationController;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import zigy.playeranimatorapi.ModInit;
import zigy.playeranimatorapi.azure.ModAzureUtilsClient;
import zigy.playeranimatorapi.data.PlayerAnimationData;
import zigy.playeranimatorapi.playeranims.ConditionalAnimations;
import zigy.playeranimatorapi.playeranims.CustomModifierLayer;
import zigy.playeranimatorapi.playeranims.PlayerAnimations;

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

                AnimatableManager<AbstractClientPlayer> manager = player.getAnimatableInstanceCache().getManagerForId(player.getId());
                AnimationController<AbstractClientPlayer> controller = manager.getAnimationControllers().get(ModInit.MOD_ID);

                if (!controller.isPlayingTriggeredAnimation()) {
                    ModAzureUtilsClient.playGeckoAnimation((AbstractClientPlayer) player, animationContainer.data, animationContainer.getSpeed());
                }
            }
        }
    }
}
