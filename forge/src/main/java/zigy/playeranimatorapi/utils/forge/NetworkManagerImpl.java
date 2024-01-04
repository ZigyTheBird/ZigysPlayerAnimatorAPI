package zigy.playeranimatorapi.utils.forge;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.PacketDistributor;
import zigy.playeranimatorapi.PlayerAnimatorAPIMod;
import zigy.playeranimatorapi.forge.network.PlayerAPIPacketHandler;
import zigy.playeranimatorapi.forge.network.server.PlayerAnimPacketS2C;
import zigy.playeranimatorapi.forge.network.server.PlayerAnimStopPacketS2C;

import java.util.Collection;

public class NetworkManagerImpl {

    public static final ResourceLocation playerAnimPacket = new ResourceLocation(PlayerAnimatorAPIMod.MOD_ID, "player_anim");
    public static final ResourceLocation playerAnimStopPacket = new ResourceLocation(PlayerAnimatorAPIMod.MOD_ID, "player_anim_stop");

    public static void sendToPlayers(Collection<ServerPlayer> players, ResourceLocation packet, FriendlyByteBuf buf) {
        for (ServerPlayer player : players) {
            if (packet.equals(playerAnimPacket)) {
                PlayerAPIPacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new PlayerAnimPacketS2C(buf));
            }
            else if (packet.equals(playerAnimStopPacket)) {
                PlayerAPIPacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new PlayerAnimStopPacketS2C(buf));
            }
        }
    }
}
