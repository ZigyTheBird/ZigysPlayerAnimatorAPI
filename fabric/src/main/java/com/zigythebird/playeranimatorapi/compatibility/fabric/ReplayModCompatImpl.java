package com.zigythebird.playeranimatorapi.compatibility.fabric;

import com.mojang.serialization.JsonOps;
import com.replaymod.recording.ReplayModRecording;
import com.zigythebird.playeranimatorapi.API.PlayerAnimAPI;
import com.zigythebird.playeranimatorapi.ModInit;
import com.zigythebird.playeranimatorapi.data.PlayerAnimationData;
import com.zigythebird.playeranimatorapi.data.PlayerParts;
import com.zigythebird.playeranimatorapi.fabric.PlayerAnimatorAPIClientFabric;
import com.zigythebird.playeranimatorapi.modifier.CommonModifier;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class ReplayModCompatImpl {
    public static void playPlayerAnim(AbstractClientPlayer player, ResourceLocation animationID, PlayerParts parts, List<CommonModifier> modifiers, int fadeLength, int easeID, int priority, boolean firstPersonEnabled, boolean replaceTick) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        PlayerAnimationData data = new PlayerAnimationData(player.getUUID(), animationID,
                parts, modifiers, fadeLength, easeID, priority, firstPersonEnabled);
        buf.writeUtf(PlayerAnimAPI.gson.toJson(PlayerAnimationData.CODEC.encodeStart(JsonOps.INSTANCE, data).getOrThrow(true, ModInit.LOGGER::warn)));
        buf.writeBoolean(replaceTick);
        ReplayModRecording.instance.getConnectionEventHandler().getPacketListener().save(ServerPlayNetworking.createS2CPacket(PlayerAnimatorAPIClientFabric.altPlayPlayerAnimationPacket, buf));
    }

    public static void stopPlayerAnim(AbstractClientPlayer player, ResourceLocation animationID) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeUUID(player.getUUID());
        buf.writeResourceLocation(animationID);
        ReplayModRecording.instance.getConnectionEventHandler().getPacketListener().save(ServerPlayNetworking.createS2CPacket(PlayerAnimatorAPIClientFabric.altStopPlayerAnimationPacket, buf));
    }
}
