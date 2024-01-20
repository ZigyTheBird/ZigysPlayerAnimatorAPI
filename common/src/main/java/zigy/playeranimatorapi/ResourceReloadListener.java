package zigy.playeranimatorapi;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.kosmx.playerAnim.core.data.gson.AnimationSerializing;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.util.GsonHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import zigy.playeranimatorapi.playeranims.PlayerAnimations;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;

public class ResourceReloadListener implements ResourceManagerReloadListener {

    private static final Logger logger = LogManager.getLogger(PlayerAnimatorAPIMod.class);

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
        PlayerAnimations.animLengthsMap = new HashMap<>();
        for (var resource : resourceManager.listResources("player_animation", location -> location.getPath().endsWith(".json")).entrySet()) {
            try {
                JsonObject jsonObject = GsonHelper.convertToJsonObject(JsonParser.parseReader(resource.getValue().openAsReader()), "resource");
                if (jsonObject.has("animations")) {
                    for (var object : jsonObject.get("animations").getAsJsonObject().asMap().entrySet()) {
                        PlayerAnimations.animLengthsMap.put(new ResourceLocation(resource.getKey().getNamespace(), object.getKey().toLowerCase(Locale.ROOT)), object.getValue().getAsJsonObject().get("animation_length").getAsFloat());
                    }
                } else {
                    try (var input = resource.getValue().open()) {

                        //Deserialize the animation json. GeckoLib animation json can contain multiple animations.
                        for (var animation : AnimationSerializing.deserializeAnimation(input)) {

                            //Save the animation for later use.
                            PlayerAnimations.animLengthsMap.put(new ResourceLocation(resource.getKey().getNamespace(), PlayerAnimationRegistry.serializeTextToString((String) animation.extraData.get("name")).toLowerCase(Locale.ROOT)), (float) (animation.endTick / 20));
                        }
                    }
                }
            } catch (IOException | NullPointerException e) {
                logger.warn("Could not load animation resource " + resource.getKey().toString() + " " + e);
            }
        }
    }
}
