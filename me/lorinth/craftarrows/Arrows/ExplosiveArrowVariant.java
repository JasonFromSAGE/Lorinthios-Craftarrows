package me.lorinth.craftarrows.Arrows;

import me.lorinth.craftarrows.Constants.ArrowNames;
import me.lorinth.craftarrows.Constants.ConfigPaths;
import me.lorinth.craftarrows.Objects.ConfigValue;
import me.lorinth.craftarrows.Util.Convert;
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
        power = Convert.Convert(Float.class, configValues.get(0).getValue(config));
        breakBlocks = (boolean) configValues.get(1).getValue(config);
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
