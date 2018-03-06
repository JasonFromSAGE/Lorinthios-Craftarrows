package me.lorinth.craftarrows.Arrows;

import me.lorinth.craftarrows.Constants.ArrowNames;
import me.lorinth.craftarrows.Constants.ConfigPaths;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;

import java.util.HashMap;

public class TeleportArrowVariant extends ArrowVariant{

    private HashMap<Entity, Entity> shooters = new HashMap<>();

    public TeleportArrowVariant(FileConfiguration config){
        super(config, ConfigPaths.Recipes + ".", ArrowNames.Teleport);
    }

    @Override
    protected void loadDetails(FileConfiguration config) {

    }

    @Override
    public void onShoot(EntityShootBowEvent event) {
        shooters.put(event.getProjectile(), event.getEntity());
    }

    @Override
    public void onEntityHit(ProjectileHitEvent event) {
        if(shooters.containsKey(event.getEntity()))
            shooters.get(event.getEntity()).teleport(event.getHitEntity());
    }

    @Override
    public void onBlockHit(ProjectileHitEvent event) {
        if(shooters.containsKey(event.getEntity())) {
            Entity shooter = shooters.get(event.getEntity());
            float yaw = shooter.getLocation().getYaw();
            float pitch = shooter.getLocation().getPitch();
            Location location = event.getHitBlock().getLocation().add(0.5, 1, 0.5);
            location.setYaw(yaw);
            location.setPitch(pitch);
            shooter.teleport(location);

        }
    }
}