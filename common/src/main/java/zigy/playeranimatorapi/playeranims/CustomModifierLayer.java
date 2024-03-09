package zigy.playeranimatorapi.playeranims;

import dev.kosmx.playerAnim.api.TransformType;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.api.layered.modifier.AbstractFadeModifier;
import dev.kosmx.playerAnim.api.layered.modifier.AbstractModifier;
import dev.kosmx.playerAnim.api.layered.modifier.SpeedModifier;
import dev.kosmx.playerAnim.core.util.Vec3f;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import zigy.playeranimatorapi.data.PlayerAnimationData;
import zigy.playeranimatorapi.modifier.AbstractCameraModifier;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public class CustomModifierLayer<T extends IAnimation> extends ModifierLayer implements IAnimation {

    public static final Vec3f voidVector = new Vec3f(0, -10000000, 0);

    public List<AbstractCameraModifier> cameraModifiers = new ArrayList<>();

    public int modifierCount = 0;
    public boolean hasModifier;
    public boolean cameraAnimEnabled;

    public boolean important = false;
    public PlayerAnimationData data;
    public ResourceLocation currentAnim;
    public KeyframeAnimationPlayer animPlayer;
    public AbstractClientPlayer player;
    private float speed = 1;

    public void setAnimationData(PlayerAnimationData data) {
        this.data = data;
        this.important = data.important();
    }

    public void setAnimPlayer(KeyframeAnimationPlayer animPlayer) {
        this.animPlayer = animPlayer;
    }

    public void setCurrentAnimationLocation(ResourceLocation animation) {
        this.currentAnim = animation;
    }

    public CustomModifierLayer(@Nullable T animation, AbstractClientPlayer player, AbstractModifier... modifiers) {
        hasModifier = false;
        this.player = player;
    }

    @Override
    public void tick() {
        super.tick();
    }

    public void addModifier(@NotNull AbstractModifier modifier) {
        this.addModifier(modifier, modifierCount);
        modifierCount += 1;
        hasModifier = true;
        if (modifier instanceof AbstractCameraModifier) {
            cameraAnimEnabled = true;
            this.cameraModifiers.add((AbstractCameraModifier) modifier);
        }
        if (modifier instanceof SpeedModifier) {
            speed *= ((SpeedModifier)modifier).speed;
        }
    }

    public void removeAllModifiers() {
        for (int i = modifierCount - 1; i >= 0; i--) {
            this.removeModifier(i);
        }
        modifierCount = 0;
        cameraAnimEnabled = false;
        hasModifier = false;
        speed = 1;
        this.cameraModifiers= new ArrayList<>();
    }

    public void replaceAnimationWithFade(AbstractFadeModifier fadeModifier, KeyframeAnimationPlayer newAnimation) {
        setAnimPlayer(newAnimation);
        replaceAnimationWithFade(fadeModifier, newAnimation, false);
    }

    public void replaceAnimation(KeyframeAnimationPlayer newAnimation) {
        setAnimPlayer(newAnimation);
        this.setAnimation(newAnimation);
        this.linkModifiers();
    }

    @Override
    public @NotNull Vec3f get3DTransform(@NotNull String modelName, @NotNull TransformType type, float tickDelta, @NotNull Vec3f value0) {
        Vec3f transform = super.get3DTransform(modelName, type, tickDelta, value0);
        if (type == TransformType.POSITION && ((modelName.equals("leftItem") && !this.data.parts().leftItem.isVisible) || (modelName.equals("rightItem") && !this.data.parts().rightItem.isVisible))) {
            transform = transform.add(voidVector);
        }
        return transform;
    }

    public float getSpeed() {
        return speed;
    }

    public CustomModifierLayer(AbstractClientPlayer player) {
        this(null, player);
    }
}