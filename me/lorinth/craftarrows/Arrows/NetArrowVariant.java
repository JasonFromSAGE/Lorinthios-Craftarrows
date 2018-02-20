package me.lorinth.craftarrows.Arrows;

import me.lorinth.craftarrows.Constants.ArrowNames;
import me.lorinth.craftarrows.Constants.ConfigPaths;
import me.lorinth.craftarrows.LorinthsCraftArrows;
import me.lorinth.craftarrows.Objects.ConfigValue;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;

import java.util.ArrayList;
import java.util.HashMap;

public class NetArrowVariant extends ArrowVariant{

    private boolean cleanUp;
    private int delay;

    public NetArrowVariant(FileConfiguration config){
        super(config, ConfigPaths.Recipes + ".", ArrowNames.Net, new ArrayList<ConfigValue>(){{
            add(new ConfigValue(ConfigPaths.Recipes + "." + ArrowNames.Net + ".CleanUpWebs", 100));
            add(new ConfigValue(ConfigPaths.Recipes + "." + ArrowNames.Net + ".CleanUpWebsDelay", true));
        }});
    }

    @Override
    protected void loadDetails(FileConfiguration config) {
        ArrayList<ConfigValue> configValues = getConfigValues();
        cleanUp = (boolean) configValues.get(0).getValue(config);
        delay = (int) configValues.get(1).getValue(config);
    }

    @Override
    public void onShoot(EntityShootBowEvent event) {

    }

    @Override
    public void onEntityHit(ProjectileHitEvent event) {
        net(event.getHitEntity().getLocation());
    }

    @Override
    public void onBlockHit(ProjectileHitEvent event) {
        net(event.getHitBlock().getLocation());
    }

    private void net(Location location){
        HashMap<Block, Material> changed = new HashMap<>();
        Block block = location.getBlock();
        for(int x=-1; x<=1; x++){
            for(int y=-1; y<=1; y++){
                for(int z=-1; z<=1; z++){
                    Block relative = block.getRelative(x, y, z);
                    if(relative.getType() == Material.AIR && relative.getRelative(0, -1, 0).getType() != Material.AIR){
                        changed.put(relative, relative.getType());
                        relative.setType(Material.WEB);
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