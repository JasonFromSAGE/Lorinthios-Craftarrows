package me.lorinth.craftarrows.Commands.tabcompleters;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.Arrays;
import java.util.List;

public class CraftArrowsTabCompleter implements TabCompleter {

    private List<String> subCommands = Arrays.asList("give", "list", "recipes", "reload");
    private CraftArrowsGiveTabCompleter giveTabCompleter = new CraftArrowsGiveTabCompleter();
    private CraftArrowsRecipeTabCompleter recipeTabCompleter = new CraftArrowsRecipeTabCompleter();
    private List<String> emptyList = Arrays.asList();

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String s, String[] args) {
        if(args.length == 1){
            return subCommands;
        }
        else if(args.length > 1){
            String subCommand = args[0];
            String[] subArgs = Arrays.copyOfRange(args, 1, args.length);
            if(subCommand.equalsIgnoreCase("give")){
                return giveTabCompleter.onTabComplete(sender, cmd, s, subArgs);
            }
            else if(subCommand.equalsIgnoreCase("list")) {
                return emptyList;
            }
            else if(subCommand.equalsIgnoreCase("recipes")){
                return recipeTabCompleter.onTabComplete(sender, cmd, s, subArgs);
            }
            else if(subCommand.equalsIgnoreCase("reload")){
                return emptyList;
            }
        }
        return emptyList;
    }
}
