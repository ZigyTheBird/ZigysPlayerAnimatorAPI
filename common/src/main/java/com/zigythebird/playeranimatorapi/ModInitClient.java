package com.zigythebird.playeranimatorapi;

import com.zigythebird.playeranimatorapi.playeranims.PlayerAnimations;
import com.zigythebird.playeranimatorapi.registry.AnimModifierRegistry;

public class ModInitClient {

    public static boolean renderingGUI = false;

    public static void init() {
        PlayerAnimations.init();
        AnimModifierRegistry.register();
    }
}
