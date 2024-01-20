package zigy.playeranimatorapi.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import zigy.playeranimatorapi.misc.IsVisibleAccessor;

@Mixin(ModelPart.class)
public abstract class ModelPartMixin implements IsVisibleAccessor {

    @Override
    public void zigysPlayerAnimatorAPI$setIsVisible(Boolean value) {
        this.zigysPlayerAnimatorAPI$isVisible = value;
    }

    @Override
    public boolean zigysPlayerAnimatorAPI$getIsVisible() {
        return this.zigysPlayerAnimatorAPI$isVisible;
    }

    @Unique
    public boolean zigysPlayerAnimatorAPI$isVisible = true;

    @Inject(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;IIFFFF)V", at = @At("HEAD"))
    private void render(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha, CallbackInfo ci) {
        if (!this.zigysPlayerAnimatorAPI$isVisible) {
            return;
        }
    }
}
