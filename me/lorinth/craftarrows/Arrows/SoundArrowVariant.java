package me.lorinth.craftarrows.Arrows;

import me.lorinth.craftarrows.Constants.ArrowNames;
import me.lorinth.craftarrows.Constants.ConfigPaths;
import me.lorinth.craftarrows.LorinthsCraftArrows;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;

public class SoundArrowVariant extends ArrowVariant{

    public SoundArrowVariant(FileConfiguration config){
        super(config, ConfigPaths.Recipes + ".", ArrowNames.Sound);
    }

    @Override
    protected void loadDetails(FileConfiguration config) {

    }

    @Override
    public void onShoot(EntityShootBowEvent event) {

    }

    @Override
    public void onEntityHit(ProjectileHitEvent event) {
        playSounds(event.getHitEntity().getLocation());
    }

    @Override
    public void onBlockHit(ProjectileHitEvent event) {
        playSounds(event.getHitBlock().getLocation());
    }

    private void playSounds(Location loc){
        loc.getWorld().playSound(loc, Sound.ENTITY_ENDERDRAGON_DEATH, 100.0F, 1.0F);
        Bukkit.getScheduler().scheduleSyncDelayedTask(LorinthsCraftArrows.instance, () -> loc.getWorld().playSound(loc, Sound.ENTITY_ENDERMEN_SCREAM, 100.0F, 1.0F), 10L);
        Bukkit.getScheduler().scheduleSyncDelayedTask(LorinthsCraftArrows.instance, () -> loc.getWorld().playSound(loc, Sound.ENTITY_BAT_TAKEOFF, 100.0F, 1.0F), 20L);
        Bukkit.getScheduler().scheduleSyncDelayedTask(LorinthsCraftArrows.instance, () -> loc.getWorld().playSound(loc, Sound.ENTITY_GHAST_SCREAM, 100.0F, 1.0F), 30L);
    }
}
