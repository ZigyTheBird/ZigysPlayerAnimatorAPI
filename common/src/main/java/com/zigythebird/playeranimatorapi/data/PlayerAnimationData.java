package com.zigythebird.playeranimatorapi.data;

import com.zigythebird.multiloaderutils.misc.ModCodecs;
import com.zigythebird.playeranimatorapi.modifier.CommonModifier;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.Utf8String;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Use at your own risk.
 * All parameters explained in the wiki.
 */
@ApiStatus.Internal
public record PlayerAnimationData(UUID playerUUID, ResourceLocation animationID, PlayerParts parts, List<CommonModifier> modifiers,
                                  int fadeLength, int easeID, int priority, int startTick) {

    public static final StreamCodec<ByteBuf, UUID> UUID_STREAM_CODEC = new StreamCodec<>() {
        @Override
        public UUID decode(ByteBuf object) {
            return UUID.fromString(Utf8String.read(object, 32767));
        }

        @Override
        public void encode(ByteBuf object, UUID object2) {
            Utf8String.write(object, object2.toString(), 32767);
        }
    };

    public static final StreamCodec<FriendlyByteBuf, PlayerAnimationData> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public PlayerAnimationData decode(FriendlyByteBuf buf) {
            return new PlayerAnimationData(
                    UUID_STREAM_CODEC.decode(buf),
                    ModCodecs.RESOURCELOCATION.decode(buf),
                    PlayerParts.STREAM_CODEC.decode(buf),
                    CommonModifier.STREAM_CODEC.apply(ByteBufCodecs.list()).decode(buf),
                    ByteBufCodecs.INT.decode(buf),
                    ByteBufCodecs.INT.decode(buf),
                    ByteBufCodecs.INT.decode(buf),
                    ByteBufCodecs.INT.decode(buf)
            );
        }

        @Override
        public void encode(FriendlyByteBuf buf, PlayerAnimationData obj) {
            UUID_STREAM_CODEC.encode(buf, obj.playerUUID());
            ModCodecs.RESOURCELOCATION.encode(buf, obj.animationID());
            PlayerParts.STREAM_CODEC.encode(buf, obj.parts() != null ? obj.parts() : PlayerParts.allEnabled);
            CommonModifier.STREAM_CODEC.apply(ByteBufCodecs.list()).encode(buf, obj.modifiers() != null ? obj.modifiers() : new ArrayList<>());
            ByteBufCodecs.INT.encode(buf, obj.fadeLength());
            ByteBufCodecs.INT.encode(buf, obj.easeID());
            ByteBufCodecs.INT.encode(buf, obj.priority());
            ByteBufCodecs.INT.encode(buf, obj.startTick());
        }
    };

    public PlayerAnimationData getDataWithStartTick(int startTick) {
        return new PlayerAnimationData(playerUUID, animationID, parts, modifiers, fadeLength, easeID, priority, startTick);
    }
}
