package me.lorinth.craftarrows.Commands;

import me.lorinth.craftarrows.Util.OutputHandler;
import org.bukkit.command.CommandSender;

public class CommandHelper {

    public static void sendHelp(CommandSender sender){
        if(sender.hasPermission("lca.give") || sender.hasPermission("craftarrow.give"))
            OutputHandler.PrintRawInfo(sender, "/lca give <player> <arrow> [count]");
        if(sender.hasPermission("lca.list") || sender.hasPermission("craftarrow.list"))
            OutputHandler.PrintRawInfo(sender, "/lca list");
        OutputHandler.PrintRawInfo(sender, "/lca recipes [name] - lookup a recipe by simple name (e.g. Freezing Arrow = freezing)");
        if(sender.hasPermission("lca.reload") || sender.hasPermission("craftarrow.reload"))
            OutputHandler.PrintRawInfo(sender, "/lca reload");
    }

}
