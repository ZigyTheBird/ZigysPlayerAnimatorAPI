package zigy.playeranimatorapi;

import com.mojang.logging.LogUtils;
import org.slf4j.Logger;
import zigy.playeranimatorapi.azure.ModAzureUtils;
import zigy.playeranimatorapi.misc.ModEntityDataSerializers;
import zigy.zigysmultiloaderutils.MultiloaderUtils;
import zigy.zigysmultiloaderutils.utils.Platform;

public class ModInit {
    public static final String MOD_ID = "playeranimatorapi";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static void init() {
        ModEntityDataSerializers.init();
        MultiloaderUtils.forceClientToHaveMod(MOD_ID, Platform.getModVersion(MOD_ID));

        if (Platform.isModLoaded("azurelib", "mod.azure.azurelib.AzureLib")) {
            ModAzureUtils.init();
        }
    }
}
