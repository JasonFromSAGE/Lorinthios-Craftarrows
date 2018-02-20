package me.lorinth.craftarrows.Arrows;

import me.lorinth.craftarrows.Constants.ArrowNames;
import me.lorinth.craftarrows.Constants.ConfigPaths;
import me.lorinth.craftarrows.LorinthsCraftArrows;
import me.lorinth.craftarrows.Objects.ConfigValue;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;

import java.util.ArrayList;

public class LightningArrowVariant extends ArrowVariant{

    private int count;

    public LightningArrowVariant(FileConfiguration config){
        super(config, ConfigPaths.Recipes + ".", ArrowNames.Lightning, new ArrayList<ConfigValue>(){{
            add(new ConfigValue(ConfigPaths.Recipes + "." + ArrowNames.Lightning + ".Count", 2));
        }});
    }

    @Override
    protected void loadDetails(FileConfiguration config) {
        ArrayList<ConfigValue> configValues = getConfigValues();
        count = (int) configValues.get(0).getValue(config);
    }

    @Override
    public void onShoot(EntityShootBowEvent event) {

    }

    @Override
    public void onEntityHit(ProjectileHitEvent event) {
        strikeLightning(event.getHitEntity().getLocation());
    }

    @Override
    public void onBlockHit(ProjectileHitEvent event) {
        strikeLightning(event.getHitBlock().getLocation());
    }

    private void strikeLightning(Location location){
        for(int i=0; i<count; i++){
            Bukkit.getScheduler().runTaskLater(LorinthsCraftArrows.instance, () -> {
                location.getWorld().strikeLightning(location);
            }, 4*i);
        }
    }
}