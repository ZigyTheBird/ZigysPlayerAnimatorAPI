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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import zigy.playeranimatorapi.API.PlayerAnimAPI;
import zigy.playeranimatorapi.PlayerAnimatorAPIMod;
import zigy.playeranimatorapi.data.PlayerAnimationData;
import zigy.playeranimatorapi.data.PlayerParts;

public class PlayPlayerAnimationCommand {

    private static final Logger logger = LogManager.getLogger(PlayerAnimatorAPIMod.class);

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher){
        dispatcher.register(Commands.literal("playPlayerAnimation").requires(commandSourceStack -> commandSourceStack.hasPermission(2))
                .then(Commands.argument("player", EntityArgument.player())
                .then(Commands.argument("normalAnimationID", ResourceLocationArgument.id())
                        .executes(context -> execute(context, false))
                .then(Commands.argument("crouchedAnimationID", ResourceLocationArgument.id())
                .then(Commands.argument("swimmingAnimationID", ResourceLocationArgument.id())
                .then(Commands.argument("playerParts", StringArgumentType.string())
                .then(Commands.argument("fadeLength", IntegerArgumentType.integer())
                .then(Commands.argument("desiredLength", FloatArgumentType.floatArg())
                .then(Commands.argument("easeID", IntegerArgumentType.integer())
                .then(Commands.argument("firstPersonEnabled", BoolArgumentType.bool())
                .then(Commands.argument("shouldMirror", BoolArgumentType.bool())
                .then(Commands.argument("shouldFollowPlayerView", BoolArgumentType.bool())
                        .executes(context -> execute(context, true))))))))))))));
    }

    private static int execute(CommandContext<CommandSourceStack> command, boolean full){
        try {
            if (!full) {
                ServerPlayer player = EntityArgument.getPlayer(command, "player");
                PlayerAnimAPI.playPlayerAnim((ServerLevel) player.level(), player,
                        ResourceLocationArgument.getId(command, "normalAnimationID"));
            } else {
                PlayerAnimationData data = new PlayerAnimationData(EntityArgument.getPlayer(command, "player").getUUID(),
                        ResourceLocationArgument.getId(command, "normalAnimationID"), ResourceLocationArgument.getId(command, "crouchedAnimationID"),
                        ResourceLocationArgument.getId(command, "swimmingAnimationID"), PlayerParts.fromInt(playerPartsIntFromString(StringArgumentType.getString(command, "playerParts"))),
                        IntegerArgumentType.getInteger(command, "fadeLength"), FloatArgumentType.getFloat(command, "desiredLength"),
                        IntegerArgumentType.getInteger(command, "easeID"),BoolArgumentType.getBool(command, "firstPersonEnabled"),
                        BoolArgumentType.getBool(command, "shouldMirror"), BoolArgumentType.getBool(command, "shouldFollowPlayerView"));

                PlayerAnimAPI.playPlayerAnim(command.getSource().getLevel(), EntityArgument.getPlayer(command, "player"), data);
            }
        } catch(CommandSyntaxException e){
            logger.warn(e);
        }

        return 1;
    }

    public static int playerPartsIntFromString(String string) {
        if (isNumeric(string) && string.length() == 14) {
            return Integer.parseInt(string);
        }
        else {
            return -1;
        }
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
