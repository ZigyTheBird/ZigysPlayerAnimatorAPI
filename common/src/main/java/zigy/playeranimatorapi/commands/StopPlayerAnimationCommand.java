package zigy.playeranimatorapi.commands;

import com.mojang.brigadier.CommandDispatcher;
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

public class StopPlayerAnimationCommand {

    private static final Logger logger = LogManager.getLogger(PlayerAnimatorAPIMod.class);

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("stopPlayerAnimation").requires(commandSourceStack -> commandSourceStack.hasPermission(2))
                .then(Commands.argument("player", EntityArgument.player())
                        .then(Commands.argument("animationID", ResourceLocationArgument.id())
                                .executes(StopPlayerAnimationCommand::execute))));
    }

    private static int execute(CommandContext<CommandSourceStack> command) {
        try {
            ServerPlayer player = EntityArgument.getPlayer(command, "player");
            PlayerAnimAPI.stopPlayerAnim((ServerLevel) player.level(), player,
                    ResourceLocationArgument.getId(command, "animationID"));
        } catch (CommandSyntaxException e) {
            logger.warn(e);
        }

        return 1;
    }
}
