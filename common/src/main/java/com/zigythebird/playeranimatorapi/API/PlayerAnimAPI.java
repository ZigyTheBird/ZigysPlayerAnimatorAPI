package com.zigythebird.playeranimatorapi.API;

import com.zigythebird.multiloaderutils.utils.NetworkManager;
import com.zigythebird.playeranimatorapi.ModInit;
import com.zigythebird.playeranimatorapi.data.PlayerAnimationData;
import com.zigythebird.playeranimatorapi.data.PlayerParts;
import com.zigythebird.playeranimatorapi.example.FirstPersonExample;
import com.zigythebird.playeranimatorapi.misc.PlayerInterface;
import com.zigythebird.playeranimatorapi.modifier.CommonModifier;
import com.zigythebird.playeranimatorapi.utils.CommonPlayerLookup;
import dev.kosmx.playerAnim.core.util.Pair;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;

import java.util.List;

/**
 * Class for playing player animations from server side.
 * Look at the GitHub wiki for information on what the method parameters do.
 */
public class PlayerAnimAPI {

    public static final ResourceLocation playerAnimPacket = ResourceLocation.fromNamespaceAndPath(ModInit.MOD_ID, "player_anim");
    public static final ResourceLocation playerAnimStopPacket = ResourceLocation.fromNamespaceAndPath(ModInit.MOD_ID, "player_anim_stop");

    public static void playPlayerAnim(ServerLevel level, Player player, ResourceLocation animationID) {
        playPlayerAnim(level, player, animationID, PlayerParts.allEnabled,
                null, -1, -1, 1000, 0);
    }

    public static void playPlayerAnim(ServerLevel level, Player player, ResourceLocation animationID, PlayerParts parts, List<CommonModifier> modifiers, int priority) {
        playPlayerAnim(level, player, animationID, parts, modifiers, -1, -1, priority, 0);
    }

    public static void playPlayerAnim(ServerLevel level, Player player, ResourceLocation animationID, PlayerParts parts, List<CommonModifier> modifiers,
                                      int fadeLength, int easeID, int priority, boolean firstPersonEnabled) {
        if (firstPersonEnabled) {
            modifiers.add(FirstPersonExample.FIRST_PERSON_MODIFIER);
        }

        playPlayerAnim(level, player, animationID, parts, modifiers, fadeLength, easeID, priority, 0);
    }

    public static void playPlayerAnim(ServerLevel level, Player player, ResourceLocation animationID, PlayerParts parts, List<CommonModifier> modifiers,
                                      int fadeLength, int easeID, int priority, int startTick) {
        PlayerAnimationData data = new PlayerAnimationData(player.getUUID(), animationID,
                parts, modifiers, fadeLength, easeID, priority, startTick);

        playPlayerAnim(level, player, data);
    }

    public static void playPlayerAnim(ServerLevel level, Player player, PlayerAnimationData data) {
        if (data.animationID() == null) return;
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        PlayerAnimationData.STREAM_CODEC.encode(buf, data);
        NetworkManager.sendToPlayers(CommonPlayerLookup.tracking(level, player.chunkPosition()), playerAnimPacket, buf);
        if (data.startTick() < 0) {
            Pair<Integer, PlayerAnimationData> oldData = ((PlayerInterface)player).paapi$getLastAnim();
            if (oldData != null) {
                int ticksPassed = player.tickCount - oldData.getLeft();
                ((PlayerInterface) player).setLastAnim(data.getDataWithStartTick(ticksPassed));
                return;
            }
        }
        ((PlayerInterface)player).setLastAnim(data);
    }

    /**Stop a specific player animation.*/
    public static void stopPlayerAnim(ServerLevel level, Player player, ResourceLocation animationID) {
        if (animationID == null) return;

        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeUUID(player.getUUID());
        buf.writeResourceLocation(animationID);
        NetworkManager.sendToPlayers(CommonPlayerLookup.tracking(level, player.chunkPosition()), playerAnimStopPacket, buf);
        Pair<Integer, PlayerAnimationData> data = ((PlayerInterface)player).paapi$getLastAnim();
        if (data != null && data.getRight().animationID().equals(animationID)) ((PlayerInterface)player).setLastAnim(null);
    }
}
