package zigy.playeranimatorapi;

import zigy.playeranimatorapi.azure.PlayerAnimationRenderer;
import zigy.playeranimatorapi.playeranims.PlayerAnimations;
import zigy.playeranimatorapi.registry.AnimModifierRegistry;

public class PlayerAnimatorAPIClient {

    public static PlayerAnimationRenderer animationRenderer;

    public static void init() {
        PlayerAnimations.init();
        AnimModifierRegistry.register();
    }
}
