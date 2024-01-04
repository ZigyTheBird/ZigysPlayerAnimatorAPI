package zigy.playeranimatorapi.fabric;

import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resources.ResourceLocation;
import zigy.playeranimatorapi.PlayerAnimatorAPIMod;
import zigy.playeranimatorapi.ResourceReloadListener;

public class ResourceReloadListenerFabric extends ResourceReloadListener implements IdentifiableResourceReloadListener {
    @Override
    public ResourceLocation getFabricId() {
        return new ResourceLocation(PlayerAnimatorAPIMod.MOD_ID, "my_resources");
    }
}
