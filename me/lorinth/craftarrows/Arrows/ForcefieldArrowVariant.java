package me.lorinth.craftarrows.Arrows;


import me.lorinth.craftarrows.Constants.ArrowNames;
import me.lorinth.craftarrows.Constants.ConfigPaths;
import me.lorinth.craftarrows.Util.VectorHelper;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;

public class ForcefieldArrowVariant extends ArrowVariant{

    public ForcefieldArrowVariant(FileConfiguration config){
        super(config, ConfigPaths.Recipes + ".", ArrowNames.Forcefield);
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
        for(Entity entity : event.getEntity().getNearbyEntities(5, 5, 5)){
            if(entity != hitEntity)
                entity.setVelocity(VectorHelper.getDirectionVector(hitEntity.getLocation(), entity.getLocation(), 2));
        }
    }

    @Override
    public void onBlockHit(ProjectileHitEvent event) {
        Entity projectile = event.getEntity();
        for(Entity entity : event.getEntity().getNearbyEntities(5, 5, 5)){
            if(entity != projectile)
                entity.setVelocity(VectorHelper.getDirectionVector(projectile.getLocation(), entity.getLocation(), 2));
        }
    }
}
