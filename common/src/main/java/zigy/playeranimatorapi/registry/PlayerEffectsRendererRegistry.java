package zigy.playeranimatorapi.registry;

import net.minecraft.client.renderer.entity.EntityRenderer;

import java.util.ArrayList;
import java.util.List;

public class PlayerEffectsRendererRegistry {
    private static List<EntityRenderer> renderers = new ArrayList<>();

    public static List<EntityRenderer> getRenderers() {
        return renderers;
    }

    public static void register(EntityRenderer renderer) {
        renderers.add(renderer);
    }
}
