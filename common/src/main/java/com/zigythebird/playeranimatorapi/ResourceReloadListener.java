package com.zigythebird.playeranimatorapi;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.zigythebird.playeranimatorapi.playeranims.PlayerAnimations;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import dev.kosmx.playerAnim.minecraftApi.codec.AnimationCodecs;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.util.GsonHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ResourceReloadListener implements ResourceManagerReloadListener {

    private static final Logger logger = LogManager.getLogger(ModInit.class);

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
        PlayerAnimations.animLengthsMap = new HashMap<>();
        PlayerAnimations.geckoMap = new HashMap<>();
        for (var resource : resourceManager.listResources("player_animation", location -> location.getPath().endsWith(".json")).entrySet()) {
            processResource(resource);
        }
        for (var resource : resourceManager.listResources("player_animations", location -> location.getPath().endsWith(".json")).entrySet()) {
            processResource(resource);
        }
    }

    public void processResource(Map.Entry<ResourceLocation, Resource> resource) {
        try {
            JsonObject jsonObject = GsonHelper.convertToJsonObject(JsonParser.parseReader(resource.getValue().openAsReader()), "resource");
            if (jsonObject.has("animations")) {
                for (var object : jsonObject.get("animations").getAsJsonObject().asMap().entrySet()) {
                    ResourceLocation resourceLocation = ResourceLocation.fromNamespaceAndPath(resource.getKey().getNamespace(), object.getKey().toLowerCase(Locale.ROOT));
                    PlayerAnimations.animLengthsMap.put(resourceLocation, object.getValue().getAsJsonObject().get("animation_length").getAsFloat());
                    if (object.getValue().getAsJsonObject().has("geckoResource")) {
                        PlayerAnimations.geckoMap.put(resourceLocation, ResourceLocation.parse(object.getValue().getAsJsonObject().get("geckoResource").getAsString()));
                    }
                }
            } else {
                for (var animation : AnimationCodecs.deserialize(AnimationCodecs.getExtension(resource.getKey().getPath()),  () -> {
                    try {
                        return resource.getValue().open();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }})) {

                    if (animation instanceof KeyframeAnimation) {
                        PlayerAnimations.animLengthsMap.put(ResourceLocation.fromNamespaceAndPath(resource.getKey().getNamespace(), PlayerAnimationRegistry.serializeTextToString((String) ((KeyframeAnimation)animation).extraData.get("name")).toLowerCase(Locale.ROOT)), (float) (((KeyframeAnimation)animation).endTick / 20));
                        if (jsonObject.has("geckoResource")) {
                            PlayerAnimations.geckoMap.put(ResourceLocation.fromNamespaceAndPath(resource.getKey().getNamespace(), PlayerAnimationRegistry.serializeTextToString((String) ((KeyframeAnimation)animation).extraData.get("name")).toLowerCase(Locale.ROOT)), ResourceLocation.parse(jsonObject.get("geckoResource").getAsString()));
                        }
                    }
                }
            }
        } catch (IOException | NullPointerException e) {
            logger.warn("Could not load animation resource " + resource.getKey().toString() + " " + e);
        }
    }
}
