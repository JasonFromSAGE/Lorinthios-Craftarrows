package me.lorinth.craftarrows;

import me.lorinth.craftarrows.Arrows.*;
import me.lorinth.craftarrows.Constants.ArrowNames;
import me.lorinth.craftarrows.Listener.CraftArrowListener;
import me.lorinth.craftarrows.Listener.UpdaterEventListener;
import me.lorinth.craftarrows.Objects.*;
import me.lorinth.craftarrows.Util.OutputHandler;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;

import java.io.*;
import java.util.HashMap;
import java.util.Set;

public class LorinthsCraftArrows extends JavaPlugin {

    public static LorinthsCraftArrows instance;
    public static Properties properties = new Properties();
    private YamlConfiguration skeletonArrows;
    private Updater updater;
    private static HashMap<String, ArrowVariant> arrowVariantsByItemName = new HashMap<>();
    private static HashMap<String, ArrowVariant> arrowVariantsByName = new HashMap<>();
    private static RandomCollection<ItemStack> randomArrowDrops;
    private static RandomCollection<ArrowVariant> randomArrowVariant;
    private ArrowDropData arrowDropData;

    @Override
    public void onEnable(){
        instance = this;
        randomArrowDrops = new RandomCollection<>();
        randomArrowVariant = new RandomCollection<>();

        firstRun();
        loadConfig();
        checkForNewArrows();
        loadArrows();
        loadArrowDropData();
        checkAutoUpdates();
        Bukkit.getPluginManager().registerEvents(new CraftArrowListener(), this);
        Bukkit.getPluginManager().registerEvents(new UpdaterEventListener(updater), this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
        if(commandLabel.equalsIgnoreCase("lca")){
            if(args.length > 0){
                if(args[0].equalsIgnoreCase("reload")){
                    onEnable();
                }
            }
        }
        return true;
    }

    private void loadConfig(){
        YamlConfiguration config = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "config.yml"));
        properties.UsePermissions = config.getBoolean("UsePermissions");
        properties.InfinityBowWorks = config.getBoolean("InfinityBowWorks");
        properties.AutoUpdate = config.getBoolean("AutoUpdate");
        properties.SkeletonCanShootArrow = config.getBoolean("SkeletonCanShootArrow");
        properties.SkeletonsDropArrows = config.getBoolean("SkeletonsDropArrows");
    }

    public static ArrowVariant getArrowVariantForItemName(String name){
        if(arrowVariantsByItemName.containsKey(name))
            return arrowVariantsByItemName.get(name);
        return null;
    }

    private void checkAutoUpdates(){
        if(properties.AutoUpdate)
            updater = new Updater(this, getFile(), Updater.UpdateType.DEFAULT);
        else
            updater = new Updater(this, getFile(), Updater.UpdateType.NO_DOWNLOAD);
    }

    public static ItemStack getRandomArrowDrop(){
        ItemStack drop = randomArrowDrops.next().clone();
        drop.setAmount(instance.arrowDropData.getValue());
        return drop;
    }

    public static ArrowVariant getRandomArrowVariant(){
        return randomArrowVariant.next();
    }

    private void firstRun(){
        try{
            File arrowEffects = new File(getDataFolder(), "ArrowEffects.yml");
            File skeletonArrows = new File(getDataFolder(), "SkeletonArrows.yml");
            File config = new File(getDataFolder(), "config.yml");

            if(!arrowEffects.exists()) {
                arrowEffects.getParentFile().mkdirs();
                copy(getResource("ArrowEffects.yml"), arrowEffects);
            }
            if(!skeletonArrows.exists()){
                skeletonArrows.getParentFile().mkdirs();
                copy(getResource("SkeletonArrows.yml"), skeletonArrows);
            }
            if(!config.exists()){
                config.getParentFile().mkdirs();
                copy(getResource("config.yml"), config);
            }
        }
        catch(Exception exception){
            OutputHandler.PrintException("Problem with first run", exception);
        }

    }

    private void copy(InputStream in, File file) {
        try {
            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while((len=in.read(buf))>0){
                out.write(buf,0,len);
            }
            out.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Tuple<Integer, Integer> getShootAndDropChance(String name){
        return new Tuple<>(
                skeletonArrows.getInt("ShootChance." + name),
                skeletonArrows.getInt("DropChance." + name)
        );
    }

    private void checkForNewArrows(){
        File file = new File(getDataFolder(), "ArrowEffects.yml");
        if(file.exists()){
            YamlConfiguration config = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "ArrowEffects.yml"));
            FileConfiguration data = YamlConfiguration.loadConfiguration(getTextResource("ArrowEffects.yml"));

            Set<String> currentRecipes = config.getConfigurationSection("Recipes").getKeys(false);
            Set<String> updatedRecipes = data.getConfigurationSection("Recipes").getKeys(false);
            for(String key : updatedRecipes){
                if(!currentRecipes.contains(key)){
                    setConfigValues(data, config, "Recipes." + key);
                }
            }

            config.setDefaults(data);
            try{
                config.save(file);
            }
            catch(Exception exception){
                OutputHandler.PrintException("Error while updating config with new arrows", exception);
            }
        }
    }

    private void setConfigValues(FileConfiguration from, FileConfiguration to, String keyPath){
        for(String key : from.getConfigurationSection(keyPath).getKeys(true)){
            if(from.get(keyPath + "." + key) != null)
                to.set(keyPath + "." + key, from.get(keyPath + "." + key));
        }
    }

    private void loadArrows(){
        File arrowFile = new File(getDataFolder(), "ArrowEffects.yml");
        File skeletonFile = new File(getDataFolder(), "SkeletonArrows.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(arrowFile);
        skeletonArrows = YamlConfiguration.loadConfiguration(skeletonFile);

        addVariant(new PotionArrowVariant(config, ArrowNames.Blinding, PotionEffectType.BLINDNESS));
        addVariant(new BloodArrowVariant(config));
        addVariant(new PotionArrowVariant(config, ArrowNames.Confusion, PotionEffectType.CONFUSION));
        addVariant(new CoolingArrowVariant(config));
        addVariant(new PotionArrowVariant(config, ArrowNames.Crippling, PotionEffectType.SLOW));
        addVariant(new ExplosiveArrowVariant(config));
        addVariant(new FireArrowVariant(config));
        addVariant(new ForcefieldArrowVariant(config));
        addVariant(new HomingArrowVariant(config));
        addVariant(new IceArrowVariant(config));
        addVariant(new LightningArrowVariant(config));
        addVariant(new MedicArrowVariant(config));
        addVariant(new MultishotArrowVariant(config));
        addVariant(new NetArrowVariant(config));
        addVariant(new PiercingArrowVariant(config));
        addVariant(new PotionArrowVariant(config, ArrowNames.Poison, PotionEffectType.POISON));
        addVariant(new PullArrowVariant(config));
        addVariant(new PushArrowVariant(config));
        addVariant(new RazorArrowVariant(config));
        addVariant(new ShuffleArrowVariant(config));
        addVariant(new SniperArrowVariant(config));
        addVariant(new SoundArrowVariant(config));
        addVariant(new TeleportArrowVariant(config));
        addVariant(new TntArrowVariant(config));
        addVariant(new TorchArrowVariant(config));
        addVariant(new VortexArrowVariant(config));
        addVariant(new VolleyArrowVariant(config));
        addVariant(new WallingArrowVariant(config));
        addVariant(new WaterArrowVariant(config));
        addVariant(new PotionArrowVariant(config, ArrowNames.Weakness, PotionEffectType.WEAKNESS));
        addVariant(new WitherArrowVariant(config));

        //Add normal arrow chances
        randomArrowVariant.add(skeletonArrows.getInt("ShootChance.Normal"), null);
        randomArrowDrops.add(skeletonArrows.getInt("DropChance.Normal"), new ItemStack(Material.ARROW));
    }

    private void addVariant(ArrowVariant variant){
        if(!variant.getRecipe().isDisabled()) {
            arrowVariantsByItemName.put(variant.getRecipe().getItemName().trim(), variant);
            arrowVariantsByName.put(variant.getName().trim(), variant);

            //Add to random collections
            Tuple<Integer, Integer> chances = getShootAndDropChance(variant.getName().trim());
            randomArrowVariant.add(chances.A, variant);
            randomArrowDrops.add(chances.B, variant.getRecipe().getItem());
        }
    }

    private void loadArrowDropData(){
        arrowDropData = new ArrowDropData();
        arrowDropData.Amount = skeletonArrows.getInt("Amount");
        arrowDropData.DropFixedAmount = skeletonArrows.getBoolean("DropFixedAmount");
        arrowDropData.Min = skeletonArrows.getInt("Min");
        arrowDropData.Max = skeletonArrows.getInt("Max");
    }
}
