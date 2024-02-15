package zigy.playeranimatorapi.forge;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import zigy.playeranimatorapi.PlayerAnimatorAPIClient;
import zigy.playeranimatorapi.PlayerAnimatorAPIMod;

@Mod(PlayerAnimatorAPIMod.MOD_ID)
public class PlayerAnimatorAPIModForge {
    public PlayerAnimatorAPIModForge() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::ClientInit);

        PlayerAnimatorAPIMod.init();
    }

    private void ClientInit(final FMLClientSetupEvent event) {
        PlayerAnimatorAPIClient.init();
    }
}
