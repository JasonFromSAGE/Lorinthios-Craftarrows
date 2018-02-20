package me.lorinth.craftarrows.Arrows;

import me.lorinth.craftarrows.Constants.ArrowNames;
import me.lorinth.craftarrows.Constants.ConfigPaths;
import me.lorinth.craftarrows.Objects.ConfigValue;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;

import java.util.ArrayList;

public class ExplosiveArrowVariant extends ArrowVariant{

    private float power;
    private boolean breakBlocks;
    private boolean setFire = false;

    public ExplosiveArrowVariant(FileConfiguration config){
        super(config, ConfigPaths.Recipes + ".", ArrowNames.Explosive, new ArrayList<ConfigValue>(){{
            add(new ConfigValue(ConfigPaths.Recipes + "." + ArrowNames.Explosive + ".Power", 4.0f));
            add(new ConfigValue(ConfigPaths.Recipes + "." + ArrowNames.Explosive + ".BreakBlocks", true));
            add(new ConfigValue(ConfigPaths.Recipes + "." + ArrowNames.Explosive + ".SetFire", false));
        }});
    }

    @Override
    protected void loadDetails(FileConfiguration config) {
        ArrayList<ConfigValue> configValues = getConfigValues();
        Object value1 = configValues.get(0).getValue(config);;
        if(value1 instanceof Integer){
            power = (float) (int) value1;
        }
        else if(value1 instanceof Double){
            power = (float) (double) value1;
        }
        else if(value1 instanceof Float){
            power = (float) value1;
        }

        breakBlocks = (boolean) configValues.get(1).getValue(config);
        if(configValues.size() > 2 && configValues.get(2) != null && configValues.get(2).getValue(config) != null)
            setFire = (boolean) configValues.get(2).getValue(config);
    }

    @Override
    public void onShoot(EntityShootBowEvent event) {

    }

    @Override
    public void onEntityHit(ProjectileHitEvent event) {
        explode(event.getEntity().getLocation());
    }

    @Override
    public void onBlockHit(ProjectileHitEvent event) {
        explode(event.getEntity().getLocation());
    }

    private void explode(Location location){
        location.getWorld().createExplosion(location.getBlockX(), location.getBlockY(), location.getBlockZ(), power, setFire, breakBlocks);
    }

}
