package zigy.playeranimatorapi.utils.fabric;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.Collection;

public class NetworkManagerImpl {
    public static void sendToPlayers(Collection<ServerPlayer> players, ResourceLocation packet, FriendlyByteBuf buf) {
        for (ServerPlayer player : players) {
            ServerPlayNetworking.send(player, packet, buf);
        }
    }
}
