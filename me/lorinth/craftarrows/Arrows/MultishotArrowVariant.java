package me.lorinth.craftarrows.Arrows;

import me.lorinth.craftarrows.Constants.ArrowNames;
import me.lorinth.craftarrows.Constants.ConfigPaths;
import me.lorinth.craftarrows.LorinthsCraftArrows;
import me.lorinth.craftarrows.Objects.ConfigValue;
import me.lorinth.craftarrows.Util.Convert;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Arrow;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;

import java.util.ArrayList;

public class MultishotArrowVariant extends ArrowVariant{

    private double delay;
    private double arrowCount;

    public MultishotArrowVariant(FileConfiguration config){
        super(config, ConfigPaths.Recipes + ".", ArrowNames.Multishot, new ArrayList<ConfigValue>(){{
            add(new ConfigValue(ConfigPaths.Recipes + "." + ArrowNames.Multishot + ".ArrowCount", 3));
            add(new ConfigValue(ConfigPaths.Recipes + "." + ArrowNames.Multishot + ".DelayBetweenShots", 4));
        }});
    }

    @Override
    protected void loadDetails(FileConfiguration config) {
        ArrayList<ConfigValue> configValues = getConfigValues();
        arrowCount = Convert.Convert(Integer.class, configValues.get(0).getValue(config));
        delay = Convert.Convert(Double.class, configValues.get(1).getValue(config));
    }

    @Override
    public void onShoot(EntityShootBowEvent event) {
        for(int i=1; i<arrowCount; i++){
            Bukkit.getScheduler().scheduleSyncDelayedTask(LorinthsCraftArrows.instance, () -> {
                event.getEntity().launchProjectile(Arrow.class);
            }, (long) delay * i);
        }
    }

    @Override
    public void onEntityHit(ProjectileHitEvent event) {

    }

    @Override
    public void onBlockHit(ProjectileHitEvent event) {

    }

}
