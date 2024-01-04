package zigy.playeranimatorapi.utils;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.apache.commons.lang3.NotImplementedException;

import java.util.Collection;

public class NetworkManager {

    @ExpectPlatform
    public static void sendToPlayers(Collection<ServerPlayer> players, ResourceLocation packet, FriendlyByteBuf buf) {
        throw new NotImplementedException();
    }
}
