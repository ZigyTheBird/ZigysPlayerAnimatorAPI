package zigy.playeranimatorapi.playeranims;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import dev.kosmx.playerAnim.api.firstPerson.FirstPersonMode;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.modifier.AbstractFadeModifier;
import dev.kosmx.playerAnim.api.layered.modifier.MirrorModifier;
import dev.kosmx.playerAnim.api.layered.modifier.SpeedModifier;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.core.util.Ease;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationFactory;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import zigy.playeranimatorapi.PlayerAnimatorAPIMod;
import zigy.playeranimatorapi.data.PlayerAnimationData;
import zigy.playeranimatorapi.data.PlayerParts;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Environment(EnvType.CLIENT)
public class PlayerAnimations {
    private static final Logger logger = LogManager.getLogger(PlayerAnimatorAPIMod.class);

    public static Gson gson = new GsonBuilder().setLenient().serializeNulls().create();

    public static Map<ResourceLocation, Float> animLengthsMap;
    public static List<String> extensions = List.of(new String[]{"crouch", "crawl", "swim"});

    public static final ResourceLocation animationLayerId = new ResourceLocation(PlayerAnimatorAPIMod.MOD_ID, "factory");

    public static void init() {
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(
                animationLayerId,
                42,
                player -> new CustomModifierLayer(player)
        );
    }

    public static void stopAnimation(UUID playerUUID, ResourceLocation animationID) {
        stopAnimation((AbstractClientPlayer) Minecraft.getInstance().level.getPlayerByUUID(playerUUID), animationID);
    }

    public static void stopAnimation(AbstractClientPlayer player, ResourceLocation animationID) {
        CustomModifierLayer animationContainer = (CustomModifierLayer) PlayerAnimationAccess.getPlayerAssociatedData(player).get(animationLayerId);

        if (animationContainer != null && animationContainer.isActive() && animationContainer.data.animationID().equals(animationID)) {
            animationContainer.animPlayer.stop();
        }
    }

    public static void receivePacket(String jsonData) {
        PlayerAnimationData data = PlayerAnimationData.CODEC.parse(JsonOps.INSTANCE, gson.fromJson(jsonData, JsonElement.class)).getOrThrow(true, logger::warn);
        AbstractClientPlayer player = (AbstractClientPlayer) Minecraft.getInstance().level.getPlayerByUUID(data.playerUUID());
        playAnimation(player, data);
    }

    public static void playAnimation(AbstractClientPlayer player, PlayerAnimationData data) {
        playAnimation(player, data, true);
    }

    public static void playAnimation(AbstractClientPlayer player, PlayerAnimationData data, boolean replaceTick) {
        playAnimation(player, data, data.parts(), data.fadeLength(), data.desiredLength(), data.easeID(),
                data.firstPersonEnabled(), data.shouldMirror(), replaceTick);
    }

    public static void playAnimation(AbstractClientPlayer player, PlayerAnimationData data, PlayerParts parts, int fadeLength,
                                     float desiredLength, int easeID, boolean firstPersonEnabled, boolean shouldMirror, boolean replaceTick) {
        try {
            CustomModifierLayer animationContainer = (CustomModifierLayer) PlayerAnimationAccess.getPlayerAssociatedData(player).get(animationLayerId);

            ResourceLocation normalAnimationID = data.animationID();
            String[] split = (normalAnimationID.getPath().split("_"));

            if (normalAnimationID.toString().equals("null:null") || extensions.contains(split[split.length -1])) {
                return;
            }

            ResourceLocation crouchedAnimationID = data.animationID().withPath(data.animationID().getPath() + "_crouch");
            ResourceLocation crawlingAnimationID = data.animationID().withPath(data.animationID().getPath() + "_crawl");
            ResourceLocation swimmingAnimationID = data.animationID().withPath(data.animationID().getPath() + "_swim");

            Map<ResourceLocation, KeyframeAnimation> animations = PlayerAnimationRegistry.getAnimations();

            if (!animations.containsKey(crouchedAnimationID)) {
                crouchedAnimationID = null;
            }
            if (!animations.containsKey(crawlingAnimationID)) {
                crawlingAnimationID = null;
            }
            if (!animations.containsKey(swimmingAnimationID)) {
                swimmingAnimationID = null;
            }

            if (animationContainer != null) {

                animationContainer.setAnimationData(data);

                if (fadeLength < 0) {
                    fadeLength = 0;
                }

                ResourceLocation animationID = normalAnimationID;
                if (player.isCrouching() && crouchedAnimationID != null) {
                    animationID = crouchedAnimationID;
                } else if (player.isVisuallyCrawling() && crawlingAnimationID != null) {
                    animationID = crawlingAnimationID;
                } else if (player.isVisuallySwimming() && swimmingAnimationID != null) {
                    animationID = swimmingAnimationID;
                }

                animationContainer.setCurrentAnimationLocation(animationID);

                KeyframeAnimation anim = PlayerAnimationRegistry.getAnimation(animationID);

                if (replaceTick) {
                    animationContainer.removeAllModifiers();

                    if (desiredLength > 0) {
                        float speed = animLengthsMap.get(animationID) / desiredLength;
                        SpeedModifier speedModifier = new SpeedModifier(speed);
                        animationContainer.addModifier(speedModifier);
                        animationContainer.setSpeedModifier(1);
                    }
                    else {
                        animationContainer.setSpeedModifier(1);
                    }

                    if (shouldMirror && player == Minecraft.getInstance().player && Minecraft.getInstance().options.mainHand().get().getId() == 0) {
                        MirrorModifier mirrorModifier = new MirrorModifier();
                        animationContainer.addModifier(mirrorModifier);
                    }
                }

                var builder = anim.mutableCopy();

                var body = builder.getPart("body");
                body.x.setEnabled(parts.body.x);
                body.y.setEnabled(parts.body.y);
                body.z.setEnabled(parts.body.z);
                body.pitch.setEnabled(parts.body.pitch);
                body.yaw.setEnabled(parts.body.yaw);
                body.roll.setEnabled(parts.body.roll);

                var head = builder.getPart("head");
                head.x.setEnabled(parts.head.x);
                head.y.setEnabled(parts.head.y);
                head.z.setEnabled(parts.head.z);
                head.pitch.setEnabled(parts.head.pitch);
                head.yaw.setEnabled(parts.head.yaw);
                head.roll.setEnabled(parts.head.roll);

                var torso = builder.getPart("torso");
                torso.x.setEnabled(parts.torso.x);
                torso.y.setEnabled(parts.torso.y);
                torso.z.setEnabled(parts.torso.z);
                torso.pitch.setEnabled(parts.torso.pitch);
                torso.yaw.setEnabled(parts.torso.yaw);
                torso.roll.setEnabled(parts.torso.roll);

                var rightArm = builder.getPart("rightArm");
                rightArm.x.setEnabled(parts.rightArm.x);
                rightArm.y.setEnabled(parts.rightArm.y);
                rightArm.z.setEnabled(parts.rightArm.z);
                rightArm.pitch.setEnabled(parts.rightArm.pitch);
                rightArm.yaw.setEnabled(parts.rightArm.yaw);
                rightArm.roll.setEnabled(parts.rightArm.roll);
                rightArm.bend.setEnabled(parts.rightArm.bend);
                rightArm.bendDirection.setEnabled(parts.rightArm.bendDirection);

                var leftArm = builder.getPart("leftArm");
                leftArm.x.setEnabled(parts.leftArm.x);
                leftArm.y.setEnabled(parts.leftArm.y);
                leftArm.z.setEnabled(parts.leftArm.z);
                leftArm.pitch.setEnabled(parts.leftArm.pitch);
                leftArm.yaw.setEnabled(parts.leftArm.yaw);
                leftArm.roll.setEnabled(parts.leftArm.roll);
                leftArm.bend.setEnabled(parts.leftArm.bend);
                leftArm.bendDirection.setEnabled(parts.leftArm.bendDirection);

                var rightLeg = builder.getPart("rightLeg");
                rightLeg.x.setEnabled(parts.rightLeg.x);
                rightLeg.y.setEnabled(parts.rightLeg.y);
                rightLeg.z.setEnabled(parts.rightLeg.z);
                rightLeg.pitch.setEnabled(parts.rightLeg.pitch);
                rightLeg.yaw.setEnabled(parts.rightLeg.yaw);
                rightLeg.roll.setEnabled(parts.rightLeg.roll);
                rightLeg.bend.setEnabled(parts.rightLeg.bend);
                rightLeg.bendDirection.setEnabled(parts.rightLeg.bendDirection);

                var leftLeg = builder.getPart("leftLeg");
                leftLeg.x.setEnabled(parts.leftLeg.x);
                leftLeg.y.setEnabled(parts.leftLeg.y);
                leftLeg.z.setEnabled(parts.leftLeg.z);
                leftLeg.pitch.setEnabled(parts.leftLeg.pitch);
                leftLeg.yaw.setEnabled(parts.leftLeg.yaw);
                leftLeg.roll.setEnabled(parts.leftLeg.roll);
                leftLeg.bend.setEnabled(parts.leftLeg.bend);
                leftLeg.bendDirection.setEnabled(parts.leftLeg.bendDirection);

                var rightItem = builder.getPart("rightItem");
                rightItem.x.setEnabled(parts.rightItem.x);
                rightItem.y.setEnabled(parts.rightItem.y);
                rightItem.z.setEnabled(parts.rightItem.z);
                rightItem.pitch.setEnabled(parts.rightItem.pitch);
                rightItem.yaw.setEnabled(parts.rightItem.yaw);
                rightItem.roll.setEnabled(parts.rightItem.roll);
                rightItem.bend.setEnabled(parts.rightItem.bend);
                rightItem.bendDirection.setEnabled(parts.rightItem.bendDirection);

                var leftItem = builder.getPart("leftItem");
                leftItem.x.setEnabled(parts.leftItem.x);
                leftItem.y.setEnabled(parts.leftItem.y);
                leftItem.z.setEnabled(parts.leftItem.z);
                leftItem.pitch.setEnabled(parts.leftItem.pitch);
                leftItem.yaw.setEnabled(parts.leftItem.yaw);
                leftItem.roll.setEnabled(parts.leftItem.roll);
                leftItem.bend.setEnabled(parts.leftItem.bend);
                leftItem.bendDirection.setEnabled(parts.leftItem.bendDirection);


                anim = builder.build();

                FirstPersonMode firstPersonMode = FirstPersonMode.DISABLED;
                if (firstPersonEnabled) {
                    firstPersonMode = FirstPersonMode.THIRD_PERSON_MODEL;
                }

                if (!replaceTick) {
                    KeyframeAnimationPlayer animPlayer = new KeyframeAnimationPlayer(anim, animationContainer.animPlayer.getCurrentTick()).setFirstPersonMode(firstPersonMode);
                    animationContainer.replaceAnimation(animPlayer);
                } else {
                    KeyframeAnimationPlayer animPlayer = new KeyframeAnimationPlayer(anim).setFirstPersonMode(firstPersonMode);
                    animationContainer.replaceAnimationWithFade(AbstractFadeModifier.standardFadeIn(fadeLength, getEase(easeID)), animPlayer);
                }
            }
        } catch (NullPointerException e) {
            logger.warn("Player Animator API failed to play player animation: " + e);
        }
    }

    public static Ease getEase(int ID) {
        if (0 <= ID && ID <= 35) {
            return Ease.getEase((byte)ID);
        }
        else {
            return Ease.INOUTQUAD;
        }
    }
}
