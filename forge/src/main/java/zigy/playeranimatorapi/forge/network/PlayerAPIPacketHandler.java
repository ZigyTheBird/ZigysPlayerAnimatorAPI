package zigy.playeranimatorapi.forge.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import zigy.playeranimatorapi.PlayerAnimatorAPIMod;
import zigy.playeranimatorapi.forge.network.server.PlayerAnimPacketS2C;
import zigy.playeranimatorapi.forge.network.server.PlayerAnimStopPacketS2C;

public class PlayerAPIPacketHandler {

    private static int ID() {
        return packetID++;
    }

    private static int packetID = 0;
    private static final String PROTOCOL_VERSION = "1.0";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(PlayerAnimatorAPIMod.MOD_ID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void register() {
        INSTANCE.messageBuilder(PlayerAnimPacketS2C.class, ID(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(PlayerAnimPacketS2C::new)
                .encoder(PlayerAnimPacketS2C::encode)
                .consumerMainThread(PlayerAnimPacketS2C::apply)
                .add();

        INSTANCE.messageBuilder(PlayerAnimStopPacketS2C.class, ID(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(PlayerAnimStopPacketS2C::new)
                .encoder(PlayerAnimStopPacketS2C::encode)
                .consumerMainThread(PlayerAnimStopPacketS2C::apply)
                .add();
    }
}
