package zigy.playeranimatorapi.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import zigy.playeranimatorapi.API.PlayerAnimAPI;
import zigy.playeranimatorapi.PlayerAnimatorAPIMod;
import zigy.playeranimatorapi.data.PlayerAnimationData;
import zigy.playeranimatorapi.data.PlayerParts;
import zigy.playeranimatorapi.modifier.CommonModifier;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class PlayPlayerAnimationCommand {

    private static final Logger logger = LogManager.getLogger(PlayerAnimatorAPIMod.class);

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("playPlayerAnimation").requires(commandSourceStack -> commandSourceStack.hasPermission(2))
                .then(Commands.argument("player", EntityArgument.player())
                        .then(Commands.argument("animationID", ResourceLocationArgument.id())
                                .executes(context -> execute(context, CommandState.Minimal))
                                    .then(Commands.argument("fadeLength", IntegerArgumentType.integer())
                                            .then(Commands.argument("easeID", IntegerArgumentType.integer())
                                                    .then(Commands.argument("firstPersonEnabled", BoolArgumentType.bool())
                                                            .then(Commands.argument("important", BoolArgumentType.bool())
                                                                    .executes(context -> execute(context, CommandState.Advanced))
                                                                        .then(Commands.argument("playerParts", StringArgumentType.string())
                                                                            .then(Commands.argument("modifiers", StringArgumentType.string())
                                                                                .executes(context -> execute(context, CommandState.Complete)))))))))));
    }

    private static int execute(CommandContext<CommandSourceStack> command, CommandState state) {
        try {
            switch (state) {
                case Minimal -> {
                    ServerPlayer player = EntityArgument.getPlayer(command, "player");
                    PlayerAnimAPI.playPlayerAnim((ServerLevel) player.level(), player,
                            ResourceLocationArgument.getId(command, "animationID"));
                }
                case Advanced -> {
                    PlayerAnimationData data = new PlayerAnimationData(EntityArgument.getPlayer(command, "player").getUUID(),
                            ResourceLocationArgument.getId(command, "animationID"), null, null,
                            IntegerArgumentType.getInteger(command, "fadeLength"), IntegerArgumentType.getInteger(command, "easeID"),
                            BoolArgumentType.getBool(command, "firstPersonEnabled"), BoolArgumentType.getBool(command, "important"));

                    PlayerAnimAPI.playPlayerAnim(command.getSource().getLevel(), EntityArgument.getPlayer(command, "player"), data);
                }
                case Complete -> {
                    PlayerAnimationData data = new PlayerAnimationData(EntityArgument.getPlayer(command, "player").getUUID(),
                            ResourceLocationArgument.getId(command, "animationID"), PlayerParts.fromBigInteger(playerPartsIntFromString(StringArgumentType.getString(command, "playerParts"))),
                            modifierList(StringArgumentType.getString(command, "modifiers")),
                            IntegerArgumentType.getInteger(command, "fadeLength"), IntegerArgumentType.getInteger(command, "easeID"),
                            BoolArgumentType.getBool(command, "firstPersonEnabled"), BoolArgumentType.getBool(command, "important"));

                    PlayerAnimAPI.playPlayerAnim(command.getSource().getLevel(), EntityArgument.getPlayer(command, "player"), data);
                }
            }
        } catch (CommandSyntaxException e) {
            logger.warn(e);
        }

        return 1;
    }

    public static List<CommonModifier> modifierList(String input) {
        List<CommonModifier> list = new ArrayList<>();
        for (String str : input.split(",")) {
            list.add(new CommonModifier(new ResourceLocation(str), null));
        }
        return list;
    }

    public static BigInteger playerPartsIntFromString(String string) {
        try {
            return new BigInteger(string, Character.MAX_RADIX);
        } catch (NumberFormatException e) {
            return new BigInteger("axq5j8k4e1uiyz27", Character.MAX_RADIX);
        }
    }
}
