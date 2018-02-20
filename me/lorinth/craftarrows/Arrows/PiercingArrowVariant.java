package me.lorinth.craftarrows.Arrows;

import me.lorinth.craftarrows.Constants.ArrowNames;
import me.lorinth.craftarrows.Constants.ConfigPaths;
import me.lorinth.craftarrows.Objects.ConfigValue;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.EntityEquipment;

import java.util.ArrayList;

public class PiercingArrowVariant extends ArrowVariant{

    private double extraDamage = 4.0;

    public PiercingArrowVariant(FileConfiguration config){
        super(config, ConfigPaths.Recipes + ".", ArrowNames.Piercing, new ArrayList<ConfigValue>(){{
            add(new ConfigValue(ConfigPaths.Recipes + "." + ArrowNames.Piercing + ".BonusDamage", 4.0));
        }});
    }

    @Override
    protected void loadDetails(FileConfiguration config) {
        ArrayList<ConfigValue> configValues = getConfigValues();
        Object value = configValues.get(0).getValue(config);
        if(value instanceof Integer){
            extraDamage = (double) (int) value;
        }
        else if(value instanceof Double){
            extraDamage = (double) value;
        }
        else if(value instanceof Float){
            extraDamage = (double) (float) value;
        }
    }

    @Override
    public void onShoot(EntityShootBowEvent event) {

    }

    @Override
    public void onEntityHit(ProjectileHitEvent event) {
        if(event.getHitEntity() instanceof LivingEntity){
            LivingEntity living = (LivingEntity) event.getHitEntity();
            EntityEquipment equipment = living.getEquipment();
            if(equipment.getHelmet().getType() != Material.AIR || equipment.getChestplate().getType() != Material.AIR ||
                    equipment.getLeggings().getType() != Material.AIR || equipment.getBoots().getType() != Material.AIR){
                living.setHealth(Math.max(0, living.getHealth() - extraDamage));
            }
        }
    }

    @Override
    public void onBlockHit(ProjectileHitEvent event) {

    }
}
