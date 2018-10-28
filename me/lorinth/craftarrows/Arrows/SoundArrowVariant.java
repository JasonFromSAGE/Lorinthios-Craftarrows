package me.lorinth.craftarrows.Arrows;

import me.lorinth.craftarrows.Constants.ArrowNames;
import me.lorinth.craftarrows.Constants.ConfigPaths;
import me.lorinth.craftarrows.LorinthsCraftArrows;
import me.lorinth.craftarrows.Objects.ConfigValue;
import me.lorinth.craftarrows.Util.Convert;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;

import java.util.ArrayList;
import java.util.List;

public class SoundArrowVariant extends ArrowVariant{

    private ArrayList<String> sounds;
    private int range;

    public SoundArrowVariant(FileConfiguration config){
        super(config, ConfigPaths.Recipes + ".", ArrowNames.Sound, new ArrayList<ConfigValue>(){{
            add(new ConfigValue(ConfigPaths.Recipes + "." + ArrowNames.Sound + ".Sounds", new ArrayList<String>(){{
                add("ENTITY_ENDERDRAGON_DEATH");
                add("ENTITY_ENDERMEN_SCREAM");
                add("ENTITY_BAT_TAKEOFF");
                add("ENTITY_GHAST_SCREAM");
            }}));
            add(new ConfigValue(ConfigPaths.Recipes + "." + ArrowNames.Sound + ".Range", -1));
        }});
    }

    @Override
    protected void loadDetails(FileConfiguration config) {
        ArrayList<ConfigValue> configValues = getConfigValues();
        sounds = (ArrayList<String>) configValues.get(0).getValue(config);
        range = Convert.Convert(Integer.class, configValues.get(1).getValue(config));
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
        long delay = 0;
        if(range == -1){
            for(String sound : sounds){
                Sound s = Sound.valueOf(sound);
                Bukkit.getScheduler().scheduleSyncDelayedTask(LorinthsCraftArrows.instance, () -> loc.getWorld().playSound(loc, s, 100.0F, 1.0F), delay);

                delay = delay + 10;
            }
        }
        else{
            for(Player player : loc.getWorld().getPlayers()){
                if(player.getLocation().distanceSquared(loc) < (range * range)){
                    delay = 0;
                    for(String sound : sounds){
                        Sound s = Sound.valueOf(sound);
                        Bukkit.getScheduler().scheduleSyncDelayedTask(LorinthsCraftArrows.instance, () -> player.playSound(loc, s, 100.0F, 1.0F), delay);

                        delay = delay + 10;
                    }
                }
            }
        }

    }
}
