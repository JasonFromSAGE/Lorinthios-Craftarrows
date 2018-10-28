package me.lorinth.craftarrows.Arrows;

import me.lorinth.craftarrows.Constants.ArrowNames;
import me.lorinth.craftarrows.Constants.ConfigPaths;
import me.lorinth.craftarrows.Objects.ConfigValue;
import me.lorinth.craftarrows.Util.Convert;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class JackOArrow extends ArrowVariant{

    private boolean placeBlock = true;
    private double damage = 3.0;
    private boolean changeHelmet = true;
    private boolean dropPreviousHelmet = false;

    public JackOArrow(FileConfiguration config) {
        super(config, ConfigPaths.Recipes + ".", ArrowNames.JackO, new ArrayList<ConfigValue>(){{
            add(new ConfigValue(ConfigPaths.Recipes + "." + ArrowNames.JackO + ".PlaceBlock", true));
            add(new ConfigValue(ConfigPaths.Recipes + "." + ArrowNames.JackO + ".Damage", 3.0));
            add(new ConfigValue(ConfigPaths.Recipes + "." + ArrowNames.JackO + ".ChangeHelmet", true));
            add(new ConfigValue(ConfigPaths.Recipes + "." + ArrowNames.JackO + ".DropPreviousHelmet", false));

            //DropPreviousHelmet
        }});
    }

    @Override
    protected void loadDetails(FileConfiguration config) {
        ArrayList<ConfigValue> configValues = getConfigValues();
        placeBlock = (boolean) configValues.get(0).getValue(config);
        damage = Convert.Convert(Double.class, configValues.get(1).getValue(config));
        changeHelmet = (boolean) configValues.get(2).getValue(config);
    }

    @Override
    public void onShoot(EntityShootBowEvent event) {

    }

    @Override
    public void onEntityHit(ProjectileHitEvent event) {
        Entity entity = event.getHitEntity();
        if(entity instanceof LivingEntity){
            ((LivingEntity) entity).damage(damage);
            if(changeHelmet){
                ItemStack helmet = ((LivingEntity) entity).getEquipment().getHelmet();
                if(helmet != null || helmet.getType() != Material.AIR){
                    if(dropPreviousHelmet){
                        entity.getWorld().dropItemNaturally(entity.getLocation(), helmet);
                    }
                    else{
                        if(entity instanceof Player)
                            ((Player) entity).getInventory().addItem(helmet);
                    }
                }
                ((LivingEntity) entity).getEquipment().setHelmet(new ItemStack(Material.JACK_O_LANTERN));
            }
        }
    }

    @Override
    public void onBlockHit(ProjectileHitEvent event) {
        if(placeBlock){
            Block block = event.getHitBlock();
            while(block.getType() != Material.AIR){
                block = block.getRelative(0, 1, 0);
            }
            block.setType(Material.JACK_O_LANTERN);
        }
    }
}
