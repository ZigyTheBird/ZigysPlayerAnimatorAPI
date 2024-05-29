package com.zigythebird.playeranimatorapi.forge;

import com.zigythebird.playeranimatorapi.ModInit;
import com.zigythebird.playeranimatorapi.ResourceReloadListener;
import com.zigythebird.playeranimatorapi.utils.CameraUtils;
import dev.kosmx.playerAnim.core.util.Vec3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = ModInit.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
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
//        @SubscribeEvent
//        public static void computeCameraAngles(ViewportEvent.ComputeCameraAngles event) {
//            CameraUtils.computeCameraLocation(event.getRenderer(), event.getCamera(), event.getPartialTick());
//            Vec3f vec = CameraUtils.computeCameraAngles(event.getRenderer(), event.getCamera(), event.getPartialTick());
//            if (vec != null) {
//                event.setYaw(vec.getX());
//                event.setPitch(vec.getY());
//                event.setRoll(vec.getZ());
//            }
//        }
    }
}
