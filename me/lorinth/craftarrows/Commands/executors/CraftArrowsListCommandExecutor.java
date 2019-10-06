package me.lorinth.craftarrows.Commands.executors;

import me.lorinth.craftarrows.Arrows.ArrowVariant;
import me.lorinth.craftarrows.LorinthsCraftArrows;
import me.lorinth.craftarrows.Util.OutputHandler;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CraftArrowsListCommandExecutor implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if(!sender.hasPermission("lca.list") && !sender.hasPermission("craftarrow.list"))
            OutputHandler.PrintError(sender, "You don't have permission to do that");
        else{
            sender.sendMessage("");
            sender.sendMessage(ChatColor.YELLOW + "Craft Arrows List");
            for(ArrowVariant variant : LorinthsCraftArrows.getAllArrowVariants()){
                sender.sendMessage("- " + variant.getName());
            }
        }

        return false;
    }
}
