package me.lorinth.craftarrows.Arrows;

import me.lorinth.craftarrows.Constants.ArrowNames;
import me.lorinth.craftarrows.Constants.ConfigPaths;
import me.lorinth.craftarrows.Util.VectorHelper;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;

import java.util.HashMap;

public class PullArrowVariant extends ArrowVariant{

    public PullArrowVariant(FileConfiguration config){
        super(config, ConfigPaths.Recipes + ".", ArrowNames.Pull);
    }
    private HashMap<Entity, Entity> entitiesWhoShot = new HashMap<>();

    @Override
    protected void loadDetails(FileConfiguration config) {

    }

    @Override
    public void onShoot(EntityShootBowEvent event) {
        entitiesWhoShot.put(event.getProjectile(), event.getEntity());
    }

    @Override
    public void onEntityHit(ProjectileHitEvent event) {
        Entity shooter = entitiesWhoShot.get(event.getEntity());
        if(shooter != null){
            Entity shot = event.getHitEntity();
            shot.setVelocity(VectorHelper.getDirectionVector(shot.getLocation(), shooter.getLocation(), 5));
        }
    }

    @Override
    public void onBlockHit(ProjectileHitEvent event) {

    }
}
