package zigy.playeranimatorapi;

import zigy.playeranimatorapi.playeranims.PlayerAnimations;
import zigy.playeranimatorapi.registry.AnimModifierRegistry;

public class PlayerAnimatorAPIClient {
    public static void init() {
        PlayerAnimations.init();
        AnimModifierRegistry.register();
    }
}
