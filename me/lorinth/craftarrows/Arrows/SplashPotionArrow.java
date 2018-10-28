package me.lorinth.craftarrows.Arrows;

import me.lorinth.craftarrows.Constants.ConfigPaths;
import me.lorinth.craftarrows.Objects.ConfigValue;
import me.lorinth.craftarrows.Util.Convert;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;

public class SplashPotionArrow extends PotionArrowVariant{

    private double radius;
    private int color;

    public SplashPotionArrow(FileConfiguration config, String name, PotionEffectType type) {
        super(config, name, type,
                new ConfigValue(ConfigPaths.Recipes + "." + name + ".Radius", 2),
                new ConfigValue(ConfigPaths.Recipes + "." + name + ".Color", 2));
    }

    @Override
    protected void loadDetails(FileConfiguration config) {
        super.loadDetails(config);
        ArrayList<ConfigValue> configValues = getConfigValues();
        radius = Convert.Convert(Integer.class, configValues.get(2).getValue(config));
        color = Convert.Convert(Integer.class, configValues.get(3).getValue(config));
    }

    @Override
    public void onEntityHit(ProjectileHitEvent event) {
        splash(event.getHitEntity().getLocation(), event.getEntity());

        if(event.getHitEntity() instanceof LivingEntity)
            ((LivingEntity) event.getHitEntity()).addPotionEffect(new PotionEffect(type, duration, potionLevel));
    }

    @Override
    public void onBlockHit(ProjectileHitEvent event) {
        splash(event.getHitBlock().getLocation().add(0, 1.0, 0), event.getEntity());
    }

    private void splash(Location location, Entity entity){
        location.getWorld().playSound(location, Sound.ENTITY_SPLASH_POTION_BREAK, 1.0f, 1.0f);
        location.getWorld().playEffect(location, Effect.POTION_BREAK, color);

        for(Entity ent : entity.getNearbyEntities(radius, radius, radius)){
            if(ent instanceof LivingEntity){
                ((LivingEntity) ent).addPotionEffect(potionEffect);
            }
        }
    }

}
