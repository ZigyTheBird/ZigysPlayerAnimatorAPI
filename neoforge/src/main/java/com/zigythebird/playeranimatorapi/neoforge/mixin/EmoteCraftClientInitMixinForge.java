package com.zigythebird.playeranimatorapi.neoforge.mixin;

import com.zigythebird.playeranimatorapi.playeranims.CustomModifierLayer;
import com.zigythebird.playeranimatorapi.playeranims.PlayerAnimations;
import io.github.kosmx.emotes.neoforge.ClientInit;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import javax.annotation.Nullable;

@Mixin(ClientInit.class)
public class EmoteCraftClientInitMixinForge {
    @Redirect(method = "lambda$initKeyBinding$1", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;setScreen(Lnet/minecraft/client/gui/screens/Screen;)V"), remap = false)
    private static void initKeybinding(Minecraft instance, @Nullable Screen guiScreen) {
        CustomModifierLayer layer = PlayerAnimations.getModifierLayer(instance.player);
        if (instance.player != null && layer.isActive() && layer.data.priority() > 1000) {
            instance.player.displayClientMessage(Component.translatable("warn.playeranimatorapi.cannotEmote"), true);
        }
    }
}
