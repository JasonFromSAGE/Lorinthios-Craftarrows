package me.lorinth.craftarrows.Arrows;

import me.lorinth.craftarrows.Objects.ConfigValue;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.ArrayList;

public abstract class CombatArrowVariant extends ArrowVariant{
    public CombatArrowVariant(FileConfiguration config, String path, String name) {
        super(config, path, name);
    }

    public CombatArrowVariant(FileConfiguration config, String path, String name, ArrayList<ConfigValue> configValues) {
        super(config, path, name, configValues);
    }

    public abstract void onEntityHit(EntityDamageByEntityEvent event, Projectile projectile, LivingEntity entity);
}
