package com.zigythebird.playeranimatorapi.mixin.azure;

import com.zigythebird.playeranimatorapi.azure.AnimatablePlayerLayer;
import com.zigythebird.playeranimatorapi.misc.AzurePlayerInterface;
import net.minecraft.client.player.AbstractClientPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(AbstractClientPlayer.class)
public class AbstractClientAzurePlayerMixin_azureOnly implements AzurePlayerInterface {

    @Unique
    private final AnimatablePlayerLayer playeranimatorapi$animatablePlayerLayer = new AnimatablePlayerLayer((AbstractClientPlayer) (Object)(this));

    @Override
    public AnimatablePlayerLayer playeranimatorapi$getAnimatablePlayerLayer() {
        return playeranimatorapi$animatablePlayerLayer;
    }
}
