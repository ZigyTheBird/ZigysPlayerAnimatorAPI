package zigy.playeranimatorapi.neoforge;

import dev.kosmx.playerAnim.core.util.Vec3f;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;
import net.neoforged.neoforge.client.event.ViewportEvent;
import zigy.playeranimatorapi.ModInit;
import zigy.playeranimatorapi.ModInitClient;
import zigy.playeranimatorapi.ResourceReloadListener;
import zigy.playeranimatorapi.utils.CameraUtils;

public class ClientEvents {

    @Mod.EventBusSubscriber(modid = ModInit.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class modEventBus {
        @SubscribeEvent
        public static void registerReloadListeners(RegisterClientReloadListenersEvent event) {
            event.registerReloadListener(new ResourceReloadListener());
        }
    }

    @Mod.EventBusSubscriber(modid = ModInit.MOD_ID, value = Dist.CLIENT)
    public static class ForgeEvents {
        @SubscribeEvent
        public static void computeCameraAngles(ViewportEvent.ComputeCameraAngles event) {
            CameraUtils.computeCameraLocation(event.getRenderer(), event.getCamera(), event.getPartialTick());
            Vec3f vec = CameraUtils.computeCameraAngles(event.getRenderer(), event.getCamera(), event.getPartialTick());
            if (vec != null) {
                event.setYaw(vec.getX());
                event.setPitch(vec.getY());
                event.setRoll(vec.getZ());
            }
        }
    }
}
