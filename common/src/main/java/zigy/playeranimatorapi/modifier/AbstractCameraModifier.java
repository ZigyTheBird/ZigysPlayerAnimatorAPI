package zigy.playeranimatorapi.modifier;

import dev.kosmx.playerAnim.api.layered.modifier.AbstractModifier;
import dev.kosmx.playerAnim.core.util.Vec3f;
import org.jetbrains.annotations.NotNull;

/**Do not touch not done.*/
public abstract class AbstractCameraModifier extends AbstractModifier {
    public AbstractCameraModifier() {
        super();
    }

    public @NotNull Vec3f get3DCameraRotation(float tickDelta, @NotNull Vec3f value0) {
        return value0;
    }
}
