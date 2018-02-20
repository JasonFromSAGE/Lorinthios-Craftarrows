package me.lorinth.craftarrows.Arrows;

import me.lorinth.craftarrows.Objects.ArrowRecipe;
import me.lorinth.craftarrows.Objects.ConfigValue;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;

import java.util.ArrayList;

public abstract class ArrowVariant {

    private String name;
    private ArrowRecipe recipe;
    private ArrayList<ConfigValue> configValues = new ArrayList<>();

    public ArrowVariant(FileConfiguration config, String path, String name){
        this.name = name;
        recipe = new ArrowRecipe(config, path, name);
        loadDetails(config);
    }

    public ArrowVariant(FileConfiguration config, String path, String name, ArrayList<ConfigValue> configValues){
        this.name = name;
        recipe = new ArrowRecipe(config, path, name);
        this.configValues.addAll(configValues);
        loadDetails(config);
    }

    public String getName(){ return name; }
    public ArrowRecipe getRecipe(){ return recipe; }

    public abstract void onShoot(EntityShootBowEvent event);

    public abstract void onEntityHit(ProjectileHitEvent event);

    public abstract void onBlockHit(ProjectileHitEvent event);

    protected abstract void loadDetails(FileConfiguration config);

    protected ArrayList<ConfigValue> getConfigValues(){
        return configValues;
    }

}
