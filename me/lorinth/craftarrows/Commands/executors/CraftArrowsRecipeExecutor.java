package me.lorinth.craftarrows.Commands.executors;

import me.lorinth.craftarrows.GUI.ArrowRecipeMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CraftArrowsRecipeExecutor implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if(sender instanceof Player){
            Player player = (Player) sender;
            if(args.length == 0){
                ArrowRecipeMenu.open(player);
            }
            if(args.length == 1){
                ArrowRecipeMenu.open(player, args[0]);
            }
        }

        return false;
    }
}
