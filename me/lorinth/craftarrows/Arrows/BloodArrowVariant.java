package me.lorinth.craftarrows.Arrows;

import me.lorinth.craftarrows.Constants.ArrowNames;
import me.lorinth.craftarrows.Constants.ConfigPaths;
import me.lorinth.craftarrows.Objects.ConfigValue;
import me.lorinth.craftarrows.Util.Convert;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;

import java.util.ArrayList;

public class BloodArrowVariant extends ArrowVariant{

    private double healPower;

    public BloodArrowVariant(FileConfiguration config){
        super(config, ConfigPaths.Recipes + ".", ArrowNames.Blood, new ArrayList<ConfigValue>(){{
            add(new ConfigValue(ConfigPaths.Recipes + "." + ArrowNames.Blood + ".HealPower", 4.0));
        }});
    }

    @Override
    protected void loadDetails(FileConfiguration config) {
        ArrayList<ConfigValue> configValues = getConfigValues();
        healPower = Convert.Convert(Double.class, configValues.get(0).getValue(config));
    }

    @Override
    public void onShoot(EntityShootBowEvent event) {

    }

    @Override
    public void onEntityHit(ProjectileHitEvent event) {
        if(event.getEntity().getShooter() instanceof LivingEntity){
            LivingEntity entity = (LivingEntity) event.getEntity().getShooter();
            entity.setHealth(Math.min(entity.getMaxHealth(), entity.getHealth() + healPower));
        }
    }

    @Override
    public void onBlockHit(ProjectileHitEvent event) {

    }
}