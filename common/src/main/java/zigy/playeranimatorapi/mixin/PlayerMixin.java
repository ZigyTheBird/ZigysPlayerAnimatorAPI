package zigy.playeranimatorapi.mixin;

import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import zigy.playeranimatorapi.events.ClientPlayerTickEvent;

@Mixin(Player.class)
public abstract class PlayerMixin {

    @Inject(method = "tick", at = @At("HEAD"))
    private void inject(CallbackInfo ci) {
        ClientPlayerTickEvent.tick((Player) (Object) this);
    }
}
