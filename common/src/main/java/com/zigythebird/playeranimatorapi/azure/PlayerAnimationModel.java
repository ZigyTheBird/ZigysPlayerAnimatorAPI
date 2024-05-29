package com.zigythebird.playeranimatorapi.azure;

import com.zigythebird.playeranimatorapi.playeranims.PlayerAnimations;
import mod.azure.azurelib.model.GeoModel;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;

import java.util.HashMap;
import java.util.Map;

public class PlayerAnimationModel extends GeoModel<AnimatablePlayerLayer> {

    public static Map<String, ResourceLocation> resourceLocations = new HashMap<>();

    @Override
    public ResourceLocation getModelResource(AnimatablePlayerLayer player) {
        ResourceLocation geckoResource = getCurrentGeckoResource(player);
        return getResourceLocation(geckoResource.getNamespace() + ":geo/player_animation/" + geckoResource.getPath() + ".geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(AnimatablePlayerLayer player) {
        ResourceLocation geckoResource = getCurrentGeckoResource(player);
        return getResourceLocation(geckoResource.getNamespace() + ":textures/player_animation/" + geckoResource.getPath() + ".png");
    }

    @Override
    public ResourceLocation getAnimationResource(AnimatablePlayerLayer player) {
        ResourceLocation geckoResource = getCurrentGeckoResource(player);
        return getResourceLocation(geckoResource.getNamespace() + ":animations/player_animation/" + geckoResource.getPath() + ".animation.json");
    }

    public static ResourceLocation getCurrentGeckoResource(AnimatablePlayerLayer player) {
        ResourceLocation currentAnim = PlayerAnimations.getModifierLayer(player.getPlayer()).currentAnim;
        if (PlayerAnimations.geckoMap.containsKey(currentAnim)) {
            return PlayerAnimations.geckoMap.get(currentAnim);
        }
        return null;
    }

    public static ResourceLocation getResourceLocation(String resource) {
        if (!resourceLocations.containsKey(resource)) {
            resourceLocations.put(resource, new ResourceLocation(resource));
        }
        return resourceLocations.get(resource);
    }

    public boolean allResourcesExist(AnimatablePlayerLayer player) {
        ResourceManager manager = Minecraft.getInstance().getResourceManager();
        return getCurrentGeckoResource(player) != null && manager.getResource(getModelResource(player)).isPresent() && manager.getResource(getTextureResource(player)).isPresent()
                && manager.getResource(getAnimationResource(player)).isPresent();
    }
}
