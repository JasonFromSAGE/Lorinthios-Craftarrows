package me.lorinth.craftarrows.Arrows;

import me.lorinth.craftarrows.Constants.ArrowNames;
import me.lorinth.craftarrows.Constants.ConfigPaths;
import me.lorinth.craftarrows.LorinthsCraftArrows;
import me.lorinth.craftarrows.Util.VectorHelper;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class VortexArrowVariant extends ArrowVariant{

    public VortexArrowVariant(FileConfiguration config){
        super(config, ConfigPaths.Recipes + ".", ArrowNames.Vortex);
    }

    @Override
    protected void loadDetails(FileConfiguration config) {

    }

    @Override
    public void onShoot(EntityShootBowEvent event) {

    }

    @Override
    public void onEntityHit(ProjectileHitEvent event) {
        Entity hitEntity = event.getHitEntity();
        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                for(Entity entity : hitEntity.getNearbyEntities(5, 5, 5)){
                    if(entity != hitEntity)
                        entity.setVelocity(VectorHelper.getVectorBetween(entity.getLocation(), hitEntity.getLocation(), 0, 0.5));
                }
            }
        };
        BukkitTask task = runnable.runTaskTimer(LorinthsCraftArrows.instance, 2, 2);

        Bukkit.getScheduler().runTaskLater(LorinthsCraftArrows.instance, () -> {
            Bukkit.getScheduler().cancelTask(task.getTaskId());
        }, 20 * 1);
    }

    @Override
    public void onBlockHit(ProjectileHitEvent event) {
        Entity projectile = event.getEntity();
        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                for(Entity entity : event.getEntity().getNearbyEntities(5, 5, 5)){
                    if(entity != projectile)
                        entity.setVelocity(VectorHelper.getVectorBetween(entity.getLocation(), projectile.getLocation(), 0, 0.5));
                }
            }
        };
        BukkitTask task = runnable.runTaskTimer(LorinthsCraftArrows.instance, 2, 2);

        Bukkit.getScheduler().runTaskLater(LorinthsCraftArrows.instance, () -> {
            Bukkit.getScheduler().cancelTask(task.getTaskId());
        }, 20 * 1);
    }
}