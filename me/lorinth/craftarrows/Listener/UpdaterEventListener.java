package me.lorinth.craftarrows.Listener;

import me.lorinth.craftarrows.Updater;
import me.lorinth.craftarrows.Util.OutputHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 *  Listener used for telling players about Updates
 */
public class UpdaterEventListener implements Listener{

    Updater updater;

    public UpdaterEventListener(Updater updater){
        this.updater = updater;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        if((player.hasPermission("craftarrows.update") || player.isOp()) && updater.getResult() == Updater.UpdateResult.UPDATE_AVAILABLE)
            OutputHandler.PrintCommandInfo(player, "[CraftArrows] : " + OutputHandler.HIGHLIGHT + "An Update is Available!");
    }

}
