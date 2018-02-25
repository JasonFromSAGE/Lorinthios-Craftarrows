package me.lorinth.craftarrows.Arrows;

import me.lorinth.craftarrows.Constants.ArrowNames;
import me.lorinth.craftarrows.Constants.ConfigPaths;
import me.lorinth.craftarrows.Objects.ConfigValue;
import me.lorinth.craftarrows.Util.Convert;
import me.lorinth.craftarrows.Util.VectorHelper;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;

import java.util.ArrayList;
import java.util.HashMap;

public class PushArrowVariant extends ArrowVariant{

    private double pushAmount;
    private HashMap<Entity, Entity> entitiesWhoShot = new HashMap<>();

    public PushArrowVariant(FileConfiguration config){
        super(config, ConfigPaths.Recipes + ".", ArrowNames.Push, new ArrayList<ConfigValue>(){{
            add(new ConfigValue(ConfigPaths.Recipes + "." + ArrowNames.Push + ".Power", 2.0));
        }});
    }

    @Override
    protected void loadDetails(FileConfiguration config) {
        ArrayList<ConfigValue> configValues = getConfigValues();
        pushAmount = Convert.Convert(Double.class, configValues.get(0).getValue(config));
    }

    @Override
    public void onShoot(EntityShootBowEvent event) {
        entitiesWhoShot.put(event.getProjectile(), event.getEntity());
    }

    @Override
    public void onEntityHit(ProjectileHitEvent event) {
        Entity shooter = entitiesWhoShot.get(event.getEntity());
        if(shooter != null){
            Entity shot = event.getHitEntity();
            shot.setVelocity(VectorHelper.getDirectionVector(shooter.getLocation(), shot.getLocation(), pushAmount));
        }
    }

    @Override
    public void onBlockHit(ProjectileHitEvent event) {

    }
}
