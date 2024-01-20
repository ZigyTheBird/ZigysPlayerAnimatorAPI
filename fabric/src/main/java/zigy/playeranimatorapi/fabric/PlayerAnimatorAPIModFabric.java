package zigy.playeranimatorapi.fabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.server.packs.PackType;
import zigy.playeranimatorapi.PlayerAnimatorAPIMod;
import zigy.playeranimatorapi.commands.PlayPlayerAnimationCommand;
import zigy.playeranimatorapi.commands.StopPlayerAnimationCommand;

public class PlayerAnimatorAPIModFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(new ResourceReloadListenerFabric());

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            PlayPlayerAnimationCommand.register(dispatcher);
            StopPlayerAnimationCommand.register(dispatcher);
        });

        PlayerAnimatorAPIMod.init();
    }
}
