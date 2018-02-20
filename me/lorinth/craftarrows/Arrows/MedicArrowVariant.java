package me.lorinth.craftarrows.Arrows;

import me.lorinth.craftarrows.Constants.ArrowNames;
import me.lorinth.craftarrows.Constants.ConfigPaths;
import me.lorinth.craftarrows.Objects.ConfigValue;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;

import java.util.ArrayList;

public class MedicArrowVariant extends ArrowVariant{

    private double healPower;

    public MedicArrowVariant(FileConfiguration config){
        super(config, ConfigPaths.Recipes + ".", ArrowNames.Medic, new ArrayList<ConfigValue>(){{
            add(new ConfigValue(ConfigPaths.Recipes + "." + ArrowNames.Medic + ".HealPower", 8.0));
        }});
    }

    @Override
    protected void loadDetails(FileConfiguration config) {
        ArrayList<ConfigValue> configValues = getConfigValues();
        Object value = configValues.get(0).getValue(config);
        if(value instanceof Integer){
            healPower = (double) (int) value;
        }
        else if(value instanceof Double){
            healPower = (double) value;
        }
        else if(value instanceof Float){
            healPower = (double) (float) value;
        }
    }

    @Override
    public void onShoot(EntityShootBowEvent event) {

    }

    public void onEntityHit(EntityDamageByEntityEvent event, LivingEntity entity){
        entity.setHealth(Math.min(entity.getMaxHealth(), entity.getHealth() + healPower));
        event.setCancelled(true);
    }

    @Override
    public void onEntityHit(ProjectileHitEvent event) {

    }

    @Override
    public void onBlockHit(ProjectileHitEvent event) {

    }
}
