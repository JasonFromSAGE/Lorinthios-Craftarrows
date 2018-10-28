package me.lorinth.craftarrows.Arrows;

import me.lorinth.craftarrows.Constants.ArrowNames;
import me.lorinth.craftarrows.Constants.ConfigPaths;
import me.lorinth.craftarrows.Objects.ConfigValue;
import me.lorinth.craftarrows.Util.Convert;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

import java.util.ArrayList;

public class LanceArrow extends CombatArrowVariant{

    private int pierceDistance = 5;
    private double targetDamage = 6;
    private double pierceDamage = 3;

    public LanceArrow(FileConfiguration config) {
        super(config, ConfigPaths.Recipes + ".", ArrowNames.Lance, new ArrayList<ConfigValue>(){{
            add(new ConfigValue(ConfigPaths.Recipes + "." + ArrowNames.Lance + ".PierceDistance", 5));
            add(new ConfigValue(ConfigPaths.Recipes + "." + ArrowNames.Lance + ".TargetDamage", 6.0));
            add(new ConfigValue(ConfigPaths.Recipes + "." + ArrowNames.Lance + ".PierceDamage", 3.0));
        }});
    }

    @Override
    protected void loadDetails(FileConfiguration config) {

        ArrayList<ConfigValue> configValues = getConfigValues();
        pierceDistance = Convert.Convert(Integer.class, configValues.get(0).getValue(config));
        targetDamage = Convert.Convert(Double.class, configValues.get(1).getValue(config));
        pierceDamage = Convert.Convert(Double.class, configValues.get(2).getValue(config));
    }

    @Override
    public void onShoot(EntityShootBowEvent event) {

    }

    @Override
    public void onEntityHit(ProjectileHitEvent event) {
        Entity arrow = event.getEntity();
        Entity hit = event.getHitEntity();
        Vector direction = arrow.getLocation().getDirection().normalize().multiply(new Vector(-1, -1, 1));

        ArrayList<LivingEntity> entitiesHit = new ArrayList<>();

        Location start = arrow.getLocation();
        Location end = arrow.getLocation().add(direction.multiply(pierceDistance));

        while(start.distanceSquared(end) > 1){
            start = start.add(direction.normalize());
            arrow.teleport(start);
            for(Entity entity : arrow.getNearbyEntities(0.5, 0.5, 0.5)){
                if(entity instanceof LivingEntity && entity != hit && !entitiesHit.contains(entity)){
                    entitiesHit.add((LivingEntity) entity);
                }
            }
        }

        if(hit instanceof LivingEntity){
            ((LivingEntity) hit).damage(targetDamage);
        }
        for(LivingEntity livingEntity : entitiesHit) {
            livingEntity.damage(pierceDamage);
        }

        arrow.remove();
    }

    @Override
    public void onBlockHit(ProjectileHitEvent event) {

    }

    @Override
    public void onEntityHit(EntityDamageByEntityEvent event, Projectile projectile, LivingEntity living) {
        event.setCancelled(true);
    }
}
