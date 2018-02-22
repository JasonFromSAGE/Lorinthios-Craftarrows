package me.lorinth.craftarrows.Arrows;

import me.lorinth.craftarrows.Constants.ArrowNames;
import me.lorinth.craftarrows.Constants.ConfigPaths;
import me.lorinth.craftarrows.LorinthsCraftArrows;
import me.lorinth.craftarrows.Objects.ConfigValue;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.WitherSkull;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;

public class WitherArrowVariant extends ArrowVariant{

    private boolean breakBlocks = false;

    public WitherArrowVariant(FileConfiguration config){
        super(config, ConfigPaths.Recipes + ".", ArrowNames.Wither, new ArrayList<ConfigValue>(){{
            add(new ConfigValue(ConfigPaths.Recipes + "." + ArrowNames.Wither + ".BreakBlocks", false));
        }});
    }

    @Override
    protected void loadDetails(FileConfiguration config) {
        ArrayList<ConfigValue> configValues = getConfigValues();
        breakBlocks = (boolean) configValues.get(0).getValue(config);
    }

    @Override
    public void onShoot(EntityShootBowEvent event){
        Projectile projectile = event.getEntity().launchProjectile(WitherSkull.class);
        projectile.setMetadata("BreakBlocks", new FixedMetadataValue(LorinthsCraftArrows.instance, breakBlocks));
        event.setProjectile(projectile);
    }

    @Override
    public void onEntityHit(ProjectileHitEvent event) {

    }

    @Override
    public void onBlockHit(ProjectileHitEvent event) {

    }
}