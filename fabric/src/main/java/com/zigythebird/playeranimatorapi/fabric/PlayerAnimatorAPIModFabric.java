package com.zigythebird.playeranimatorapi.fabric;

import com.zigythebird.playeranimatorapi.ModInit;
import com.zigythebird.playeranimatorapi.commands.PlayPlayerAnimationCommand;
import com.zigythebird.playeranimatorapi.commands.StopPlayerAnimationCommand;
import com.zigythebird.playeranimatorapi.events.PlayerStartedTrackingEvent;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.EntityTrackingEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.server.packs.PackType;

public class PlayerAnimatorAPIModFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(new ResourceReloadListenerFabric());

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            PlayPlayerAnimationCommand.register(dispatcher);
            StopPlayerAnimationCommand.register(dispatcher);
        });

        EntityTrackingEvents.START_TRACKING.register(((trackedEntity, player) -> PlayerStartedTrackingEvent.event(player, trackedEntity)));

        ModInit.init();
    }
}
