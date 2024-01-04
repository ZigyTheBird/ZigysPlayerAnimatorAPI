package zigy.playeranimatorapi.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public record PlayerAnimationData(UUID playerUUID, ResourceLocation normalAnimationID, ResourceLocation crouchedAnimationID,
                                  ResourceLocation swimmingAnimationID, PlayerParts parts, int fadeLength,
                                  float desiredLength, int easeID, boolean firstPersonEnabled, boolean shouldMirror, boolean shouldFollowPlayerView) {

    public static final Codec<UUID> UUID_CODEC = Codec.list(Codec.LONG).comapFlatMap(PlayerAnimationData::readUUID, PlayerAnimationData::writeUUID).stable();
    public static final Codec<ResourceLocation> RESOURCE_LOCATION_CODEC = Codec.STRING.comapFlatMap(ResourceLocation::read, ResourceLocation::toString).stable();;

    public static DataResult<UUID> readUUID(List<Long> input) {
        return DataResult.success(new UUID(input.get(0), input.get(1)));
    }

    public static List<Long> writeUUID(UUID uuid) {
        List<Long> list = new ArrayList<>();
        list.add(uuid.getMostSignificantBits());
        list.add(uuid.getLeastSignificantBits());
        return list;
    }

    public static DataResult<ResourceLocation> readResourceLocation(String string) {
        if (string == null) {
            return DataResult.success(null);
        }
        return ResourceLocation.read(string);
    }

    public static String resourceLocationToString(ResourceLocation location) {
        if (location == null) {
            return null;
        }

        return location.toString();
    }

    public static final Codec<PlayerAnimationData> CODEC = RecordCodecBuilder.create(playerAnimationDataInstance -> playerAnimationDataInstance.group(
            UUID_CODEC.fieldOf("playerUUID").forGetter(PlayerAnimationData::playerUUID),
            ResourceLocation.CODEC.fieldOf("normalAnimationID").forGetter(PlayerAnimationData::normalAnimationID),
            ResourceLocation.CODEC.fieldOf("crouchedAnimationID").forGetter(PlayerAnimationData::crouchedAnimationID),
            ResourceLocation.CODEC.fieldOf("swimmingAnimationID").forGetter(PlayerAnimationData::swimmingAnimationID),
            PlayerParts.CODEC.fieldOf("parts").forGetter(PlayerAnimationData::parts),
            Codec.INT.fieldOf("fadeLength").forGetter(PlayerAnimationData::fadeLength),
            Codec.FLOAT.fieldOf("desiredLength").forGetter(PlayerAnimationData::desiredLength),
            Codec.INT.fieldOf("easeID").forGetter(PlayerAnimationData::easeID),
            Codec.BOOL.fieldOf("firstPersonEnabled").forGetter(PlayerAnimationData::firstPersonEnabled),
            Codec.BOOL.fieldOf("shouldMirror").forGetter(PlayerAnimationData::shouldMirror),
            Codec.BOOL.fieldOf("shouldFollowPlayerView").forGetter(PlayerAnimationData::shouldFollowPlayerView)
            ).apply(playerAnimationDataInstance, PlayerAnimationData::new));
}
