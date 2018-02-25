package me.lorinth.craftarrows.Arrows;

import me.lorinth.craftarrows.Constants.ArrowNames;
import me.lorinth.craftarrows.Constants.ConfigPaths;
import me.lorinth.craftarrows.Objects.ConfigValue;
import me.lorinth.craftarrows.Util.Convert;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.Vector;

import java.util.ArrayList;

public class VolleyArrowVariant extends ArrowVariant{

    private float spread;
    private int arrowCount;

    public VolleyArrowVariant(FileConfiguration config){
        super(config, ConfigPaths.Recipes + ".", ArrowNames.Volley, new ArrayList<ConfigValue>(){{
            add(new ConfigValue(ConfigPaths.Recipes + "." + ArrowNames.Volley + ".ArrowCount", 9));
            add(new ConfigValue(ConfigPaths.Recipes + "." + ArrowNames.Volley + ".ArrowSpread", 12));
        }});
    }

    @Override
    protected void loadDetails(FileConfiguration config) {
        ArrayList<ConfigValue> configValues = getConfigValues();
        arrowCount = Convert.Convert(Integer.class, configValues.get(0).getValue(config));
        spread = Convert.Convert(Float.class, configValues.get(1).getValue(config));
    }

    @Override
    public void onShoot(EntityShootBowEvent event) {
        spreadShot(event.getEntity(), event.getProjectile(), event.getForce() * 2);
        event.getProjectile().remove();
    }

    @Override
    public void onEntityHit(ProjectileHitEvent event) {

    }

    @Override
    public void onBlockHit(ProjectileHitEvent event) {

    }

    private void spreadShot(Entity shooter, Entity projectile, float force){
        Vector direction = shooter.getLocation().getDirection();
        if(shooter instanceof Player)
            direction = ((Player) shooter).getEyeLocation().getDirection();

        Location location = projectile.getLocation().add(direction.normalize());

        for(int i=0; i<arrowCount; i++){
            Arrow arrow = shooter.getWorld().spawnArrow(location, projectile.getVelocity(), force, spread);
            if(shooter instanceof ProjectileSource)
                arrow.setShooter((ProjectileSource) shooter);
        }

    }

}
