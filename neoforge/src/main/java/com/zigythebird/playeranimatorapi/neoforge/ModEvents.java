package com.zigythebird.playeranimatorapi.neoforge;

import com.zigythebird.playeranimatorapi.ModInit;
import com.zigythebird.playeranimatorapi.commands.PlayPlayerAnimationCommand;
import com.zigythebird.playeranimatorapi.commands.StopPlayerAnimationCommand;
import com.zigythebird.playeranimatorapi.events.PlayerStartedTrackingEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

public class ModEvents {

    @EventBusSubscriber(modid = ModInit.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
    public class ModEventListener {

        @SubscribeEvent
        public static void registerCommands(RegisterCommandsEvent event) {
            PlayPlayerAnimationCommand.register(event.getDispatcher());
            StopPlayerAnimationCommand.register(event.getDispatcher());
        }

        @SubscribeEvent
        public static void startTracking(PlayerEvent.StartTracking event) {
            PlayerStartedTrackingEvent.event(event.getEntity(), event.getTarget());
        }
    }
}
