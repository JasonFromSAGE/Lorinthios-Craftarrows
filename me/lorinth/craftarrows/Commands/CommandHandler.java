package me.lorinth.craftarrows.Commands;

import me.lorinth.craftarrows.Arrows.ArrowVariant;
import me.lorinth.craftarrows.GUI.ArrowRecipeMenu;
import me.lorinth.craftarrows.LorinthsCraftArrows;
import me.lorinth.craftarrows.Util.OutputHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CommandHandler {

    public static void ProcessCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
        if(args[0].equalsIgnoreCase("give")){
            if(!sender.hasPermission("lca.give"))
                OutputHandler.PrintError(sender, "You don't have permission to do that");
            else{
                //lca give <player> <arrow> <count>
                if(args.length == 4){
                    Player player = Bukkit.getPlayer(args[1]);
                    ArrowVariant arrowVariant = LorinthsCraftArrows.getArrowVariantBySimpleName(args[2]);
                    int count = 1;
                    try{
                        count = Integer.valueOf(args[3]);
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
        }
        else if(args[0].equalsIgnoreCase("recipes")){
            if(sender instanceof Player){
                Player player = (Player) sender;
                if(args.length == 1){
                    ArrowRecipeMenu.open(player);
                }
                if(args.length == 2){
                    ArrowRecipeMenu.open(player, args[1]);
                }
            }
        }
        else
            LorinthsCraftArrows.sendHelp(sender);
    }

}
