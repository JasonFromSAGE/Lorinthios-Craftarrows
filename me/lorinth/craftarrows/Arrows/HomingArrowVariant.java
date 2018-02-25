package me.lorinth.craftarrows.Arrows;

import me.lorinth.craftarrows.Constants.ArrowNames;
import me.lorinth.craftarrows.Constants.ConfigPaths;
import me.lorinth.craftarrows.LorinthsCraftArrows;
import me.lorinth.craftarrows.Objects.ConfigValue;
import me.lorinth.craftarrows.Util.Convert;
import me.lorinth.craftarrows.Util.VectorHelper;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class HomingArrowVariant extends CombatArrowVariant{

    private double velocity;
    private double damage;

    public HomingArrowVariant(FileConfiguration config){
        super(config, ConfigPaths.Recipes + ".", ArrowNames.Homing, new ArrayList<ConfigValue>(){{
            add(new ConfigValue(ConfigPaths.Recipes + "." + ArrowNames.Homing + ".Velocity", 0.6));
            add(new ConfigValue(ConfigPaths.Recipes + "." + ArrowNames.Homing + ".Damage", 5.0));
        }});
    }

    @Override
    protected void loadDetails(FileConfiguration config) {
        ArrayList<ConfigValue> configValues = getConfigValues();
        velocity = Convert.Convert(Double.class, configValues.get(0).getValue(config));
        damage = Convert.Convert(Double.class, configValues.get(1).getValue(config));
    }

    @Override
    public void onShoot(EntityShootBowEvent event) {
        Entity projectile = event.getProjectile();
        projectile.setGravity(false);
        projectile.setVelocity(projectile.getVelocity().normalize().multiply(100));
    }

    @Override
    public void onEntityHit(EntityDamageByEntityEvent event, Projectile projectile, LivingEntity entity){
        if(projectile.getShooter() instanceof LivingEntity){
            event.setCancelled(true);

            HomingArrowRunnable runnable = new HomingArrowRunnable((LivingEntity) projectile.getShooter(), velocity, damage);
            runnable.setTarget(entity);
            runnable.runTaskTimer(LorinthsCraftArrows.instance, 0, 1);
        }
    }

    @Override
    public void onEntityHit(ProjectileHitEvent event) {

    }

    @Override
    public void onBlockHit(ProjectileHitEvent event) {
        if(event.getEntity().getShooter() instanceof LivingEntity) {
            HomingArrowRunnable runnable = new HomingArrowRunnable((LivingEntity) event.getEntity().getShooter(), velocity, damage);
            runnable.runTaskTimer(LorinthsCraftArrows.instance, 0, 5);
            event.getEntity().remove();
        }
    }

    private class HomingArrowRunnable extends BukkitRunnable{

        private Entity target;
        private double targetVelocity;
        private double currentVelocity = 2.0;
        private Arrow arrow;

        public HomingArrowRunnable(LivingEntity entity, double velocity, double damage){
            this.targetVelocity = velocity;

            arrow = entity.launchProjectile(Arrow.class);
            arrow.setMetadata("LCA.Remove", new FixedMetadataValue(LorinthsCraftArrows.instance, true));
            arrow.setMetadata("LCA.Damage", new FixedMetadataValue(LorinthsCraftArrows.instance, damage));
            arrow.setGravity(false);
        }

        @Override
        public void run() {
            if(arrow.isValid()){
                if(currentVelocity < targetVelocity)
                    currentVelocity += 0.1;
                else if(currentVelocity > targetVelocity)
                    currentVelocity -= 0.1;

                if(target != null)
                    arrow.setVelocity(VectorHelper.getDirectionVector(arrow.getLocation(), target.getLocation().add(0, 1.0, 0), currentVelocity));
                else
                    arrow.setVelocity(arrow.getVelocity().normalize().multiply(currentVelocity));
            }
            else{
                cancel();
            }
        }

        public void setTarget(Entity entity){
            target = entity;
        }
    }

}
