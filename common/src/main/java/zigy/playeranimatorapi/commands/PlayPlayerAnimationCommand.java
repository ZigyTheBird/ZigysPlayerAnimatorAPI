package zigy.playeranimatorapi.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import zigy.playeranimatorapi.API.PlayerAnimAPI;
import zigy.playeranimatorapi.PlayerAnimatorAPIMod;
import zigy.playeranimatorapi.data.PlayerAnimationData;
import zigy.playeranimatorapi.data.PlayerParts;

import java.math.BigInteger;

public class PlayPlayerAnimationCommand {

    private static final Logger logger = LogManager.getLogger(PlayerAnimatorAPIMod.class);

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("playPlayerAnimation").requires(commandSourceStack -> commandSourceStack.hasPermission(2))
                .then(Commands.argument("player", EntityArgument.player())
                        .then(Commands.argument("animationID", ResourceLocationArgument.id())
                                .executes(context -> execute(context, false))
                                .then(Commands.argument("playerParts", StringArgumentType.string())
                                        .then(Commands.argument("fadeLength", IntegerArgumentType.integer())
                                                .then(Commands.argument("desiredLength", FloatArgumentType.floatArg())
                                                        .then(Commands.argument("easeID", IntegerArgumentType.integer())
                                                                .then(Commands.argument("firstPersonEnabled", BoolArgumentType.bool())
                                                                        .then(Commands.argument("shouldMirror", BoolArgumentType.bool())
                                                                                .then(Commands.argument("important", BoolArgumentType.bool())
                                                                                        .executes(context -> execute(context, true))))))))))));
    }

    private static int execute(CommandContext<CommandSourceStack> command, boolean full) {
        try {
            if (!full) {
                ServerPlayer player = EntityArgument.getPlayer(command, "player");
                PlayerAnimAPI.playPlayerAnim((ServerLevel) player.level(), player,
                        ResourceLocationArgument.getId(command, "animationID"));
            } else {
                PlayerAnimationData data = new PlayerAnimationData(EntityArgument.getPlayer(command, "player").getUUID(),
                        ResourceLocationArgument.getId(command, "animationID"), PlayerParts.fromBigInteger(playerPartsIntFromString(StringArgumentType.getString(command, "playerParts"))),
                        IntegerArgumentType.getInteger(command, "fadeLength"), FloatArgumentType.getFloat(command, "desiredLength"),
                        IntegerArgumentType.getInteger(command, "easeID"), BoolArgumentType.getBool(command, "firstPersonEnabled"),
                        BoolArgumentType.getBool(command, "shouldMirror"), BoolArgumentType.getBool(command, "important"));

                PlayerAnimAPI.playPlayerAnim(command.getSource().getLevel(), EntityArgument.getPlayer(command, "player"), data);
            }
        } catch (CommandSyntaxException e) {
            logger.warn(e);
        }

        return 1;
    }

    public static BigInteger playerPartsIntFromString(String string) {
        try {
            return new BigInteger(Base64.decodeBase64(string));
        } catch (NumberFormatException e) {
            return new BigInteger(Base64.decodeBase64("axq5j8k4e1uiyz27"));
        }
    }
}
