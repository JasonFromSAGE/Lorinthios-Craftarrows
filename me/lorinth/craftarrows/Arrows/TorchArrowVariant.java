package me.lorinth.craftarrows.Arrows;

import me.lorinth.craftarrows.Constants.ArrowNames;
import me.lorinth.craftarrows.Constants.ConfigPaths;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.material.Torch;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;

public class TorchArrowVariant extends ArrowVariant{

    public TorchArrowVariant(FileConfiguration config){
        super(config, ConfigPaths.Recipes + ".", ArrowNames.Torch);
    }
    private HashMap<BlockFace, Byte> faces = new HashMap<BlockFace, Byte>(){{
        put(BlockFace.EAST, (byte) 1);
        put(BlockFace.WEST, (byte) 2);
        put(BlockFace.NORTH, (byte) 4);
        put(BlockFace.SOUTH, (byte) 3);
        put(BlockFace.UP, (byte) 5);
    }};

    @Override
    protected void loadDetails(FileConfiguration config) {

    }

    @Override
    public void onShoot(EntityShootBowEvent event) {

    }

    @Override
    public void onEntityHit(ProjectileHitEvent event) {
        lightTorch(event.getHitEntity().getLocation().getBlock());
    }

    @Override
    public void onBlockHit(ProjectileHitEvent event) {
        lightTorch(event.getHitBlock());
    }

    private void lightTorch(Block block){
        for(BlockFace face : faces.keySet()) {
            Block relative = block.getRelative(face);
            if(relative.getType() == Material.AIR) {
                if(face == BlockFace.UP)
                    block.getRelative(face).setType(Material.TORCH);
                else{
                    Block blockRelative = block.getRelative(face);
                    blockRelative.setType(Material.WALL_TORCH);

                    //Set Direction
                    Directional directional = (Directional) blockRelative.getBlockData();
                    directional.setFacing(face);
                    blockRelative.setBlockData(directional);
                }
            }
        }
    }
}