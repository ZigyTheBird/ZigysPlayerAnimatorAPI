package com.zigythebird.playeranimatorapi.playeranims;

import com.zigythebird.playeranimatorapi.data.PlayerAnimationData;
import dev.kosmx.playerAnim.api.IPlayable;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Environment(EnvType.CLIENT)
public class ConditionalAnimations {

    private static Map<String, Function<PlayerAnimationData, ResourceLocation>> perModConditions = new HashMap<>();

    public static void addModConditions(String namespace, Function<PlayerAnimationData, ResourceLocation> function) {
        perModConditions.put(namespace, function);
    }

    public static ResourceLocation getAnimationForCurrentConditions(PlayerAnimationData data) {

        if (perModConditions.containsKey(data.animationID().getNamespace())) {
            return perModConditions.get(data.animationID().getNamespace()).apply(data);
        }

        return data.animationID();
    }
}
