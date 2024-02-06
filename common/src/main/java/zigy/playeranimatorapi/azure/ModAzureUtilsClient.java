package zigy.playeranimatorapi.azure;

import mod.azure.azurelib.core.animation.*;
import net.minecraft.client.player.AbstractClientPlayer;
import zigy.playeranimatorapi.PlayerAnimatorAPIMod;
import zigy.playeranimatorapi.data.PlayerAnimationData;
import zigy.playeranimatorapi.playeranims.ConditionalAnimations;

public class ModAzureUtilsClient {

    public static void playGeckoAnimation(AbstractClientPlayer player, PlayerAnimationData data) {
        AnimatableManager<AbstractClientPlayer> manager = player.getAnimatableInstanceCache().getManagerForId(player.getId());
        AnimationController<AbstractClientPlayer> controller = manager.getAnimationControllers().get(PlayerAnimatorAPIMod.MOD_ID);
        controller.triggerableAnim(data.animationID().getPath(), RawAnimation.begin().then(data.animationID().getPath(), Animation.LoopType.DEFAULT));
        controller.tryTriggerAnimation(ConditionalAnimations.getAnimationForCurrentConditions(data).getPath());
    }

    public static void stopGeckoAnimation(AbstractClientPlayer player) {
        AnimatableManager<AbstractClientPlayer> manager = player.getAnimatableInstanceCache().getManagerForId(player.getId());
        AnimationController<AbstractClientPlayer> controller = manager.getAnimationControllers().get(PlayerAnimatorAPIMod.MOD_ID);
        controller.stop();
    }
}
