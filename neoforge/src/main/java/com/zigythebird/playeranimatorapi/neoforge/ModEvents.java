package com.zigythebird.playeranimatorapi.neoforge;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import com.zigythebird.playeranimatorapi.ModInit;
import com.zigythebird.playeranimatorapi.commands.PlayPlayerAnimationCommand;
import com.zigythebird.playeranimatorapi.commands.StopPlayerAnimationCommand;

public class ModEvents {

    @Mod.EventBusSubscriber(modid = ModInit.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public class ModEventListener {

        @SubscribeEvent
        public static void registerCommands(RegisterCommandsEvent event) {
            PlayPlayerAnimationCommand.register(event.getDispatcher());
            StopPlayerAnimationCommand.register(event.getDispatcher());
        }
    }
}
