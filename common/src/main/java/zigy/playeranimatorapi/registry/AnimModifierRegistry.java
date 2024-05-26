package zigy.playeranimatorapi.registry;

import com.google.gson.JsonObject;
import dev.kosmx.playerAnim.api.layered.modifier.AbstractModifier;
import dev.kosmx.playerAnim.api.layered.modifier.MirrorModifier;
import dev.kosmx.playerAnim.api.layered.modifier.SpeedModifier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;
import zigy.playeranimatorapi.modifier.HeadPosBoundCamera;
import zigy.playeranimatorapi.modifier.HeadRotBoundCamera;
import zigy.playeranimatorapi.modifier.LengthModifier;
import zigy.playeranimatorapi.modifier.MirrorOnAltHandModifier;
import zigy.playeranimatorapi.playeranims.CustomModifierLayer;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

@Environment(EnvType.CLIENT)
public class AnimModifierRegistry {

    private static final Map<ResourceLocation, BiFunction<CustomModifierLayer, JsonObject, AbstractModifier>> modifiers = new HashMap<>();

    public static void registerModifier(ResourceLocation ID, BiFunction<CustomModifierLayer, JsonObject, AbstractModifier> function) {
        modifiers.put(ID, function);
    }

    public static Map<ResourceLocation, BiFunction<CustomModifierLayer, JsonObject, AbstractModifier>> getModifiers() {
        return modifiers;
    }

    public static void register() {
        registerModifier(new ResourceLocation("player-animator", "mirror"), (layer, json) -> new MirrorModifier());
        registerModifier(new ResourceLocation("player-animator", "speed"), (layer, json) -> {try {return new SpeedModifier(json.get("speed").getAsFloat());}catch (NullPointerException | UnsupportedOperationException | IllegalArgumentException e) {return new SpeedModifier(1);}});
        registerModifier(new ResourceLocation("playeranimatorapi", "length"), (layer, json) -> {try {return new LengthModifier(layer, json.get("desiredLength").getAsFloat());}catch (NullPointerException | UnsupportedOperationException | IllegalArgumentException e) {return new LengthModifier(layer, -1);}});
        registerModifier(new ResourceLocation("playeranimatorapi", "mirroronalthand"), (layer, json) -> new MirrorOnAltHandModifier(layer));
        registerModifier(new ResourceLocation("playeranimatorapi", "headposboundcamera"), (layer, json) -> new HeadPosBoundCamera(layer));
        registerModifier(new ResourceLocation("playeranimatorapi", "headrotboundcamera"), (layer, json) -> new HeadRotBoundCamera(layer));
    }
}
