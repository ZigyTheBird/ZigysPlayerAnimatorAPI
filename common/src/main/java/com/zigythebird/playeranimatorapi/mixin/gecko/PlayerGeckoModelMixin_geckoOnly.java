package com.zigythebird.playeranimatorapi.mixin.gecko;

import com.zigythebird.playeranimatorapi.gecko.PlayerAnimationModel;
import com.zigythebird.playeranimatorapi.gecko.PlayerAnimationRenderer;
import com.zigythebird.playeranimatorapi.misc.GeckoPlayerInterface;
import com.zigythebird.playeranimatorapi.misc.GetGeckoModelRendererInterface;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerModel.class)
public class PlayerGeckoModelMixin_geckoOnly<T extends LivingEntity> implements GetGeckoModelRendererInterface {

    @Unique
    private PlayerAnimationRenderer zigysPlayerAnimatorAPI$renderer = null;

    @Inject(method = "setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V", at = @At("TAIL"))
    private void inject(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
        PlayerAnimationRenderer renderer = playeranimatorapi$getRenderer();
        if (renderer != null && entity instanceof Player && entity.equals(Minecraft.getInstance().player) && ((PlayerAnimationModel)renderer.getGeoModel()).allResourcesExist(((GeckoPlayerInterface) entity).playeranimatorapi$getAnimatablePlayerLayer())) {
            renderer.setupAnim(renderer.getGeoModel().getBakedModel(renderer.getGeoModel().getModelResource(((GeckoPlayerInterface) entity).playeranimatorapi$getAnimatablePlayerLayer())));
        }
    }

    @Override
    public PlayerAnimationRenderer playeranimatorapi$getRenderer() {
        return zigysPlayerAnimatorAPI$renderer;
    }

    @Override
    public void playeranimatorapi$setRenderer(PlayerAnimationRenderer renderer) {
        zigysPlayerAnimatorAPI$renderer = renderer;
    }
}
