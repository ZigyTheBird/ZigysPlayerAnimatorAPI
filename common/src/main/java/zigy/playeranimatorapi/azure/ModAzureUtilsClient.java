package zigy.playeranimatorapi.azure;

import mod.azure.azurelib.core.animation.*;
import mod.azure.azurelib.core.object.PlayState;
import net.minecraft.client.player.AbstractClientPlayer;
import zigy.playeranimatorapi.data.PlayerAnimationData;
import zigy.playeranimatorapi.playeranims.ConditionalAnimations;

import java.util.ArrayList;
import java.util.List;

import static zigy.playeranimatorapi.playeranims.PlayerAnimations.extensions;

public class ModAzureUtilsClient {

    public static List<AnimationController> animationControllers = new ArrayList<>();

    public static void playGeckoAnimation(AbstractClientPlayer player, PlayerAnimationData data) {
        AnimatableManager<AbstractClientPlayer> manager = player.getAnimatableInstanceCache().getManagerForId(player.getId());

        if (!manager.getAnimationControllers().containsKey(data.animationID().getPath())) {
            AnimationController<AbstractClientPlayer> controller = new AnimationController<>(player, data.animationID().getPath(), state -> PlayState.STOP).setOverrideEasingType(EasingType.EASE_OUT_QUAD);

            controller.triggerableAnim(data.animationID().getPath(), RawAnimation.begin().then(data.animationID().getPath(), Animation.LoopType.DEFAULT));

            for (String str : extensions) {
                controller.triggerableAnim(data.animationID().getPath() + "_" + str, RawAnimation.begin());
            }
            manager.addController(controller);
            animationControllers.add(controller);
        }

        for (AnimationController controller2 : animationControllers) {
            controller2.stop();
        }

        manager.getAnimationControllers().get(data.animationID().getPath()).tryTriggerAnimation(ConditionalAnimations.getAnimationForCurrentConditions(data).getPath());
    }

    public static void stopGeckoAnimation(AbstractClientPlayer player) {
        AnimatableManager<AbstractClientPlayer> manager = player.getAnimatableInstanceCache().getManagerForId(player.getId());
        for (AnimationController controller : animationControllers) {
            controller.stop();
        }
    }
}
