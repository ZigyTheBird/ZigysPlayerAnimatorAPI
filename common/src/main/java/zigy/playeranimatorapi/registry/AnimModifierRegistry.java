package zigy.playeranimatorapi.registry;

import com.google.gson.JsonObject;
import dev.kosmx.playerAnim.api.layered.modifier.AbstractModifier;
import dev.kosmx.playerAnim.api.layered.modifier.MirrorModifier;
import dev.kosmx.playerAnim.api.layered.modifier.SpeedModifier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;
import zigy.playeranimatorapi.PlayerAnimatorAPIMod;
import zigy.playeranimatorapi.modifier.CameraModifier;
import zigy.playeranimatorapi.modifier.LengthModifier;
import zigy.playeranimatorapi.modifier.CommonModifier;
import zigy.playeranimatorapi.modifier.MirrorOnAltHandModifier;
import zigy.playeranimatorapi.playeranims.CustomModifierLayer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

@Environment(EnvType.CLIENT)
public class AnimModifierRegistry {

    private static final Map<ResourceLocation, BiFunction<CustomModifierLayer, JsonObject, AbstractModifier>> modifiers = new HashMap<>();

    @Environment(EnvType.CLIENT)
    public static void registerModifier(ResourceLocation ID, BiFunction<CustomModifierLayer, JsonObject, AbstractModifier> function) {
        modifiers.put(ID, function);
    }

    @Environment(EnvType.CLIENT)
    public static void applyModifiers(CustomModifierLayer layer, List<CommonModifier> commonModifiers) {
        for (CommonModifier commonModifier : commonModifiers) {
            if (modifiers.containsKey(commonModifier.ID)) {
                try {
                    layer.addModifier(modifiers.get(commonModifier.ID).apply(layer, commonModifier.data));
                } catch (NullPointerException | UnsupportedOperationException e) {
                    PlayerAnimatorAPIMod.LOGGER.error("Failed to apply modifier: " + commonModifier.ID + " :" + e);
                }
            }
        }
    }

    public static void register() {
        registerModifier(new ResourceLocation("player-animator", "mirror"), (layer, json) -> new MirrorModifier());
        registerModifier(new ResourceLocation("player-animator", "speed"), (layer, json) -> new SpeedModifier(json.get("speed").getAsFloat()));
        registerModifier(new ResourceLocation("playeranimatorapi", "camera"), (layer, json) -> new CameraModifier());
        registerModifier(new ResourceLocation("playeranimatorapi", "length"), (layer, json) -> new LengthModifier(layer, json.get("desiredLength").getAsFloat()));
        registerModifier(new ResourceLocation("playeranimatorapi", "mirrorOnAltHand"), (layer, json) -> new MirrorOnAltHandModifier(layer));
    }
}
