package com.zigythebird.playeranimatorapi.fabric;

import com.zigythebird.playeranimatorapi.ModInitClient;
import net.fabricmc.api.ClientModInitializer;

public class PlayerAnimatorAPIClientFabric implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ModInitClient.init();
    }
}
