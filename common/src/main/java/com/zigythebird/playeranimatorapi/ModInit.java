package com.zigythebird.playeranimatorapi;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;

public class ModInit {
    public static final String MOD_ID = "playeranimatorapi";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final ResourceLocation altStopPlayerAnimationPacket = ResourceLocation.fromNamespaceAndPath(MOD_ID, "alt_stop_player_animation_packet");
    public static final ResourceLocation altPlayPlayerAnimationPacket = ResourceLocation.fromNamespaceAndPath(MOD_ID, "alt_play_player_animation_packet");

    public static void init() {}
}
