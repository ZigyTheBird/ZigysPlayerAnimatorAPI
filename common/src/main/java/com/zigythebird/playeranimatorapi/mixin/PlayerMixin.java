package com.zigythebird.playeranimatorapi.mixin;

import com.zigythebird.playeranimatorapi.data.PlayerAnimationData;
import com.zigythebird.playeranimatorapi.events.ClientPlayerTickEvent;
import com.zigythebird.playeranimatorapi.misc.PlayerInterface;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import dev.kosmx.playerAnim.core.util.Pair;

@Mixin(Player.class)
public abstract class PlayerMixin implements PlayerInterface {
    @Unique
    private Pair<Integer, PlayerAnimationData> paapi$lastAnim;

    @Inject(method = "tick", at = @At("HEAD"))
    private void inject(CallbackInfo ci) {
        ClientPlayerTickEvent.tick((Player) (Object) this);
    }

    public void setLastAnim(PlayerAnimationData data) {
        if (data == null) {
            paapi$lastAnim = null;
            return;
        }
        paapi$lastAnim = new Pair<>(((Player)(Object)this).tickCount, data);
    }

    public Pair<Integer, PlayerAnimationData> paapi$getLastAnim() {
        return paapi$lastAnim;
    }
}