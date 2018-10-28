package me.lorinth.craftarrows.Arrows;

import me.lorinth.craftarrows.Constants.ArrowNames;
import me.lorinth.craftarrows.Constants.ConfigPaths;
import me.lorinth.craftarrows.Listener.CraftArrowListener;
import me.lorinth.craftarrows.LorinthsCraftArrows;
import me.lorinth.craftarrows.Objects.ConfigValue;
import me.lorinth.craftarrows.Util.Convert;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;

import java.util.ArrayList;

public class ExplosiveArrowVariant extends ArrowVariant{

    private float power;
    private boolean breakBlocks;
    private boolean setFire = false;

    public ExplosiveArrowVariant(FileConfiguration config){
        super(config, ConfigPaths.Recipes + ".", ArrowNames.Explosive, new ArrayList<ConfigValue>(){{
            add(new ConfigValue(ConfigPaths.Recipes + "." + ArrowNames.Explosive + ".Power", 4.0f));
            add(new ConfigValue(ConfigPaths.Recipes + "." + ArrowNames.Explosive + ".BreakBlocks", true));
            add(new ConfigValue(ConfigPaths.Recipes + "." + ArrowNames.Explosive + ".SetFire", false));
        }});
    }

    @Override
    protected void loadDetails(FileConfiguration config) {
        ArrayList<ConfigValue> configValues = getConfigValues();
        power = Convert.Convert(Float.class, configValues.get(0).getValue(config));
        breakBlocks = (boolean) configValues.get(1).getValue(config);
        setFire = (boolean) configValues.get(2).getValue(config);
    }

    @Override
    public void onShoot(EntityShootBowEvent event) {

    }

    @Override
    public void onEntityHit(ProjectileHitEvent event) {
        Entity entity = event.getHitEntity();
        if(entity instanceof LivingEntity){
            Bukkit.getScheduler().runTaskLater(LorinthsCraftArrows.instance, () -> {
                ((LivingEntity) entity).setNoDamageTicks(15);
                explode(event.getEntity().getLocation());
            }, 1);
        }
        else
            explode(event.getEntity().getLocation());
    }

    @Override
    public void onBlockHit(ProjectileHitEvent event) {
        explode(event.getEntity().getLocation());
    }

    private void explode(Location location){
        CraftArrowListener.ignoredExplosions.add(location);
        location.getWorld().createExplosion(location.getBlockX(), location.getBlockY(), location.getBlockZ(), power, setFire, breakBlocks);

        Bukkit.getScheduler().runTaskLater(LorinthsCraftArrows.instance, () -> {
            CraftArrowListener.ignoredExplosions.remove(location);
        }, 3);
    }

}
