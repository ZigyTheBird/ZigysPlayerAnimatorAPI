package com.zigythebird.playeranimatorapi;

import com.zigythebird.multiloaderutils.utils.NetworkManager;
import com.zigythebird.playeranimatorapi.data.PlayerAnimationData;
import com.zigythebird.playeranimatorapi.playeranims.PlayerAnimations;
import com.zigythebird.playeranimatorapi.registry.AnimModifierRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;

import java.util.UUID;

public class ModInitClient {

    public static boolean renderingGUI = false;

    public static void init() {
        PlayerAnimations.init();
        AnimModifierRegistry.register();

        NetworkManager.registerReceiver(NetworkManager.Side.S2C, ModInit.altPlayPlayerAnimationPacket, (buf, context) -> {
            try {
                PlayerAnimations.playAnimation(PlayerAnimationData.STREAM_CODEC.decode(buf));
            }
            catch (NullPointerException | IllegalStateException e) {
                ModInit.LOGGER.error(e.getMessage());
            }
        });

        NetworkManager.registerReceiver(NetworkManager.Side.S2C, ModInit.altStopPlayerAnimationPacket, (buf, context) -> {

            UUID uuid = buf.readUUID();
            ResourceLocation resourceLocation = buf.readResourceLocation();

            try {
                PlayerAnimations.stopAnimation((AbstractClientPlayer) Minecraft.getInstance().level.getPlayerByUUID(uuid), resourceLocation);
            }
            catch (NullPointerException e) {
                ModInit.LOGGER.error(e.getMessage());
            }
        });
    }
}
