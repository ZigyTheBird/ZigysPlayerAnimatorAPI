package zigy.playeranimatorapi.fabric;

import net.fabricmc.api.ClientModInitializer;
import zigy.playeranimatorapi.ModInitClient;

public class PlayerAnimatorAPIClientFabric implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ModInitClient.init();
    }
}
