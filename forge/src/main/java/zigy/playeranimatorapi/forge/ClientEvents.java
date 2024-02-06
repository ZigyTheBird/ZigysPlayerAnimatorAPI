package zigy.playeranimatorapi.forge;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import zigy.playeranimatorapi.PlayerAnimatorAPIMod;
import zigy.playeranimatorapi.ResourceReloadListener;
import zigy.playeranimatorapi.utils.ComputeCameraAngles;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = PlayerAnimatorAPIMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientEvents {

    @Mod.EventBusSubscriber(modid = PlayerAnimatorAPIMod.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class modEventBus {
        @SubscribeEvent
        public static void registerReloadListeners(RegisterClientReloadListenersEvent event) {
            event.registerReloadListener(new ResourceReloadListener());
        }
    }

    @Mod.EventBusSubscriber(modid = PlayerAnimatorAPIMod.MOD_ID, value = Dist.CLIENT)
    public static class ForgeEvents {
        @SubscribeEvent
        public static void computeCameraAngles(ViewportEvent.ComputeCameraAngles event) {
            ComputeCameraAngles.computeCameraAngles(event.getRenderer(), event.getCamera(), event.getPartialTick());
        }
    }
}
