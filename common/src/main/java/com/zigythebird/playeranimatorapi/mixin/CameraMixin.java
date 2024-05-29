package com.zigythebird.playeranimatorapi.mixin;

import com.zigythebird.playeranimatorapi.misc.CameraInterface;
import net.minecraft.client.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Camera.class)
public class CameraMixin implements CameraInterface {

    @Shadow private float xRot;

    @Shadow private float yRot;

    @Override
    public void PAAPI$setAnglesInternal(float xRot, float yRot) {
        this.xRot = xRot;
        this.yRot = yRot;
    }
}
