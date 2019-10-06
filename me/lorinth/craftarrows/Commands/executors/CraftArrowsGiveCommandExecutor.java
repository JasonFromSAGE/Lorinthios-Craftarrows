package me.lorinth.craftarrows.Commands.executors;

import me.lorinth.craftarrows.Arrows.ArrowVariant;
import me.lorinth.craftarrows.LorinthsCraftArrows;
import me.lorinth.craftarrows.Util.OutputHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CraftArrowsGiveCommandExecutor implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if(!sender.hasPermission("lca.give") && !sender.hasPermission("craftarrow.give"))
            OutputHandler.PrintError(sender, "You don't have permission to do that");
        else{
            //lca give <player> <arrow> <count>
            if(args.length == 3){
                Player player = Bukkit.getPlayer(args[0]);
                ArrowVariant arrowVariant = LorinthsCraftArrows.getArrowVariantBySimpleName(args[1]);
                int count = 1;
                try{
                    count = Integer.valueOf(args[2]);
                }
                catch(Exception e){

                }

                if(player != null && arrowVariant != null){
                    ItemStack arrow = arrowVariant.getRecipe().getItem();
                    arrow.setAmount(count);
                    player.getInventory().addItem(arrow);
                }
            }
            else{
                Bukkit.getConsoleSender().sendMessage("[LorinthsCraftArrows]: Invalid command usage, /lca give <player> <arrow> <count>");
            }
        }
        return false;
    }
}
