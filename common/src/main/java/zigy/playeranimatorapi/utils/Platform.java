package zigy.playeranimatorapi.utils;

import dev.architectury.injectables.annotations.ExpectPlatform;
import org.apache.commons.lang3.NotImplementedException;

public class Platform {

    @ExpectPlatform
    public static boolean isModLoaded(String modID) {
        throw new NotImplementedException();
    }
}
