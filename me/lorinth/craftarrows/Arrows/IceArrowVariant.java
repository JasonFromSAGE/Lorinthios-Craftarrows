package me.lorinth.craftarrows.Arrows;

import me.lorinth.craftarrows.Constants.ArrowNames;
import me.lorinth.craftarrows.Constants.ConfigPaths;
import me.lorinth.craftarrows.LorinthsCraftArrows;
import me.lorinth.craftarrows.Objects.ConfigValue;
import me.lorinth.craftarrows.Util.Convert;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Arrow;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;

public class IceArrowVariant extends ArrowVariant{

    private boolean cleanUp;
    private int delay;

    public IceArrowVariant(FileConfiguration config){
        super(config, ConfigPaths.Recipes + ".", ArrowNames.Ice, new ArrayList<ConfigValue>(){{
            add(new ConfigValue(ConfigPaths.Recipes + "." + ArrowNames.Ice + ".CleanUpIce", true));
            add(new ConfigValue(ConfigPaths.Recipes + "." + ArrowNames.Ice + ".CleanUpIceDelay", 600));
        }});
    }

    @Override
    protected void loadDetails(FileConfiguration config) {
        ArrayList<ConfigValue> configValues = getConfigValues();
        cleanUp = (boolean) configValues.get(0).getValue(config);
        delay = Convert.Convert(Integer.class, configValues.get(1).getValue(config));
    }

    @Override
    public void onShoot(EntityShootBowEvent event) {
        final Arrow arrow = (Arrow) event.getProjectile();
        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                Location location = arrow.getLocation();
                Block block = location.getBlock();
                Material material = block.getType();
                if (material == Material.WATER) {
                    arrow.remove();
                    freeze(location);
                    cancel();
                    return;
                }
            }

            ;
        };
        runnable.runTaskTimer(LorinthsCraftArrows.instance, 1, 1);
    }

    @Override
    public void onEntityHit(ProjectileHitEvent event) {

    }

    @Override
    public void onBlockHit(ProjectileHitEvent event) {

    }

    private void freeze(Location location){
        HashMap<Block, Material> changed = new HashMap<>();
        Block block = location.getBlock();
        for(int x=-1; x<=1; x++){
            for(int y=-1; y<=2; y++){
                for(int z=-1; z<=1; z++){
                    Block relative = block.getRelative(x, y, z);
                    if(relative.getType() == Material.WATER){
                        changed.put(relative, relative.getType());
                        relative.setType(Material.ICE);
                    }
                }
            }
        }

        if(cleanUp){
            Bukkit.getScheduler().runTaskLater(LorinthsCraftArrows.instance, () -> {
                for(Block changedBlock : changed.keySet()){
                    changedBlock.setType(changed.get(changedBlock));
                }
            }, delay);
        }

    }
}
