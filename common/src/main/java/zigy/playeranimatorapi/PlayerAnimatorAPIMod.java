package zigy.playeranimatorapi;

import com.mojang.logging.LogUtils;
import org.slf4j.Logger;
import zigy.playeranimatorapi.azure.ModAzureUtils;
import zigy.playeranimatorapi.misc.ModEntityDataSerializers;
import zigy.playeranimatorapi.utils.Platform;

public class PlayerAnimatorAPIMod {
    public static final String MOD_ID = "playeranimatorapi";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static void init() {
        ModEntityDataSerializers.init();

        if (Platform.isModLoaded("azurelib")) {
            ModAzureUtils.init();
        }
    }
}
