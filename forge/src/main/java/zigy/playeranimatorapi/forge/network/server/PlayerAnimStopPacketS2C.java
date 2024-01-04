package zigy.playeranimatorapi.forge.network.server;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;
import zigy.playeranimatorapi.playeranims.PlayerAnimations;

import java.util.UUID;
import java.util.function.Supplier;

public class PlayerAnimStopPacketS2C {

    public UUID player;
    public ResourceLocation anim;

    public PlayerAnimStopPacketS2C(UUID player, ResourceLocation anim) {
        this.player = player;
        this.anim = anim;
    }

    public PlayerAnimStopPacketS2C(FriendlyByteBuf buf) {
        this.player = buf.readUUID();
        this.anim = buf.readResourceLocation();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUUID(this.player);
        buf.writeResourceLocation(this.anim);
    }

    public void apply(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            PlayerAnimations.stopAnimation(player, anim);
        });
    }
}
