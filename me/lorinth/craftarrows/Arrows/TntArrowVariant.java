package me.lorinth.craftarrows.Arrows;

import me.lorinth.craftarrows.Constants.ArrowNames;
import me.lorinth.craftarrows.Constants.ConfigPaths;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;

public class TntArrowVariant extends ArrowVariant{

    public TntArrowVariant(FileConfiguration config){
        super(config, ConfigPaths.Recipes + ".", ArrowNames.Tnt);
    }

    @Override
    protected void loadDetails(FileConfiguration config) {

    }

    @Override
    public void onShoot(EntityShootBowEvent event) {

    }

    @Override
    public void onEntityHit(ProjectileHitEvent event) {
        explode(event.getEntity().getLocation());
    }

    @Override
    public void onBlockHit(ProjectileHitEvent event) {
        explode(event.getEntity().getLocation());
    }

    private void explode(Location location){
        location.getWorld().createExplosion(location.getBlockX(), location.getBlockY(), location.getBlockZ(), 2, false, false);
    }
}