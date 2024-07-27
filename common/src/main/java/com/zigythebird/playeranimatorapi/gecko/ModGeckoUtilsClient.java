package com.zigythebird.playeranimatorapi.gecko;

import com.zigythebird.playeranimatorapi.ModInit;
import com.zigythebird.playeranimatorapi.data.PlayerAnimationData;
import com.zigythebird.playeranimatorapi.misc.GeckoPlayerInterface;
import com.zigythebird.playeranimatorapi.playeranims.ConditionalAnimations;
import com.zigythebird.playeranimatorapi.playeranims.CustomModifierLayer;
import com.zigythebird.playeranimatorapi.playeranims.PlayerAnimations;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.player.Player;
import software.bernie.geckolib.animation.*;

public class ModGeckoUtilsClient {

    public static void playGeckoAnimation(AbstractClientPlayer player, PlayerAnimationData data, float speed) {
        AnimatableManager<AnimatablePlayerLayer> manager = ((GeckoPlayerInterface)player).playeranimatorapi$getAnimatablePlayerLayer().getAnimatableInstanceCache().getManagerForId(player.getId());
        AnimationController<AnimatablePlayerLayer> controller = manager.getAnimationControllers().get(ModInit.MOD_ID);
        controller.triggerableAnim(ConditionalAnimations.getAnimationForCurrentConditions(data).getPath(), RawAnimation.begin().then(ConditionalAnimations.getAnimationForCurrentConditions(data).getPath(), Animation.LoopType.DEFAULT));
        controller.setAnimationSpeed(speed);

        controller.transitionLength(data.fadeLength() > -1 ? data.fadeLength() : 0);
        controller.tryTriggerAnimation(ConditionalAnimations.getAnimationForCurrentConditions(data).getPath());
    }

    public static void stopGeckoAnimation(AbstractClientPlayer player) {
        AnimatableManager<AnimatablePlayerLayer> manager = ((GeckoPlayerInterface)player).playeranimatorapi$getAnimatablePlayerLayer().getAnimatableInstanceCache().getManagerForId(player.getId());
        AnimationController<AnimatablePlayerLayer> controller = manager.getAnimationControllers().get(ModInit.MOD_ID);
        controller.stop();
    }

    public static void tick(AbstractClientPlayer player, CustomModifierLayer animationContainer) {
        AnimatableManager<AnimatablePlayerLayer> manager = ((GeckoPlayerInterface)player).playeranimatorapi$getAnimatablePlayerLayer().getAnimatableInstanceCache().getManagerForId(player.getId());
        AnimationController<AnimatablePlayerLayer> controller = manager.getAnimationControllers().get(ModInit.MOD_ID);

        if (!controller.isPlayingTriggeredAnimation()) {
            ModGeckoUtilsClient.playGeckoAnimation(player, animationContainer.data, animationContainer.getSpeed());
        }
    }

    public static EasingType getEasingTypeForID(Player player) {
        CustomModifierLayer animationContainer = PlayerAnimations.getModifierLayer((AbstractClientPlayer) player);
        switch (animationContainer.data.easeID()) {
            case 0 -> {
                return EasingType.LINEAR;
            }
            case 6 -> {
                return EasingType.EASE_IN_SINE;
            }
            case 7 -> {
                return EasingType.EASE_OUT_SINE;
            }
            case 8 -> {
                return EasingType.EASE_IN_OUT_SINE;
            }
            case 9 -> {
                return EasingType.EASE_IN_CUBIC;
            }
            case 10 -> {
                return EasingType.EASE_OUT_CUBIC;
            }
            case 11 -> {
                return EasingType.EASE_IN_OUT_CUBIC;
            }
            case 12 -> {
                return EasingType.EASE_IN_QUAD;
            }
            case 13 -> {
                return EasingType.EASE_OUT_QUAD;
            }
            case 14 -> {
                return EasingType.EASE_IN_OUT_QUAD;
            }
            case 15 -> {
                return EasingType.EASE_IN_QUART;
            }
            case 16 -> {
                return EasingType.EASE_OUT_QUART;
            }
            case 17 -> {
                return EasingType.EASE_IN_OUT_QUART;
            }
            case 18 -> {
                return EasingType.EASE_IN_QUINT;
            }
            case 19 -> {
                return EasingType.EASE_OUT_QUINT;
            }
            case 20 -> {
                return EasingType.EASE_IN_OUT_QUINT;
            }
            case 21 -> {
                return EasingType.EASE_IN_EXPO;
            }
            case 22 -> {
                return EasingType.EASE_OUT_EXPO;
            }
            case 23 -> {
                return EasingType.EASE_IN_OUT_EXPO;
            }
            case 24 -> {
                return EasingType.EASE_IN_CIRC;
            }
            case 25 -> {
                return EasingType.EASE_OUT_CIRC;
            }
            case 26 -> {
                return EasingType.EASE_IN_OUT_CIRC;
            }
            case 27 -> {
                return EasingType.EASE_IN_BACK;
            }
            case 28 -> {
                return EasingType.EASE_OUT_BACK;
            }
            case 29 -> {
                return EasingType.EASE_IN_OUT_BACK;
            }
            case 30 -> {
                return EasingType.EASE_IN_ELASTIC;
            }
            case 31 -> {
                return EasingType.EASE_OUT_ELASTIC;
            }
            case 32 -> {
                return EasingType.EASE_IN_OUT_ELASTIC;
            }
            case 33 -> {
                return EasingType.EASE_IN_BOUNCE;
            }
            case 34 -> {
                return EasingType.EASE_OUT_BOUNCE;
            }
            case 35 -> {
                return EasingType.EASE_IN_OUT_BOUNCE;
            }
        }
        return null;
    }
}
