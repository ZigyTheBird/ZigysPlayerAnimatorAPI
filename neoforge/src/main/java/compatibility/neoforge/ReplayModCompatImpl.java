package compatibility.neoforge;

import com.replaymod.recording.ReplayModRecording;
import com.zigythebird.multiloaderutils.network.MultiloaderPacket;
import com.zigythebird.playeranimatorapi.ModInit;
import com.zigythebird.playeranimatorapi.data.PlayerAnimationData;
import com.zigythebird.playeranimatorapi.data.PlayerParts;
import com.zigythebird.playeranimatorapi.modifier.CommonModifier;
import io.netty.buffer.Unpooled;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class ReplayModCompatImpl {
    public static void playPlayerAnim(AbstractClientPlayer player, ResourceLocation animationID, PlayerParts parts, List<CommonModifier> modifiers, int fadeLength, int easeID, int priority, int startTick) {
        if (ReplayModRecording.instance.getConnectionEventHandler().getPacketListener() != null) {
            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
            PlayerAnimationData data = new PlayerAnimationData(player.getUUID(), animationID,
                    parts, modifiers, fadeLength, easeID, priority, startTick);
            PlayerAnimationData.STREAM_CODEC.encode(buf, data);
            ReplayModRecording.instance.getConnectionEventHandler().getPacketListener().save(new MultiloaderPacket(buf, ModInit.altPlayPlayerAnimationPacket).toVanillaClientbound());
        }
    }

    public static void stopPlayerAnim(AbstractClientPlayer player, ResourceLocation animationID) {
        if (ReplayModRecording.instance.getConnectionEventHandler().getPacketListener() != null) {
            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
            buf.writeUUID(player.getUUID());
            buf.writeResourceLocation(animationID);
            ReplayModRecording.instance.getConnectionEventHandler().getPacketListener().save(new MultiloaderPacket(buf, ModInit.altStopPlayerAnimationPacket).toVanillaClientbound());
        }
    }
}
