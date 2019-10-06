package me.lorinth.craftarrows.Commands.executors;

import me.lorinth.craftarrows.Commands.CommandHelper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

public class CraftArrowsCommandExecutor implements CommandExecutor {

    private CraftArrowsGiveCommandExecutor giveCommandExecutor = new CraftArrowsGiveCommandExecutor();
    private CraftArrowsListCommandExecutor listCommandExecutor = new CraftArrowsListCommandExecutor();
    private CraftArrowsRecipeExecutor recipeCommandExecutor = new CraftArrowsRecipeExecutor();
    private CraftArrowsReloadCommandExecutor reloadCommandExecutor = new CraftArrowsReloadCommandExecutor();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if(args.length > 0) {
            String command = args[0];
            String[] subArgs = Arrays.copyOfRange(args, 1, args.length);
            if(command.equalsIgnoreCase("give")){
                giveCommandExecutor.onCommand(sender, cmd, command, subArgs);
            }
            else if(command.equalsIgnoreCase("list")){
                listCommandExecutor.onCommand(sender, cmd, command, subArgs);
            }
            else if(command.equalsIgnoreCase("recipes")){
                recipeCommandExecutor.onCommand(sender, cmd, command, subArgs);
            }
            else if(command.equalsIgnoreCase("reload")) {
                reloadCommandExecutor.onCommand(sender, cmd, command, subArgs);
            }
            else{
                CommandHelper.sendHelp(sender);
            }
        }
        else {
            CommandHelper.sendHelp(sender);
        }

        return false;
    }


}
