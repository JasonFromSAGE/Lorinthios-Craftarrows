package me.lorinth.craftarrows.Arrows;

import me.lorinth.craftarrows.Constants.ArrowNames;
import me.lorinth.craftarrows.Constants.ConfigPaths;
import me.lorinth.craftarrows.Objects.ConfigValue;
import me.lorinth.craftarrows.Util.Convert;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;

import java.util.ArrayList;

public class MedicArrowVariant extends CombatArrowVariant{

    private double healPower;

    public MedicArrowVariant(FileConfiguration config){
        super(config, ConfigPaths.Recipes + ".", ArrowNames.Medic, new ArrayList<ConfigValue>(){{
            add(new ConfigValue(ConfigPaths.Recipes + "." + ArrowNames.Medic + ".HealPower", 8.0));
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
    public void onEntityHit(EntityDamageByEntityEvent event, Projectile projectile, LivingEntity entity){
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
