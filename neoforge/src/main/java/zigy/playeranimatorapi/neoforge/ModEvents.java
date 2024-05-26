package zigy.playeranimatorapi.neoforge;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import zigy.playeranimatorapi.ModInit;
import zigy.playeranimatorapi.commands.PlayPlayerAnimationCommand;
import zigy.playeranimatorapi.commands.StopPlayerAnimationCommand;

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
