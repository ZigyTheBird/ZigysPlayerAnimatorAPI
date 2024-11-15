package com.zigythebird.playeranimatorapi.API;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.serialization.JsonOps;
import com.zigythebird.multiloaderutils.utils.NetworkManager;
import com.zigythebird.playeranimatorapi.ModInit;
import com.zigythebird.playeranimatorapi.data.PlayerAnimationData;
import com.zigythebird.playeranimatorapi.data.PlayerParts;
import com.zigythebird.playeranimatorapi.modifier.CommonModifier;
import com.zigythebird.playeranimatorapi.utils.CommonPlayerLookup;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for playing player animations from server side.
 * Look at the GitHub wiki for information on what the method parameters do.
 */
public class PlayerAnimAPI {

    public static final ResourceLocation playerAnimPacket = ResourceLocation.fromNamespaceAndPath(ModInit.MOD_ID, "player_anim");
    public static final ResourceLocation playerAnimStopPacket = ResourceLocation.fromNamespaceAndPath(ModInit.MOD_ID, "player_anim_stop");

    public static final ResourceLocation MIRROR_ON_ALT_HAND = ResourceLocation.fromNamespaceAndPath("playeranimatorapi", "mirroronalthand");

    /**Use this if you are using an animation for an item.*/
    public static final List<CommonModifier> gameplayModifiers = new ArrayList<>(){
        {
            add(new CommonModifier(MIRROR_ON_ALT_HAND, null));
        }
    };

    public static Gson gson = new GsonBuilder().setLenient().serializeNulls().create();

    /**For emotes.*/
    public static void playPlayerAnim(ServerLevel level, Player player, ResourceLocation animationID) {
        playPlayerAnim(level, player, animationID, PlayerParts.allEnabled,
                null, -1, -1, 1000, false);
    }

    /**For gameplay related stuff like player animations for items.*/
    public static void playPlayerAnim(ServerLevel level, Player player, ResourceLocation animationID, PlayerParts parts, List<CommonModifier> modifiers, int priority) {
        playPlayerAnim(level, player, animationID, parts, modifiers, -1, -1, priority, false);
    }

    /**Play player animations with the PlayerAnimationData class.*/
    public static void playPlayerAnim(ServerLevel level, Player player, PlayerAnimationData data) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());

        buf.writeUtf(gson.toJson(PlayerAnimationData.CODEC.encodeStart(JsonOps.INSTANCE, data).getOrThrow()));
        NetworkManager.sendToPlayers(CommonPlayerLookup.tracking(level, player.chunkPosition()), playerAnimPacket, buf);
    }

    /**Play player animations with full customizability.*/
    public static void playPlayerAnim(ServerLevel level, Player player, ResourceLocation animationID, PlayerParts parts, List<CommonModifier> modifiers,
                                      int fadeLength, int easeID, int priority, boolean firstPersonEnabled) {

        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        PlayerAnimationData data = new PlayerAnimationData(player.getUUID(), animationID,
                parts, modifiers, fadeLength, easeID, priority, firstPersonEnabled);

        buf.writeUtf(gson.toJson(PlayerAnimationData.CODEC.encodeStart(JsonOps.INSTANCE, data).getOrThrow()));
        NetworkManager.sendToPlayers(CommonPlayerLookup.tracking(level, player.chunkPosition()), playerAnimPacket, buf);
    }

    /**Stop a specific player animation.*/
    public static void stopPlayerAnim(ServerLevel level, Player player, ResourceLocation animationID) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeUUID(player.getUUID());
        buf.writeResourceLocation(animationID);
        NetworkManager.sendToPlayers(CommonPlayerLookup.tracking(level, player.chunkPosition()), playerAnimStopPacket, buf);
    }
}
