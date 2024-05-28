package com.zigythebird.playeranimatorapi.mixin;

import com.zigythebird.playeranimatorapi.azure.AnimatablePlayerLayer;
import com.zigythebird.playeranimatorapi.misc.PlayerInterface;
import net.minecraft.client.player.AbstractClientPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(AbstractClientPlayer.class)
public class AbstractClientPlayerMixin_azureOnly implements PlayerInterface {

    @Unique
    private final AnimatablePlayerLayer playeranimatorapi$animatablePlayerLayer = new AnimatablePlayerLayer((AbstractClientPlayer) (Object)(this));

    @Override
    public AnimatablePlayerLayer playeranimatorapi$getAnimatablePlayerLayer() {
        return playeranimatorapi$animatablePlayerLayer;
    }
}
