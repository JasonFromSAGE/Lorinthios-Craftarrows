package me.lorinth.craftarrows.Arrows;

import me.lorinth.craftarrows.Constants.ArrowNames;
import me.lorinth.craftarrows.Constants.ConfigPaths;
import me.lorinth.craftarrows.Objects.ConfigValue;
import me.lorinth.craftarrows.Util.VectorHelper;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;

import java.util.ArrayList;
import java.util.HashMap;

public class PullArrowVariant extends ArrowVariant{

    private HashMap<Entity, Entity> entitiesWhoShot = new HashMap<>();
    private double pullAmount;

    public PullArrowVariant(FileConfiguration config){
        super(config, ConfigPaths.Recipes + ".", ArrowNames.Pull, new ArrayList<ConfigValue>(){{
            add(new ConfigValue(ConfigPaths.Recipes + "." + ArrowNames.Pull + ".Power", 2.0));
        }});
    }


    @Override
    protected void loadDetails(FileConfiguration config) {
        ArrayList<ConfigValue> configValues = getConfigValues();
        Object value = configValues.get(0).getValue(config);
        if(value instanceof Integer){
            pullAmount = (double) (int) value;
        }
        else if(value instanceof Double){
            pullAmount = (double) value;
        }
        else if(value instanceof Float){
            pullAmount = (double) (float) value;
        }
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
            shot.setVelocity(VectorHelper.getDirectionVector(shot.getLocation(), shooter.getLocation(), pullAmount));
        }
    }

    @Override
    public void onBlockHit(ProjectileHitEvent event) {

    }
}
