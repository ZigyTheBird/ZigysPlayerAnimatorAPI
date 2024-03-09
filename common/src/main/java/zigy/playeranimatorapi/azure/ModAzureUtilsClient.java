package zigy.playeranimatorapi.azure;

import mod.azure.azurelib.core.animation.AnimatableManager;
import mod.azure.azurelib.core.animation.Animation;
import mod.azure.azurelib.core.animation.AnimationController;
import mod.azure.azurelib.core.animation.RawAnimation;
import net.minecraft.client.player.AbstractClientPlayer;
import zigy.playeranimatorapi.ModInit;
import zigy.playeranimatorapi.data.PlayerAnimationData;
import zigy.playeranimatorapi.playeranims.ConditionalAnimations;

public class ModAzureUtilsClient {

    public static void playGeckoAnimation(AbstractClientPlayer player, PlayerAnimationData data, float speed) {
        AnimatableManager<AbstractClientPlayer> manager = player.getAnimatableInstanceCache().getManagerForId(player.getId());
        AnimationController<AbstractClientPlayer> controller = manager.getAnimationControllers().get(ModInit.MOD_ID);
        controller.triggerableAnim(ConditionalAnimations.getAnimationForCurrentConditions(data).getPath(), RawAnimation.begin().then(ConditionalAnimations.getAnimationForCurrentConditions(data).getPath(), Animation.LoopType.DEFAULT));
        controller.setAnimationSpeed(speed);
        controller.tryTriggerAnimation(ConditionalAnimations.getAnimationForCurrentConditions(data).getPath());
    }

    public static void stopGeckoAnimation(AbstractClientPlayer player) {
        AnimatableManager<AbstractClientPlayer> manager = player.getAnimatableInstanceCache().getManagerForId(player.getId());
        AnimationController<AbstractClientPlayer> controller = manager.getAnimationControllers().get(ModInit.MOD_ID);
        controller.stop();
    }
}
