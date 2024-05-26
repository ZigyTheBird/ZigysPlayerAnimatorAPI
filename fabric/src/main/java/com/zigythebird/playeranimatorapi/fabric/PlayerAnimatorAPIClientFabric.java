package com.zigythebird.playeranimatorapi.fabric;

import net.fabricmc.api.ClientModInitializer;
import com.zigythebird.playeranimatorapi.ModInitClient;

public class PlayerAnimatorAPIClientFabric implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ModInitClient.init();
    }
}
