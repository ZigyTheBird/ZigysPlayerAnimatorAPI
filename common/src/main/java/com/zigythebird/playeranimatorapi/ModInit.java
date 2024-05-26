package com.zigythebird.playeranimatorapi;

import com.mojang.logging.LogUtils;
import com.zigythebird.multiloaderutils.MultiloaderUtils;
import com.zigythebird.multiloaderutils.utils.Platform;
import org.slf4j.Logger;
import com.zigythebird.playeranimatorapi.azure.ModAzureUtils;
public class ModInit {
    public static final String MOD_ID = "playeranimatorapi";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static void init() {
        MultiloaderUtils.forceClientToHaveMod(MOD_ID, Platform.getModVersion(MOD_ID));

        if (Platform.isModLoaded("azurelib", "mod.azure.azurelib.common.internal.common.AzureLib")) {
            ModAzureUtils.init();
        }
    }
}
