package me.lorinth.craftarrows.Commands.tabcompleters;

import me.lorinth.craftarrows.Arrows.ArrowVariant;
import me.lorinth.craftarrows.LorinthsCraftArrows;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CraftArrowsGiveTabCompleter implements TabCompleter {

    private List<String> arrowNames;
    private List<String> countList = Arrays.asList("[count]");
    private List<String> emptyList = Arrays.asList();

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String s, String[] args) {
        if(args.length == 1){
            return getPlayerNames();
        }
        else if(args.length == 2){
            if(arrowNames == null)
                arrowNames = getArrowNames();
            return arrowNames;
        }
        else if(args.length > 2){
            return countList;
        }

        return emptyList;
    }

    private List<String> getPlayerNames(){
        Stream<String> playerNames = Arrays.stream(Bukkit.getOnlinePlayers().toArray()).map(o -> ((Player) o).getName());
        return playerNames.collect(Collectors.toList());
    }

    private List<String> getArrowNames(){
        Stream<String> arrowNames = Arrays.stream(LorinthsCraftArrows.getAllArrowVariants().toArray()).map(o -> ((ArrowVariant)o).getName());
        return arrowNames.collect(Collectors.toList());
    }
}
