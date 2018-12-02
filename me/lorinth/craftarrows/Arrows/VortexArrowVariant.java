package me.lorinth.craftarrows.Arrows;

import me.lorinth.craftarrows.Constants.ArrowNames;
import me.lorinth.craftarrows.Constants.ConfigPaths;
import me.lorinth.craftarrows.Listener.CraftArrowListener;
import me.lorinth.craftarrows.LorinthsCraftArrows;
import me.lorinth.craftarrows.Util.VectorHelper;
import org.bukkit.Bukkit;
import org.bukkit.Location;
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
        createVortex(event.getEntity(), event.getHitEntity());
    }

    @Override
    public void onBlockHit(ProjectileHitEvent event) {
        createVortex(event.getEntity(), null);
    }

    private void createVortex(Entity arrow, Entity hitEntity){
        Location hitLocation = arrow.getLocation();

        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                for(Entity entity : hitEntity.getNearbyEntities(5, 5, 5)){
                    if(entity != hitEntity && !CraftArrowListener.ignoredEntities.contains(entity))
                        entity.setVelocity(VectorHelper.getVectorBetween(entity.getLocation(), hitEntity != null ? hitEntity.getLocation() : hitLocation, 0, 0.5));
                }
            }
        };
        BukkitTask task = runnable.runTaskTimer(LorinthsCraftArrows.instance, 2, 2);

        Bukkit.getScheduler().runTaskLater(LorinthsCraftArrows.instance, () -> {
            Bukkit.getScheduler().cancelTask(task.getTaskId());
        }, 20 * 1);
    }
}