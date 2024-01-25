package zigy.playeranimatorapi.utils.forge;

import net.minecraftforge.fml.ModList;
import org.spongepowered.asm.service.MixinService;

import java.io.IOException;
import java.util.Objects;

public class PlatformImpl {
    public static boolean isModLoaded(String modID) {
        if (ModList.get() != null) {
            return ModList.get().isLoaded(modID);
        } else {
            if (Objects.equals(modID, "azurelib")) {
                return hasClass("mod.azure.azurelib.AzureLib");
            }
            if (Objects.equals(modID, "emotecraft")) {
                return hasClass("io.github.kosmx.emotes.forge.ForgeWrapper");
            }
        }
        return false;
    }

    private static boolean hasClass(String name) {
        try {
            // This does *not* load the class!
            MixinService.getService().getBytecodeProvider().getClassNode(name);
            return true;
        } catch (ClassNotFoundException | IOException e) {
            return false;
        }
    }
}
