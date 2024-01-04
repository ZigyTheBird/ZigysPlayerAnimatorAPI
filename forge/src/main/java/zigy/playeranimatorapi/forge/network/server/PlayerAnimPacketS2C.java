package zigy.playeranimatorapi.forge.network.server;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import zigy.playeranimatorapi.playeranims.PlayerAnimations;

import java.util.function.Supplier;

public class PlayerAnimPacketS2C {

    public String data;

    public PlayerAnimPacketS2C(String data) {
        this.data = data;
    }

    public PlayerAnimPacketS2C(FriendlyByteBuf buf) {
        this.data = buf.readUtf();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(this.data);
    }

    public void apply(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            PlayerAnimations.receivePacket(this.data);
        });
    }
}
