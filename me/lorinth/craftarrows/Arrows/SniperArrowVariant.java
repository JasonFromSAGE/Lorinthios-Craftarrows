package me.lorinth.craftarrows.Arrows;

import me.lorinth.craftarrows.Constants.ArrowNames;
import me.lorinth.craftarrows.Constants.ConfigPaths;
import me.lorinth.craftarrows.LorinthsCraftArrows;
import me.lorinth.craftarrows.Objects.ConfigValue;
import me.lorinth.craftarrows.Util.Convert;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;

public class SniperArrowVariant extends ArrowVariant{

    private double velocityMultiplier;

    public SniperArrowVariant(FileConfiguration config){
        super(config, ConfigPaths.Recipes + ".", ArrowNames.Sniper, new ArrayList<ConfigValue>(){{
            add(new ConfigValue(ConfigPaths.Recipes + "." + ArrowNames.Sniper + ".VelocityMultiplier", 2.0));
        }});
    }

    @Override
    protected void loadDetails(FileConfiguration config) {
        ArrayList<ConfigValue> configValues = getConfigValues();
        velocityMultiplier = Convert.Convert(Double.class, configValues.get(0).getValue(config));
    }

    @Override
    public void onShoot(EntityShootBowEvent event) {
        Entity projectile = event.getProjectile();
        projectile.setGravity(false);
        Vector velocity = projectile.getVelocity().multiply(velocityMultiplier);
        projectile.setVelocity(velocity);

        new SniperRunnable(projectile, velocity);

    }

    @Override
    public void onEntityHit(ProjectileHitEvent event) {

    }

    @Override
    public void onBlockHit(ProjectileHitEvent event) {

    }

    private class SniperRunnable extends BukkitRunnable{

        private Entity arrow;
        private Vector velocity;
        private int count = 0;
        private int maxCount = 20;

        public SniperRunnable(Entity arrow, Vector velocity){
            this.arrow = arrow;
            this.velocity = velocity;

            this.runTaskTimer(LorinthsCraftArrows.instance, 2, 2);
        }

        @Override
        public void run() {
            if(count >= maxCount)
                this.arrow.remove();
            if(this.arrow.isValid() && ! this.arrow.isDead() && count < maxCount)
                this.arrow.setVelocity(this.velocity);
            else
                this.cancel();

            count++;
        }
    }

}
