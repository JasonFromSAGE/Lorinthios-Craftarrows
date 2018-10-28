package me.lorinth.craftarrows.Arrows;

import me.lorinth.craftarrows.Constants.ArrowNames;
import me.lorinth.craftarrows.Constants.ConfigPaths;
import me.lorinth.craftarrows.LorinthsCraftArrows;
import me.lorinth.craftarrows.Objects.ConfigValue;
import me.lorinth.craftarrows.Util.Convert;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;

public class BoneArrow extends ArrowVariant{

    private boolean transforms = true;
    private int duration = 100;
    private boolean spawns = false;
    private double damage = 4.0;

    public BoneArrow(FileConfiguration config) {
        super(config, ConfigPaths.Recipes + ".", ArrowNames.Bone, new ArrayList<ConfigValue>(){{
            add(new ConfigValue(ConfigPaths.Recipes + "." + ArrowNames.Bone + ".Transforms", true));
            add(new ConfigValue(ConfigPaths.Recipes + "." + ArrowNames.Bone + ".Duration", 100));
            add(new ConfigValue(ConfigPaths.Recipes + "." + ArrowNames.Bone + ".Spawns", false));
            add(new ConfigValue(ConfigPaths.Recipes + "." + ArrowNames.Bone + ".Damage", 4.0));
        }});
    }

    @Override
    protected void loadDetails(FileConfiguration config) {
        ArrayList<ConfigValue> configValues = getConfigValues();
        transforms = (boolean) configValues.get(0).getValue(config);
        duration = Convert.Convert(Integer.class, configValues.get(1).getValue(config));
        spawns = (boolean) configValues.get(2).getValue(config);
        damage = Convert.Convert(Double.class, configValues.get(3).getValue(config));
    }

    @Override
    public void onShoot(EntityShootBowEvent event) {

    }

    @Override
    public void onEntityHit(ProjectileHitEvent event) {
        Entity entity = event.getHitEntity();
        if(entity instanceof LivingEntity){
            ((LivingEntity) entity).damage(damage);

            if(transforms){
                ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, duration, 1, false, false), false);

                Entity newEntity = null;
                if(entity instanceof Horse){
                    newEntity = entity.getWorld().spawn(entity.getLocation(), SkeletonHorse.class);
                }
                else{
                    newEntity = entity.getWorld().spawn(entity.getLocation(), Skeleton.class);
                }

                final Entity e = newEntity;
                entity.addPassenger(e);
                Bukkit.getScheduler().runTaskLater(LorinthsCraftArrows.instance, e::remove, duration);
            }
            if(spawns){
                entity.getWorld().spawn(event.getEntity().getLocation(), Skeleton.class);
            }
        }
    }

    @Override
    public void onBlockHit(ProjectileHitEvent event) {

    }
}
