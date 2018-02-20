package me.lorinth.craftarrows.Arrows;

import me.lorinth.craftarrows.Constants.ArrowNames;
import me.lorinth.craftarrows.Constants.ConfigPaths;
import me.lorinth.craftarrows.LorinthsCraftArrows;
import me.lorinth.craftarrows.Objects.ConfigValue;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;

import java.util.ArrayList;

public class WaterArrowVariant extends ArrowVariant{

    private boolean cleanUp;
    private int delay;

    public WaterArrowVariant(FileConfiguration config){
        super(config, ConfigPaths.Recipes + ".", ArrowNames.Water, new ArrayList<ConfigValue>(){{
            add(new ConfigValue(ConfigPaths.Recipes + "." + ArrowNames.Water + ".CleanUpWater", 100));
            add(new ConfigValue(ConfigPaths.Recipes + "." + ArrowNames.Water + ".CleanUpWaterDelay", true));
        }});
    }

    @Override
    protected void loadDetails(FileConfiguration config) {
        ArrayList<ConfigValue> configValues = getConfigValues();
        cleanUp = (boolean) configValues.get(0).getValue(config);
        delay = (int) configValues.get(1).getValue(config);
    }

    @Override
    public void onShoot(EntityShootBowEvent event){

    }

    @Override
    public void onEntityHit(ProjectileHitEvent event) {
        setWater(event.getHitEntity().getLocation().getBlock());
    }

    @Override
    public void onBlockHit(ProjectileHitEvent event) {
        setWater(event.getHitBlock());
    }

    private void setWater(Block block){
        while(block.getType() != Material.AIR){
            block = block.getRelative(0, 1, 0);
        }

        final Block finalBlock = block;
        finalBlock.setType(Material.WATER);

        if(cleanUp){
            Bukkit.getScheduler().runTaskLater(LorinthsCraftArrows.instance, () -> {
                finalBlock.setType(Material.AIR);
            }, delay);
        }
    }
}