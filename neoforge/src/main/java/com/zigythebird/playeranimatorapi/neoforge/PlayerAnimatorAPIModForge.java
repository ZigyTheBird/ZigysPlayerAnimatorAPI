package com.zigythebird.playeranimatorapi.neoforge;

import com.zigythebird.playeranimatorapi.ModInit;
import com.zigythebird.playeranimatorapi.ModInitClient;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

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
