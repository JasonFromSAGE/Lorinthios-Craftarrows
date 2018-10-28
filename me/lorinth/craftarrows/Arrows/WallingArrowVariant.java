package me.lorinth.craftarrows.Arrows;

import me.lorinth.craftarrows.Constants.ArrowNames;
import me.lorinth.craftarrows.Constants.ConfigPaths;
import me.lorinth.craftarrows.LorinthsCraftArrows;
import me.lorinth.craftarrows.Objects.ConfigValue;
import me.lorinth.craftarrows.Util.Convert;
import me.lorinth.craftarrows.Util.DirectionHelper;
import me.lorinth.craftarrows.Util.OutputHandler;
import me.lorinth.craftarrows.Util.TryParse;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class WallingArrowVariant extends ArrowVariant{

    private boolean cleanUp;
    private int delay;
    private ArrayList<Material> blocks;
    private Random random = new Random();
    private Vector wallSize;
    private HashMap<Entity, DirectionHelper.Direction> projectileDirections = new HashMap<>();

    public WallingArrowVariant(FileConfiguration config){
        super(config, ConfigPaths.Recipes + ".", ArrowNames.Walling, new ArrayList<ConfigValue>(){{
            add(new ConfigValue(ConfigPaths.Recipes + "." + ArrowNames.Walling + ".CleanUp", true));
            add(new ConfigValue(ConfigPaths.Recipes + "." + ArrowNames.Walling + ".CleanUpDelay", 100));
            add(new ConfigValue<List<String>>(ConfigPaths.Recipes + "." + ArrowNames.Walling + ".Blocks", new ArrayList<String>(){{
                add("DIRT");
                add("STONE");
                add("COBBLESTONE");
            }}));
            add(new ConfigValue(ConfigPaths.Recipes + "." + ArrowNames.Walling + ".Wall.Width", 5));
            add(new ConfigValue(ConfigPaths.Recipes + "." + ArrowNames.Walling + ".Wall.Length", 2));
            add(new ConfigValue(ConfigPaths.Recipes + "." + ArrowNames.Walling + ".Wall.Height", 3));
        }});
    }

    @Override
    protected void loadDetails(FileConfiguration config) {
        ArrayList<ConfigValue> configValues = getConfigValues();
        cleanUp = (boolean) configValues.get(0).getValue(config);
        delay = Convert.Convert(Integer.class, configValues.get(1).getValue(config));
        blocks = new ArrayList<>();
        for(String material : (List<String>) (configValues.get(2).getValue(config))){
            if(TryParse.parseMaterial(material))
                blocks.add(Material.valueOf(material));
            else
                OutputHandler.PrintError("Material, " + OutputHandler.HIGHLIGHT + material + OutputHandler.ERROR + " is not a valid material (in " + ConfigPaths.Recipes + "." + ArrowNames.Walling + ".Blocks" + ")");
        }

        int width = Convert.ConvertToInteger(configValues.get(3).getValue(config));
        int length = Convert.ConvertToInteger(configValues.get(4).getValue(config));
        int height = Convert.ConvertToInteger(configValues.get(5).getValue(config));

        wallSize = new Vector(width, height, length);
    }

    @Override
    public void onShoot(EntityShootBowEvent event){
        projectileDirections.put(event.getProjectile(), DirectionHelper.getCardinalDirection(event.getEntity()));
    }

    @Override
    public void onEntityHit(ProjectileHitEvent event) {
        makeWall(projectileDirections.remove(event.getEntity()), event.getHitEntity().getLocation().getBlock());
    }

    @Override
    public void onBlockHit(ProjectileHitEvent event) {
        makeWall(projectileDirections.remove(event.getEntity()), event.getHitBlock());
    }

    private Material getNextMaterial(){
        return blocks.get(random.nextInt(blocks.size()));
    }

    private void makeWall(DirectionHelper.Direction direction, Block block){
        while(block.getType() != Material.AIR){
            block = block.getRelative(0, 1, 0);
        }

        int startX = 0;
        int endX = 0;
        int startZ = 0;
        int endZ = 0;
        int height = wallSize.getBlockY();

        switch(direction){
            case NORTH:
                startX = block.getX() + 1 + wallSize.getBlockX()/2;
                endX = startX - wallSize.getBlockX();
                startZ = block.getZ();
                endZ = startZ - wallSize.getBlockZ();
                break;
            case SOUTH:
                startX = block.getX() - wallSize.getBlockX()/2;
                endX = startX + wallSize.getBlockX();
                startZ = block.getZ();
                endZ = startZ + wallSize.getBlockZ();
                break;
            case EAST:
                startX = block.getX();
                endX = startX + wallSize.getBlockZ();
                startZ = block.getZ() - wallSize.getBlockX()/2;
                endZ = startZ + wallSize.getBlockX();
                break;
            case WEST:
                startX = block.getX();
                endX = startX - wallSize.getBlockZ();
                startZ = block.getZ() + 1 + wallSize.getBlockX()/2;
                endZ = startZ - wallSize.getBlockX();
                break;
        }

        final ArrayList<Block> blocks = new ArrayList<>();

        for(int x=Math.min(startX, endX); x< Math.max(startX, endX); x++){
            for(int z=Math.min(startZ, endZ); z< Math.max(startZ, endZ); z++){
                for(int y =block.getY(); y<block.getY() + height; y++){
                    Block target = block.getWorld().getBlockAt(x, y, z);
                    if(target.getType() == Material.AIR){
                        blocks.add(target);
                        target.setType(getNextMaterial());
                    }
                }
            }
        }

        if(cleanUp){
            Bukkit.getScheduler().runTaskLater(LorinthsCraftArrows.instance, () -> {
                for(Block target : blocks)
                    target.setType(Material.AIR);
            }, delay);
        }
    }
}
