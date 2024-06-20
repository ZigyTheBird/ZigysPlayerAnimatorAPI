package com.zigythebird.playeranimatorapi.fabric;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import com.zigythebird.playeranimatorapi.ModInit;
import com.zigythebird.playeranimatorapi.ModInitClient;
import com.zigythebird.playeranimatorapi.data.PlayerAnimationData;
import com.zigythebird.playeranimatorapi.playeranims.PlayerAnimations;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;

import java.util.UUID;

import static com.zigythebird.playeranimatorapi.API.PlayerAnimAPI.gson;

public class PlayerAnimatorAPIClientFabric implements ClientModInitializer {

    public static final ResourceLocation altPlayPlayerAnimationPacket = new ResourceLocation(ModInit.MOD_ID, "alt_play_player_animation_packet");
    public static final ResourceLocation altStopPlayerAnimationPacket = new ResourceLocation(ModInit.MOD_ID, "alt_stop_player_animation_packet");

    @Override
    public void onInitializeClient() {
        ModInitClient.init();

        ClientPlayNetworking.registerGlobalReceiver(altPlayPlayerAnimationPacket, (client, handler, buf, responseSender) -> {
            String jsonData = buf.readUtf();
            boolean replaceTick = buf.readBoolean();

            client.execute(() -> {
                try {
                    PlayerAnimationData data = PlayerAnimationData.CODEC.parse(JsonOps.INSTANCE, gson.fromJson(jsonData, JsonElement.class)).getOrThrow(true, ModInit.LOGGER::warn);
                    AbstractClientPlayer player2 = (AbstractClientPlayer) Minecraft.getInstance().level.getPlayerByUUID(data.playerUUID());
                    PlayerAnimations.playAnimation(player2, data, replaceTick);
                }
                catch (NullPointerException e) {
                    ModInit.LOGGER.error(e.getMessage());
                }
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(altStopPlayerAnimationPacket, (client, handler, buf, responseSender) -> {

            UUID uuid = buf.readUUID();
            ResourceLocation resourceLocation = buf.readResourceLocation();

            client.execute(() -> {
                try {
                    PlayerAnimations.stopAnimation((AbstractClientPlayer) Minecraft.getInstance().level.getPlayerByUUID(uuid), resourceLocation);
                }
                catch (NullPointerException e) {
                    ModInit.LOGGER.error(e.getMessage());
                }
            });
        });
    }
}
