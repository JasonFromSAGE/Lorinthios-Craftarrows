package me.lorinth.craftarrows.Arrows;

import me.lorinth.craftarrows.Constants.ConfigPaths;
import me.lorinth.craftarrows.Objects.ConfigValue;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;

public class PotionArrowVariant extends ArrowVariant{

    protected int potionLevel;
    protected int duration;
    protected PotionEffectType type;

    public PotionArrowVariant(FileConfiguration config, String name, PotionEffectType type){
        super(config, ConfigPaths.Recipes + ".", name, new ArrayList<ConfigValue>(){{
            add(new ConfigValue(ConfigPaths.Recipes + "." + name + ".Power", 0));
            add(new ConfigValue(ConfigPaths.Recipes + "." + name + ".Duration", 60));
        }});
        this.type = type;
    }

    public PotionArrowVariant(FileConfiguration config, String name, PotionEffectType type, ArrayList<ConfigValue> configValues){
        super(config, ConfigPaths.Recipes + ".", name, new ArrayList<ConfigValue>(){{
            add(new ConfigValue(ConfigPaths.Recipes + "." + name + ".Power", 0));
            add(new ConfigValue(ConfigPaths.Recipes + "." + name + ".Duration", 60));
            addAll(configValues);
        }});
        this.type = type;
    }

    @Override
    protected void loadDetails(FileConfiguration config) {
        ArrayList<ConfigValue> configValues = getConfigValues();
        potionLevel = (int) configValues.get(0).getValue(config);
        duration = (int) configValues.get(1).getValue(config);
    }

    @Override
    public void onShoot(EntityShootBowEvent event) {

    }

    @Override
    public void onEntityHit(ProjectileHitEvent event) {
        if(event.getHitEntity() instanceof LivingEntity)
            ((LivingEntity) event.getHitEntity()).addPotionEffect(new PotionEffect(type, duration, potionLevel));
    }

    @Override
    public void onBlockHit(ProjectileHitEvent event) {

    }
}
