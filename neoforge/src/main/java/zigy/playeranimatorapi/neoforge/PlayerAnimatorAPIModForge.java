package zigy.playeranimatorapi.neoforge;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import zigy.playeranimatorapi.ModInit;
import zigy.playeranimatorapi.ModInitClient;

@Mod(ModInit.MOD_ID)
public class PlayerAnimatorAPIModForge {
    public PlayerAnimatorAPIModForge(IEventBus bus) {
        bus.addListener(PlayerAnimatorAPIModForge::ClientInit);
        ModInit.init();
    }

    private static void ClientInit(final FMLClientSetupEvent event) {
        ModInitClient.init();
    }
}
