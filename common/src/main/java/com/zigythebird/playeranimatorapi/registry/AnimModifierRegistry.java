package com.zigythebird.playeranimatorapi.registry;

import com.google.gson.JsonObject;
import com.zigythebird.playeranimatorapi.modifier.*;
import com.zigythebird.playeranimatorapi.playeranims.CustomModifierLayer;
import dev.kosmx.playerAnim.api.firstPerson.FirstPersonConfiguration;
import dev.kosmx.playerAnim.api.firstPerson.FirstPersonMode;
import dev.kosmx.playerAnim.api.layered.modifier.AbstractModifier;
import dev.kosmx.playerAnim.api.layered.modifier.FirstPersonModifier;
import dev.kosmx.playerAnim.api.layered.modifier.MirrorModifier;
import dev.kosmx.playerAnim.api.layered.modifier.SpeedModifier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;

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
        registerModifier(ResourceLocation.fromNamespaceAndPath("player-animator", "mirror"), (layer, json) -> new MirrorModifier());
        registerModifier(ResourceLocation.fromNamespaceAndPath("player-animator", "speed"), (layer, json) -> {try {return new SpeedModifier(json.get("speed").getAsFloat());}catch (NullPointerException | UnsupportedOperationException | IllegalArgumentException e) {return new SpeedModifier(1);}});
        registerModifier(ResourceLocation.fromNamespaceAndPath("playeranimatorapi", "length"), (layer, json) -> {try {return new LengthModifier(layer, json.get("desiredLength").getAsFloat());}catch (NullPointerException | UnsupportedOperationException | IllegalArgumentException e) {return new LengthModifier(layer, -1);}});
        registerModifier(ResourceLocation.fromNamespaceAndPath("playeranimatorapi", "mirroronalthand"), (layer, json) -> new MirrorOnAltHandModifier(layer));
        registerModifier(ResourceLocation.fromNamespaceAndPath("playeranimatorapi", "headposboundcamera"), (layer, json) -> new HeadPosBoundCamera(layer));
        registerModifier(ResourceLocation.fromNamespaceAndPath("playeranimatorapi", "headrotboundcamera"), (layer, json) -> new HeadRotBoundCamera(layer));
        registerModifier(ResourceLocation.fromNamespaceAndPath("playeranimatorapi", "firstperson"), (layer, json) -> {
            FirstPersonMode firstPersonMode = FirstPersonMode.NONE;
            try {
                firstPersonMode = FirstPersonMode.valueOf(json.get("firstPersonMode").getAsString());
            }
            catch (IllegalArgumentException ignore) {}

            boolean showRightArm = false;
            boolean showLeftArm = false;
            boolean showRightItem = true;
            boolean showLeftItem = true;

            if (json.has("showRightArm")) showRightArm = json.get("showRightArm").getAsBoolean();
            if (json.has("showLeftArm")) showLeftArm = json.get("showLeftArm").getAsBoolean();
            if (json.has("showRightItem")) showRightItem = json.get("showRightItem").getAsBoolean();
            if (json.has("showLeftItem")) showLeftItem = json.get("showLeftItem").getAsBoolean();

            return new CustomFirstPersonModifier(firstPersonMode, new FirstPersonConfiguration(showRightArm, showLeftArm, showRightItem, showLeftItem));
        });
    }
}
