package me.lorinth.craftarrows.Arrows;

import me.lorinth.craftarrows.Constants.ArrowNames;
import me.lorinth.craftarrows.Constants.ConfigPaths;
import me.lorinth.craftarrows.Objects.ConfigValue;
import me.lorinth.craftarrows.Util.Convert;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Random;

public class TrickOrTreatArrow extends ArrowVariant{

    private double trickChance = 30.0;
    private double trickDamage = 6.0;
    private int trickHunger = -6;
    private boolean causesFoodPoisoning = true;

    private double treatHealth = 4.0;
    private int treatHunger = 4;

    private Random random = new Random();

    public TrickOrTreatArrow(FileConfiguration config) {
        super(config, ConfigPaths.Recipes + ".", ArrowNames.TrickOrTreat, new ArrayList<ConfigValue>(){{
            add(new ConfigValue(ConfigPaths.Recipes + "." + ArrowNames.TrickOrTreat + ".Trick.Chance", 30.0));
            add(new ConfigValue(ConfigPaths.Recipes + "." + ArrowNames.TrickOrTreat + ".Trick.Damage", 6.0));
            add(new ConfigValue(ConfigPaths.Recipes + "." + ArrowNames.TrickOrTreat + ".Trick.Hunger", -6));
            add(new ConfigValue(ConfigPaths.Recipes + "." + ArrowNames.TrickOrTreat + ".Treat.Health", 4.0));
            add(new ConfigValue(ConfigPaths.Recipes + "." + ArrowNames.TrickOrTreat + ".Treat.Hunger", 4));
        }});
    }

    @Override
    protected void loadDetails(FileConfiguration config) {
        ArrayList<ConfigValue> configValues = getConfigValues();
        trickChance = Convert.Convert(Double.class, configValues.get(0).getValue(config));
        trickDamage = Convert.Convert(Double.class, configValues.get(1).getValue(config));
        trickHunger = Convert.Convert(Integer.class, configValues.get(2).getValue(config));
        treatHealth = Convert.Convert(Double.class, configValues.get(3).getValue(config));
        treatHunger = Convert.Convert(Integer.class, configValues.get(4).getValue(config));
    }

    @Override
    public void onShoot(EntityShootBowEvent event) {

    }

    @Override
    public void onEntityHit(ProjectileHitEvent event) {
        Entity entity = event.getHitEntity();
        if(entity instanceof LivingEntity){
            if(random.nextDouble() * 100 < trickChance){
                //Trick
                ((LivingEntity) entity).damage(trickDamage);
                if(entity instanceof Player){
                    if(causesFoodPoisoning)
                        ((Player) entity).addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 1, 100));
                    ((Player) entity).setFoodLevel(Math.max(0, ((Player) entity).getFoodLevel() + trickHunger));
                }
            }
            else{
                //Treat
                ((LivingEntity) entity).setHealth(Math.max(((LivingEntity) entity).getMaxHealth(), ((LivingEntity) entity).getHealth() + treatHealth));
                if(entity instanceof Player)
                    ((Player) entity).setFoodLevel(Math.max(0, ((Player) entity).getFoodLevel() + treatHunger));
            }
        }
    }

    @Override
    public void onBlockHit(ProjectileHitEvent event) {

    }
}
