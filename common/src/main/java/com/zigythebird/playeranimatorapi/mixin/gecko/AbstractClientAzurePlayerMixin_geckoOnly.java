package com.zigythebird.playeranimatorapi.mixin.gecko;

import com.zigythebird.playeranimatorapi.gecko.AnimatablePlayerLayer;
import com.zigythebird.playeranimatorapi.misc.GeckoPlayerInterface;
import net.minecraft.client.player.AbstractClientPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(AbstractClientPlayer.class)
public class AbstractClientAzurePlayerMixin_geckoOnly implements GeckoPlayerInterface {

    @Unique
    private final AnimatablePlayerLayer playeranimatorapi$animatablePlayerLayer = new AnimatablePlayerLayer((AbstractClientPlayer) (Object)(this));

    @Override
    public AnimatablePlayerLayer playeranimatorapi$getAnimatablePlayerLayer() {
        return playeranimatorapi$animatablePlayerLayer;
    }
}
