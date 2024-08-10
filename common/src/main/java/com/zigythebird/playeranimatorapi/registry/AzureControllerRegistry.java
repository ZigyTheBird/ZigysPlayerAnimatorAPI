package com.zigythebird.playeranimatorapi.registry;

import com.zigythebird.playeranimatorapi.ModInit;
import com.zigythebird.playeranimatorapi.azure.AnimatablePlayerLayer;
import com.zigythebird.playeranimatorapi.playeranims.CustomModifierLayer;
import mod.azure.azurelib.common.internal.common.core.animation.AnimationController;
import mod.azure.azurelib.common.internal.common.core.animation.AnimationState;
import mod.azure.azurelib.common.internal.common.core.object.PlayState;
import net.minecraft.core.NonNullList;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class AzureControllerRegistry {
    private static final Map<String, NonNullList<BiFunction<AnimationState<AnimatablePlayerLayer>, CustomModifierLayer<?>, PlayState>>> CONTROLLERS = new HashMap<>();

    public static AnimationController.AnimationStateHandler<AnimatablePlayerLayer> getControllerForMod(int i, CustomModifierLayer<?> layer) {
        try {
            if (layer.isActive()) {
                String modID = layer.data.animationID().getNamespace();
                return (state) -> CONTROLLERS.get(modID).get(i).apply(state, layer);
            }
            else {
                return (state) -> PlayState.CONTINUE;
            }
        }
        catch (Exception e) {
            return (state) -> PlayState.CONTINUE;
        }
    }

    public static void addController(String modID, BiFunction<AnimationState<AnimatablePlayerLayer>, CustomModifierLayer<?>, PlayState> controller) {
        if (!CONTROLLERS.containsKey(modID)) {
            CONTROLLERS.put(modID, NonNullList.createWithCapacity(5));
        }
        if (CONTROLLERS.get(modID).size() >= 5) {
            ModInit.LOGGER.error("Cannot register new gecko controller for mod " + modID + " due to a controller count limit of 5.");
            return;
        }
        CONTROLLERS.get(modID).add(controller);
    }
}
