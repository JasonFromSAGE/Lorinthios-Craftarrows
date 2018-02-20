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
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;

import java.util.ArrayList;
import java.util.HashMap;

public class FireArrowVariant extends ArrowVariant{

    private boolean burnBlocks;
    private int duration = 60;

    public FireArrowVariant(FileConfiguration config){
        super(config, ConfigPaths.Recipes + ".", ArrowNames.Fire, new ArrayList<ConfigValue>(){{
            add(new ConfigValue(ConfigPaths.Recipes + "." + ArrowNames.Fire + ".BurnBlocks", true));
            add(new ConfigValue(ConfigPaths.Recipes + "." + ArrowNames.Fire + ".Duration", 60));
        }});
    }

    @Override
    protected void loadDetails(FileConfiguration config) {
        ArrayList<ConfigValue> configValues = getConfigValues();
        burnBlocks = (boolean) configValues.get(0).getValue(config);
        if(configValues.size() > 1 && configValues.get(1) != null && configValues.get(1).getValue(config) != null)
            duration = (int) configValues.get(1).getValue(config);
    }

    @Override
    public void onShoot(EntityShootBowEvent event) {

    }

    @Override
    public void onEntityHit(ProjectileHitEvent event) {
        if(event.getHitEntity() instanceof LivingEntity){
            LivingEntity entity = (LivingEntity) event.getHitEntity();
            entity.setFireTicks(duration);
        }
    }

    @Override
    public void onBlockHit(ProjectileHitEvent event) {
        Block block = event.getHitBlock();

        final HashMap<Block, Material> changed = new HashMap<>();
        Location loc = block.getLocation();
        for(int x=-1; x<=1; x++){
            for(int y=-1; y<=1; y++){
                for(int z=-1; z<=1; z++){
                    Block relative = block.getRelative(x, y, z);
                    if(relative.getType() == Material.AIR || relative.getType() == Material.SNOW){
                        changed.put(relative, relative.getType());
                        relative.setType(Material.FIRE);
                    }
                }
            }
        }

        Bukkit.getScheduler().runTaskLater(LorinthsCraftArrows.instance, () -> {
            for(Block changedBlock : changed.keySet()){
                changedBlock.setType(changed.get(changedBlock));
            }
        }, duration);
    }
}
