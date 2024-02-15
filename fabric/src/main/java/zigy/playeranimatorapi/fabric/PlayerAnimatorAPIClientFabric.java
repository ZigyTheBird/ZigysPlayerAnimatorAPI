package zigy.playeranimatorapi.fabric;

import net.fabricmc.api.ClientModInitializer;
import zigy.playeranimatorapi.PlayerAnimatorAPIClient;

public class PlayerAnimatorAPIClientFabric implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        PlayerAnimatorAPIClient.init();
    }
}
