package zigy.playeranimatorapi.forge.mixin;

import io.github.kosmx.emotes.forge.ClientInit;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import zigy.playeranimatorapi.PlayerAnimatorAPIMod;
import zigy.playeranimatorapi.playeranims.PlayerAnimations;

import javax.annotation.Nullable;


@Mixin(ClientInit.class)
public class EmoteCraftClientInitMixinForge {

    private static final ResourceLocation animationLayerId = new ResourceLocation(PlayerAnimatorAPIMod.MOD_ID, "factory");

    @Redirect(method = "lambda$initKeyBinding$2", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;setScreen(Lnet/minecraft/client/gui/screens/Screen;)V"))
    private static void initKeybinding(Minecraft instance, @Nullable Screen guiScreen) {
        if (instance.player != null && PlayerAnimations.getModifierLayer(instance.player).isActive() && PlayerAnimations.getModifierLayer(instance.player).important) {
            instance.player.displayClientMessage(Component.translatable("warn.playeranimatorapi.cannotEmote"), true);
        }
    }
}
