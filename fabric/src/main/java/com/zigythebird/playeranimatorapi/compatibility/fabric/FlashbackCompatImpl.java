package com.zigythebird.playeranimatorapi.compatibility.fabric;

import com.moulberry.flashback.Flashback;
import com.zigythebird.multiloaderutils.network.MultiloaderPacket;
import com.zigythebird.playeranimatorapi.ModInit;
import com.zigythebird.playeranimatorapi.data.PlayerAnimationData;
import com.zigythebird.playeranimatorapi.data.PlayerParts;
import com.zigythebird.playeranimatorapi.modifier.CommonModifier;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.ConnectionProtocol;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class FlashbackCompatImpl {
    public static void playPlayerAnim(AbstractClientPlayer player, ResourceLocation animationID, PlayerParts parts, List<CommonModifier> modifiers, int fadeLength, int easeID, int priority, int startTick) {
        if (Flashback.RECORDER.readyToWrite()) {
            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
            PlayerAnimationData data = new PlayerAnimationData(player.getUUID(), animationID,
                    parts, modifiers, fadeLength, easeID, priority, startTick);
            PlayerAnimationData.STREAM_CODEC.encode(buf, data);
            Flashback.RECORDER.writePacketAsync(ServerPlayNetworking.createS2CPacket(new MultiloaderPacket(buf, ModInit.altPlayPlayerAnimationPacket)), ConnectionProtocol.PLAY);
        }
    }

    public static void stopPlayerAnim(AbstractClientPlayer player, ResourceLocation animationID) {
        if (Flashback.RECORDER.readyToWrite()) {
            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
            buf.writeUUID(player.getUUID());
            buf.writeResourceLocation(animationID);
            Flashback.RECORDER.writePacketAsync(ServerPlayNetworking.createS2CPacket(new MultiloaderPacket(buf, ModInit.altStopPlayerAnimationPacket)), ConnectionProtocol.PLAY);
        }
    }
}
