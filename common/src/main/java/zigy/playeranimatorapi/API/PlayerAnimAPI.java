package zigy.playeranimatorapi.API;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.serialization.JsonOps;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import zigy.playeranimatorapi.PlayerAnimatorAPIMod;
import zigy.playeranimatorapi.data.PlayerAnimationData;
import zigy.playeranimatorapi.data.PlayerParts;
import zigy.playeranimatorapi.utils.CommonPlayerLookup;
import zigy.playeranimatorapi.utils.NetworkManager;

/**
 * Class for playing player animations from server side.
 * Look at the GitHub wiki for information on what the method parameters do.
 */
public class PlayerAnimAPI {

    public static final ResourceLocation playerAnimPacket = new ResourceLocation(PlayerAnimatorAPIMod.MOD_ID, "player_anim");
    public static final ResourceLocation playerAnimStopPacket = new ResourceLocation(PlayerAnimatorAPIMod.MOD_ID, "player_anim_stop");

    private static final Logger logger = LogManager.getLogger(PlayerAnimatorAPIMod.class);

    public static Gson gson = new GsonBuilder().setLenient().serializeNulls().create();

    /**For emotes.*/
    public static void playPlayerAnim(ServerLevel level, Player player, ResourceLocation animationID) {
        playPlayerAnim(level, player, animationID, PlayerParts.allEnabled,
                -1, -1, -1, false, false, false);
    }

    /**For gameplay related stuff like player animations for items.*/
    public static void playPlayerAnim(ServerLevel level, Player player, ResourceLocation animationID, PlayerParts parts, float desiredLength, boolean important) {
        playPlayerAnim(level, player, animationID, parts, -1,
                desiredLength, -1, false, true, important);
    }

    /**Play player animations with the PlayerAnimationData class.*/
    public static void playPlayerAnim(ServerLevel level, Player player, PlayerAnimationData data) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());

        buf.writeUtf(gson.toJson(PlayerAnimationData.CODEC.encodeStart(JsonOps.INSTANCE, data).getOrThrow(true, logger::warn)));
        NetworkManager.sendToPlayers(CommonPlayerLookup.tracking(level, player.chunkPosition()), playerAnimPacket, buf);
    }

    /**Play player animations with full customizability.*/
    public static void playPlayerAnim(ServerLevel level, Player player, ResourceLocation animationID, PlayerParts parts, int fadeLength, float desiredLength,
                                      int easeID, boolean firstPersonEnabled, boolean shouldMirror, boolean important) {

        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        PlayerAnimationData data = new PlayerAnimationData(player.getUUID(), animationID,
                parts, fadeLength, desiredLength, easeID, firstPersonEnabled, shouldMirror, important);

        buf.writeUtf(gson.toJson(PlayerAnimationData.CODEC.encodeStart(JsonOps.INSTANCE, data).getOrThrow(true, logger::warn)));
        NetworkManager.sendToPlayers(CommonPlayerLookup.tracking(level, player.chunkPosition()), playerAnimPacket, buf);
    }

    /**Stop a player animation*/
    public static void stopPlayerAnim(ServerLevel level, Player player, ResourceLocation animationID) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeUUID(player.getUUID());
        buf.writeResourceLocation(animationID);
        NetworkManager.sendToPlayers(CommonPlayerLookup.tracking(level, player.chunkPosition()), playerAnimStopPacket, buf);
    }
}
