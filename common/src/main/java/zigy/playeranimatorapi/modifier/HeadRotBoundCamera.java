package zigy.playeranimatorapi.modifier;

import dev.kosmx.playerAnim.api.TransformType;
import dev.kosmx.playerAnim.core.util.Vec3f;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Camera;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.PlayerModelPart;
import org.jetbrains.annotations.NotNull;
import zigy.playeranimatorapi.playeranims.CustomModifierLayer;

public class HeadRotBoundCamera extends AbstractCameraModifier {

    private final CustomModifierLayer layer;

    public HeadRotBoundCamera(CustomModifierLayer layer) {
        super();
        this.layer = layer;
    }

    public @NotNull Vec3f get3DCameraTransform(GameRenderer renderer, Camera camera, TransformType type, float tickDelta, @NotNull Vec3f value0) {
        AbstractClientPlayer player = Minecraft.getInstance().player;
        float f = player.yBodyRot;
        float g = player.yHeadRot;
        float netHeadYaw = g - f;
        float i;
        if (player.isPassenger() && player.getVehicle() instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity)player.getVehicle();
            f = player.yBodyRot;
            netHeadYaw = g - f;
            i = Mth.wrapDegrees(netHeadYaw);
            if (i < -85.0F) {
                i = -85.0F;
            }

            if (i >= 85.0F) {
                i = 85.0F;
            }

            f = g - i;
            if (i * i > 2500.0F) {
                f += i * 0.2F;
            }

            netHeadYaw = g - f;
        }
        float headPitch = player.getXRot();
        if (isEntityUpsideDown(player)) {
            headPitch *= -1.0F;
            netHeadYaw *= -1.0F;
        }
        if (type == TransformType.ROTATION && Minecraft.getInstance().options.getCameraType() == CameraType.FIRST_PERSON) {
            value0 = layer.get3DTransform("head", TransformType.ROTATION, tickDelta, value0).add(new Vec3f(0, Mth.wrapDegrees(player.getYRot() - netHeadYaw) * 0.017453292F, 0));
        }
        return value0;
    }

    public static boolean isEntityUpsideDown(LivingEntity entity) {
        if (entity instanceof Player || entity.hasCustomName()) {
            String string = ChatFormatting.stripFormatting(entity.getName().getString());
            if ("Dinnerbone".equals(string) || "Grumm".equals(string)) {
                return !(entity instanceof Player) || ((Player)entity).isModelPartShown(PlayerModelPart.CAPE);
            }
        }

        return false;
    }
}
