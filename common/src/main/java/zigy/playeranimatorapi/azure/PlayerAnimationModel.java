package zigy.playeranimatorapi.azure;

import mod.azure.azurelib.model.GeoModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import zigy.playeranimatorapi.playeranims.PlayerAnimations;

import java.util.HashMap;
import java.util.Map;

public class PlayerAnimationModel extends GeoModel<AbstractClientPlayer> {

    public static Map<String, ResourceLocation> resourceLocations = new HashMap<>();

    public PlayerAnimationModel() {
    }

    @Override
    public ResourceLocation getModelResource(AbstractClientPlayer player) {
        ResourceLocation currentAnim = getCurrentAnim(player);
        return getResourceLocation(currentAnim.getNamespace() + ":geo/player_animation/" + currentAnim.getPath() + ".geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(AbstractClientPlayer player) {
        ResourceLocation currentAnim = getCurrentAnim(player);
        return getResourceLocation(currentAnim.getNamespace() + ":textures/player_animation/" + currentAnim.getPath() + ".png");
    }

    @Override
    public ResourceLocation getAnimationResource(AbstractClientPlayer player) {
        ResourceLocation currentAnim = getCurrentAnim(player);
        return getResourceLocation(currentAnim.getNamespace() + ":animations/player_animation/" + currentAnim.getPath() + ".animation.json");
    }

    public static ResourceLocation getCurrentAnim(AbstractClientPlayer player) {
        return PlayerAnimations.getModifierLayer(player).currentAnim;
    }

    public static ResourceLocation getResourceLocation(String resource) {
        if (!resourceLocations.containsKey(resource)) {
            resourceLocations.put(resource, new ResourceLocation(resource));
        }
        return resourceLocations.get(resource);
    }

    public boolean allResourcesExist(AbstractClientPlayer player) {
        ResourceManager manager = Minecraft.getInstance().getResourceManager();
        return manager.getResource(getModelResource(player)).isPresent() && manager.getResource(getTextureResource(player)).isPresent()
                && manager.getResource(getCurrentAnim(player)).isPresent();
    }
}
