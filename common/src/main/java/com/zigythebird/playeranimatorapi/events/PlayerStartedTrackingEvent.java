package com.zigythebird.playeranimatorapi.events;

import com.zigythebird.multiloaderutils.utils.NetworkManager;
import com.zigythebird.playeranimatorapi.API.PlayerAnimAPI;
import com.zigythebird.playeranimatorapi.data.PlayerAnimationData;
import com.zigythebird.playeranimatorapi.misc.PlayerInterface;
import dev.kosmx.playerAnim.core.util.Pair;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class PlayerStartedTrackingEvent {
    public static void event(Player player, Entity entity) {
        //Prob never called client side, but I am not taking any risks here.
        if (!player.level().isClientSide() && entity instanceof Player player2) {
            Pair<Integer, PlayerAnimationData> data = ((PlayerInterface)player2).paapi$getLastAnim();
            if (data == null) return;
            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
            PlayerAnimationData animationData = data.getRight();
            if (animationData.startTick() >= 0) {
                int ticksPassed = player2.tickCount - data.getLeft();
                animationData = new PlayerAnimationData(animationData.playerUUID(), animationData.animationID(), animationData.parts(), animationData.modifiers(),
                        animationData.fadeLength(), animationData.easeID(), animationData.priority(), animationData.startTick() + ticksPassed);
            }
            PlayerAnimationData.STREAM_CODEC.encode(buf, animationData);
            NetworkManager.sendToPlayer((ServerPlayer) player, PlayerAnimAPI.playerAnimPacket, buf);
        }
    }
}
