package zigy.playeranimatorapi;

import com.google.common.collect.ImmutableMap;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import zigy.zigysmultiloaderutils.utils.Platform;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public class ModMixinPlugin implements IMixinConfigPlugin {

    private static final Supplier<Boolean> TRUE = () -> true;

    private static final Map<String, Supplier<Boolean>> CONDITIONS = ImmutableMap.of(
            "zigy.playeranimatorapi.mixin.PlayerAzureMixin", () -> Platform.isModLoaded("azurelib", "mod.azure.azurelib.AzureLib"),
            "zigy.playeranimatorapi.mixin.LivingEntityRendererAzureMixin", () -> Platform.isModLoaded("azurelib", "mod.azure.azurelib.AzureLib"),
            "zigy.playeranimatorapi.mixin.LivingEntityRendererMixin", () -> !Platform.isModLoaded("azurelib", "mod.azure.azurelib.AzureLib")
    );

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        return CONDITIONS.getOrDefault(mixinClassName, TRUE).get();
    }

    //Boilerplate

    @Override
    public void onLoad(String mixinPackage) {
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }
}
