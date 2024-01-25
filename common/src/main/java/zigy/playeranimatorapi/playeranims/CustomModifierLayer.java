package zigy.playeranimatorapi.playeranims;

import dev.kosmx.playerAnim.api.TransformType;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.api.layered.modifier.AbstractFadeModifier;
import dev.kosmx.playerAnim.api.layered.modifier.AbstractModifier;
import dev.kosmx.playerAnim.core.util.Vec3f;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import zigy.playeranimatorapi.data.PlayerAnimationData;
import zigy.playeranimatorapi.modifier.CameraModifier;

@Environment(EnvType.CLIENT)
public class CustomModifierLayer<T extends IAnimation> extends ModifierLayer implements IAnimation {

    public static final Vec3f voidVector = new Vec3f(0, -10000000, 0);

    public int tick;
    public int modifierCount = 0;
    public boolean hasModifier;
    public boolean cameraAnimEnabled;

    public boolean important = false;
    public PlayerAnimationData data;
    public ResourceLocation currentAnim;
    public KeyframeAnimationPlayer animPlayer;
    public AbstractClientPlayer player;

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
        tick += 1;
    }

    public void addModifier(@NotNull AbstractModifier modifier) {
        this.addModifier(modifier, modifierCount);
        modifierCount += 1;
        hasModifier = true;
        if (modifier instanceof CameraModifier) {
            cameraAnimEnabled = true;
        }
    }

    public void removeAllModifiers() {
        for (int i = modifierCount - 1; i >= 0; i--) {
            this.removeModifier(i);
        }
        modifierCount = 0;
        cameraAnimEnabled = false;
        hasModifier = false;
    }

    public void replaceAnimationWithFade(AbstractFadeModifier fadeModifier, KeyframeAnimationPlayer newAnimation) {
        tick = 0;
        setAnimPlayer(newAnimation);
        replaceAnimationWithFade(fadeModifier, newAnimation, false);
    }

    public void replaceAnimation(KeyframeAnimationPlayer newAnimation) {
        tick = 0;
        setAnimPlayer(newAnimation);
        this.setAnimation(newAnimation);
        this.linkModifiers();
    }

    @Override
    public @NotNull Vec3f get3DTransform(@NotNull String modelName, @NotNull TransformType type, float tickDelta, @NotNull Vec3f value0) {
        Vec3f transform = super.get3DTransform(modelName, type, tickDelta, value0);
//        if (type == TransformType.ROTATION && modelName.equals("body")) {
//            transform = transform.add(new Vec3f(0, (float) (-playerLookRightWayRot(player, tickDelta) * 0.017453292), 0));
//        }
        if (type == TransformType.POSITION && ((modelName.equals("leftItem") && !this.data.parts().leftItem.isVisible) || (modelName.equals("rightItem") && !this.data.parts().rightItem.isVisible))) {
            transform = transform.add(voidVector);
        }
        return transform;
    }

//    public static double playerLookRightWayRot(AbstractClientPlayer player, float tickDelta) {
//        float f = Mth.rotLerp(tickDelta, player.yBodyRotO, player.yBodyRot);
//        float g = Mth.rotLerp(tickDelta, player.yHeadRotO, player.yHeadRot);
//        float h = g - f;
//        float i;
//        if (player.isPassenger() && player.getVehicle() instanceof LivingEntity) {
//            LivingEntity livingEntity = (LivingEntity) player.getVehicle();
//            f = Mth.rotLerp(tickDelta, livingEntity.yBodyRotO, livingEntity.yBodyRot);
//            h = g - f;
//            i = Mth.wrapDegrees(h);
//            if (i < -85.0F) {
//                i = -85.0F;
//            }
//
//            if (i >= 85.0F) {
//                i = 85.0F;
//            }
//
//            f = g - i;
//            if (i * i > 2500.0F) {
//                f += i * 0.2F;
//            }
//
//            h = g - f;
//        }
//
//        if (isEntityUpsideDown(player)) {
//            h *= -1.0F;
//        }
//
//        return h;
//    }

    public CustomModifierLayer(AbstractClientPlayer player) {
        this(null, player);
    }
}