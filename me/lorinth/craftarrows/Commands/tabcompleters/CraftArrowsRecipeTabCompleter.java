package me.lorinth.craftarrows.Commands.tabcompleters;

import me.lorinth.craftarrows.Arrows.ArrowVariant;
import me.lorinth.craftarrows.LorinthsCraftArrows;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CraftArrowsRecipeTabCompleter implements TabCompleter {

    private List<String> arrowNames;
    private List<String> emptyList = Arrays.asList();

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        if(args.length == 1){
            if(arrowNames == null)
                arrowNames = getArrowNames();
            return arrowNames;
        }

        return emptyList;
    }

    private List<String> getArrowNames(){
        Stream<String> arrowNames = Arrays.stream(LorinthsCraftArrows.getAllArrowVariants().toArray()).map(o -> ((ArrowVariant)o).getName());
        return arrowNames.collect(Collectors.toList());
    }

}
