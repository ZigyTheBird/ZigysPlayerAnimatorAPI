package com.zigythebird.playeranimatorapi.fabric;

import com.zigythebird.playeranimatorapi.ResourceReloadListener;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resources.ResourceLocation;
import com.zigythebird.playeranimatorapi.ModInit;

public class ResourceReloadListenerFabric extends ResourceReloadListener implements IdentifiableResourceReloadListener {
    @Override
    public ResourceLocation getFabricId() {
        return new ResourceLocation(ModInit.MOD_ID, "my_resources");
    }
}
