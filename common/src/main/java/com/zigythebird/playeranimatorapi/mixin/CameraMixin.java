package com.zigythebird.playeranimatorapi.mixin;

import com.zigythebird.playeranimatorapi.utils.CameraUtils;
import dev.kosmx.playerAnim.core.util.Vec3f;
import net.minecraft.client.Camera;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public abstract class CameraMixin{

    @Shadow private float xRot;

    @Shadow private float yRot;

    @Shadow @Final private Quaternionf rotation;

    @Shadow @Final private static Vector3f FORWARDS;

    @Shadow @Final private static Vector3f UP;

    @Shadow @Final private static Vector3f LEFT;

    @Shadow @Final private Vector3f left;

    @Shadow @Final private Vector3f up;

    @Shadow @Final private Vector3f forwards;

    @Shadow public abstract Vector3f getLookVector();

    @Shadow public abstract Vector3f getUpVector();

    @Shadow public abstract Vector3f getLeftVector();

    @Shadow public abstract Vec3 getPosition();

    @Shadow protected abstract void setPosition(double x, double y, double z);

    @Inject(method = "setup", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Camera;setPosition(DDD)V", shift = At.Shift.AFTER))
    private void computeCameraAngles(BlockGetter level, Entity entity, boolean detached, boolean thirdPersonReverse, float partialTick, CallbackInfo ci) {
        Vec3f vec = CameraUtils.computeCameraAngles(((Camera)(Object)this), partialTick);
        if (vec != null) {
            this.zigysPlayerAnimatorAPI$setRotation(vec.getY(), vec.getX(), vec.getZ());
        }
        Vec3f transform = CameraUtils.computeCameraLocation(((Camera)(Object)this), partialTick);
        if (transform != null) {
            Vector3f forwards = this.getLookVector();
            Vector3f up = this.getUpVector();
            Vector3f left = this.getLeftVector();
            double d = (double)forwards.x() * transform.getX() + (double)up.x() * transform.getY() + (double)left.x() * transform.getZ();
            double e = (double)forwards.y() * transform.getX() + (double)up.y() * transform.getY() + (double)left.y() * transform.getZ();
            double f = (double)forwards.z() * transform.getX() + (double)up.z() * transform.getY() + (double)left.z() * transform.getZ();
            this.setPosition(this.getPosition().x + f, this.getPosition().y - e, this.getPosition().z - d);
        }
    }

    @Unique
    protected void zigysPlayerAnimatorAPI$setRotation(float f, float g, float roll) {
        this.xRot = g;
        this.yRot = f;
        this.rotation.rotationYXZ(3.1415927F - f * 0.017453292F, -g * 0.017453292F, -roll * 0.017453292F);
        FORWARDS.rotate(this.rotation, this.forwards);
        UP.rotate(this.rotation, this.up);
        LEFT.rotate(this.rotation, this.left);
    }
}
