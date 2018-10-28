package me.lorinth.craftarrows.Util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

/**
 * Not to be used outside of LorinthsRpgMobs, this is used to ease output to players/console
 */
public class OutputHandler {
    public static final ChatColor ERROR = ChatColor.RED;
    public static final ChatColor INFO = ChatColor.YELLOW;
    public static final ChatColor COMMAND = ChatColor.GOLD;
    public static final ChatColor HIGHLIGHT = ChatColor.YELLOW;
    private static String consolePrefix = "[LorinthsCraftArrows] : ";
    private static String infoPrefix = INFO + consolePrefix;
    private static String errorPrefix = ERROR + "[Error]" + infoPrefix + ERROR;
    private static ConsoleCommandSender console = Bukkit.getConsoleSender();

    /**
     * Should only be used internally in LorinthsRpgMobs as it is used for printing info messages
     * @param message - message to display
     */
    public static void PrintInfo(String message){
        console.sendMessage(infoPrefix + message);
    }

    /**
     * Should only be used internally in LorinthsRpgMobs as it is used for printing error messages
     * @param message - error message to display
     */
    public static void PrintError(String message){
        console.sendMessage(errorPrefix + message);
    }

    public static void PrintException(String message, Exception exception){
        PrintError(message);
        exception.printStackTrace();
    }

    public static void PrintRawInfo(String message){ console.sendMessage(INFO + message); }

    public static void PrintRawError(String message){ console.sendMessage(ERROR + message); }

    public static void PrintInfo(CommandSender player, String message){
        player.sendMessage(infoPrefix + message);
    }

    public static void PrintError(CommandSender player, String message){
        player.sendMessage(errorPrefix + message);
    }

    public static void PrintRawInfo(CommandSender player, String message){
        player.sendMessage(INFO + message);
    }

    public static void PrintRawError(CommandSender player, String message){
        player.sendMessage(ERROR + message);
    }

    public static void PrintCommandInfo(CommandSender player, String message){
        player.sendMessage(COMMAND + message);
    }

    public static void PrintWhiteSpace(CommandSender player, int lines){
        for(int i=0; i<lines; i++)
            player.sendMessage("");
    }
}
