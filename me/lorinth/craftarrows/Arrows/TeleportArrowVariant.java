package me.lorinth.craftarrows.Arrows;

import me.lorinth.craftarrows.Constants.ArrowNames;
import me.lorinth.craftarrows.Constants.ConfigPaths;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;

public class TeleportArrowVariant extends ArrowVariant{

    public TeleportArrowVariant(FileConfiguration config){
        super(config, ConfigPaths.Recipes + ".", ArrowNames.Teleport);
    }

    @Override
    protected void loadDetails(FileConfiguration config) {

    }

    @Override
    public void onShoot(EntityShootBowEvent event) {

    }

    @Override
    public void onEntityHit(ProjectileHitEvent event) {
        if(event.getEntity().getShooter() instanceof Entity){
            Entity shooter = (Entity) event.getEntity();
            shooter.teleport(event.getHitEntity());
        }
    }

    @Override
    public void onBlockHit(ProjectileHitEvent event) {
        if(event.getEntity().getShooter() instanceof Entity){
            Entity shooter = (Entity) event.getEntity();
            shooter.teleport(event.getHitBlock().getLocation().add(0, 1, 0));
        }
    }
}