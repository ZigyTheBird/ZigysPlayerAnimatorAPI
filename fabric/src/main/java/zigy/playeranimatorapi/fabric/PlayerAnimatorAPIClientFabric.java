package zigy.playeranimatorapi.fabric;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.resources.ResourceLocation;
import zigy.playeranimatorapi.PlayerAnimatorAPIClient;
import zigy.playeranimatorapi.PlayerAnimatorAPIMod;
import zigy.playeranimatorapi.playeranims.PlayerAnimations;

public class PlayerAnimatorAPIClientFabric implements ClientModInitializer {

    public static final ResourceLocation playerAnimPacket = new ResourceLocation(PlayerAnimatorAPIMod.MOD_ID, "player_anim");
    public static final ResourceLocation playerAnimStopPacket = new ResourceLocation(PlayerAnimatorAPIMod.MOD_ID, "player_anim_stop");

    @Override
    public void onInitializeClient() {
        PlayerAnimatorAPIClient.init();

        ClientPlayNetworking.registerGlobalReceiver(playerAnimPacket, (client, handler, buf, responseSender)
                -> PlayerAnimations.receivePacket(buf.readUtf()));
        ClientPlayNetworking.registerGlobalReceiver(playerAnimStopPacket, (client, handler, buf, responseSender)
                -> PlayerAnimations.stopAnimation(buf.readUUID(), buf.readResourceLocation()));
    }
}
