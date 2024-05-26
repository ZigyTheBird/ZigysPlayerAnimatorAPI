package zigy.playeranimatorapi.azure;

import mod.azure.azurelib.common.api.client.model.GeoModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import zigy.playeranimatorapi.playeranims.PlayerAnimations;

import java.util.HashMap;
import java.util.Map;

public class PlayerAnimationModel extends GeoModel<AbstractClientPlayer> {

    public static Map<String, ResourceLocation> resourceLocations = new HashMap<>();

    @Override
    public ResourceLocation getModelResource(AbstractClientPlayer player) {
        ResourceLocation geckoResource = getCurrentGeckoResource(player);
        return getResourceLocation(geckoResource.getNamespace() + ":geo/player_animation/" + geckoResource.getPath() + ".geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(AbstractClientPlayer player) {
        ResourceLocation geckoResource = getCurrentGeckoResource(player);
        return getResourceLocation(geckoResource.getNamespace() + ":textures/player_animation/" + geckoResource.getPath() + ".png");
    }

    @Override
    public ResourceLocation getAnimationResource(AbstractClientPlayer player) {
        ResourceLocation geckoResource = getCurrentGeckoResource(player);
        return getResourceLocation(geckoResource.getNamespace() + ":animations/player_animation/" + geckoResource.getPath() + ".animation.json");
    }

    public static ResourceLocation getCurrentGeckoResource(AbstractClientPlayer player) {
        ResourceLocation currentAnim = PlayerAnimations.getModifierLayer(player).currentAnim;
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

    public boolean allResourcesExist(AbstractClientPlayer player) {
        ResourceManager manager = Minecraft.getInstance().getResourceManager();
        return getCurrentGeckoResource(player) != null && manager.getResource(getModelResource(player)).isPresent() && manager.getResource(getTextureResource(player)).isPresent()
                && manager.getResource(getAnimationResource(player)).isPresent();
    }
}
