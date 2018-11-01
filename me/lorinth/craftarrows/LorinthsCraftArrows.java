package me.lorinth.craftarrows;

import me.lorinth.craftarrows.Arrows.*;
import me.lorinth.craftarrows.Commands.CommandHandler;
import me.lorinth.craftarrows.Constants.ArrowNames;
import me.lorinth.craftarrows.GUI.ArrowRecipeMenu;
import me.lorinth.craftarrows.Listener.CraftArrowListener;
import me.lorinth.craftarrows.Listener.UpdaterEventListener;
import me.lorinth.craftarrows.Npc.NoCraftArrowTrait;
import me.lorinth.craftarrows.Objects.*;
import me.lorinth.craftarrows.Objects.Properties;
import me.lorinth.craftarrows.Util.NmsHelper;
import me.lorinth.craftarrows.Util.OutputHandler;
import me.lorinth.craftarrows.WorldGuard.NoCraftArrowFlag;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;

import java.io.*;
import java.util.*;

public class LorinthsCraftArrows extends JavaPlugin {

    public static LorinthsCraftArrows instance;
    public static Properties properties = new Properties();
    private YamlConfiguration skeletonArrows;
    private Updater updater;
    public static ArrayList<ArrowVariant> ArrowVariantList;
    private static HashMap<String, ArrowVariant> arrowVariantsByItemName;
    private static HashMap<String, ArrowVariant> arrowVariantsByName;

    private static RandomCollection<ItemStack> randomArrowDrops;
    private static RandomCollection<ArrowVariant> randomArrowVariant;
    private ArrowDropData arrowDropData;

    private static ArrayList<Recipe> arrowRecipes = new ArrayList<>();

    public static boolean WorldGuardEnabled = false;

    @Override
    public void onEnable(){
        int version = NmsHelper.getSimpleVersion();
        if(version > 12){
            OutputHandler.PrintError("You are running a version greater than 1.12. Please download the 1.13 version");
            return;
        }

        startup(true);
    }

    private void startup(boolean onEnable){
        instance = this;
        ArrowVariantList = new ArrayList<>();
        arrowVariantsByItemName = new HashMap<>();
        arrowVariantsByName = new HashMap<>();
        randomArrowDrops = new RandomCollection<>();
        randomArrowVariant = new RandomCollection<>();

        firstRun();
        loadConfig();
        checkForNewArrows();
        loadArrows();
        loadArrowDropData();
        //checkAutoUpdates();
        Bukkit.getPluginManager().registerEvents(new CraftArrowListener(), this);
        //Bukkit.getPluginManager().registerEvents(new UpdaterEventListener(updater), this);

        ArrowRecipeMenu.load();

        if(onEnable)
            hookCitizens();

        WorldGuardEnabled = !(getServer().getPluginManager().getPlugin("WorldGuard") == null || getServer().getPluginManager().getPlugin("WorldGuard").isEnabled() == false);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
        if(commandLabel.equalsIgnoreCase("lca")){
            if(args.length > 0) {
                if (args[0].equalsIgnoreCase("reload")) {
                    if (!sender.hasPermission("lca.reload"))
                        OutputHandler.PrintError(sender, "You don't have permission to do that");
                    else {
                        RemoveAllRecipes();
                        startup(false);
                        sender.sendMessage(ChatColor.GREEN + "[LorinthsCraftArrows]: Reloaded!");
                    }
                } else
                    CommandHandler.ProcessCommand(sender, cmd, commandLabel, args);
            }
            else
                sendHelp(sender);
        }
        return true;
    }

    public static void sendHelp(CommandSender sender){
        if(sender.hasPermission("lca.reload"))
            OutputHandler.PrintRawInfo(sender, "/lca reload");
        if(sender.hasPermission("lca.give"))
            OutputHandler.PrintRawInfo(sender, "/lca give <player> <arrow> <count>");
        OutputHandler.PrintRawInfo(sender, "/lca recipes [name] - lookup a recipe by simple name (e.g. Freezing Arrow = freezing)");
    }

    @Override
    public void onLoad(){
        OutputHandler.PrintInfo("Lorinths Craft Arrows : On Load");
        hookWorldGuard();
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

    public static ArrowVariant getArrowVariantBySimpleName(String name){
        return arrowVariantsByName.get(name.toLowerCase());
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
        addVariant(new SplashPotionArrow(config, ArrowNames.BlindingAoe, PotionEffectType.BLINDNESS));
        addVariant(new BloodArrowVariant(config));
        //addVariant(new BoneArrow(config));
        addVariant(new PotionArrowVariant(config, ArrowNames.Confusion, PotionEffectType.CONFUSION));
        addVariant(new SplashPotionArrow(config, ArrowNames.ConfusionAoe, PotionEffectType.CONFUSION));
        addVariant(new CoolingArrowVariant(config));
        addVariant(new PotionArrowVariant(config, ArrowNames.Crippling, PotionEffectType.SLOW));
        addVariant(new PotionArrowVariant(config, ArrowNames.CripplingAoe, PotionEffectType.SLOW));
        addVariant(new ExplosiveArrowVariant(config));
        addVariant(new FireArrowVariant(config));
        addVariant(new ForcefieldArrowVariant(config));
        addVariant(new HomingArrowVariant(config));
        addVariant(new IceArrowVariant(config));
        addVariant(new JackOArrow(config));
        addVariant(new LanceArrow(config));
        addVariant(new PotionArrowVariant(config, ArrowNames.Levitating, PotionEffectType.LEVITATION));
        addVariant(new SplashPotionArrow(config, ArrowNames.LevitatingAoe, PotionEffectType.LEVITATION));
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
        addVariant(new TorchArrowVariant(config));
        addVariant(new VortexArrowVariant(config));
        addVariant(new VolleyArrowVariant(config));
        addVariant(new WallingArrowVariant(config));
        addVariant(new WaterArrowVariant(config));
        addVariant(new PotionArrowVariant(config, ArrowNames.Weakness, PotionEffectType.WEAKNESS));
        addVariant(new WitherArrowVariant(config));

        try {
            config.save(arrowFile);
        }
        catch(Exception e){
            e.printStackTrace();
        }

        //Add normal arrow chances
        randomArrowVariant.add(skeletonArrows.getInt("ShootChance.Normal"), null);
        randomArrowDrops.add(skeletonArrows.getInt("DropChance.Normal"), new ItemStack(Material.ARROW));
    }

    private void addVariant(ArrowVariant variant){
        if(!variant.getRecipe().isDisabled()) {
            ArrowVariantList.add(variant);
            arrowVariantsByItemName.put(variant.getRecipe().getItemName().trim(), variant);
            arrowVariantsByName.put(variant.getName().trim().toLowerCase(), variant);

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

    private void hookCitizens(){
        if(getServer().getPluginManager().getPlugin("Citizens") == null || getServer().getPluginManager().getPlugin("Citizens").isEnabled() == false)
            return;

        //Register your trait with Citizens.
        net.citizensnpcs.api.CitizensAPI.getTraitFactory().registerTrait(net.citizensnpcs.api.trait.TraitInfo.create(NoCraftArrowTrait.class));
    }

    private void hookWorldGuard(){
        if(getServer().getPluginManager().getPlugin("WorldGuard") == null)
            return;

        NoCraftArrowFlag.onLoad();
    }

    public static void AddRecipe(Recipe recipe){
        Bukkit.addRecipe(recipe);
        arrowRecipes.add(recipe);
    }

    private void RemoveAllRecipes(){
        for(Recipe recipe : arrowRecipes){
            RemoveRecipe(recipe);
        }
        arrowRecipes = new ArrayList<>();
    }

    private static void RemoveRecipe(Recipe r){
        List<Recipe> backup = new ArrayList<>();
        {
            // Idk why you change scope, but why not
            Iterator<Recipe> a = instance.getServer().recipeIterator();

            while(a.hasNext()){
                Recipe recipe = a.next();
                ItemStack result = recipe.getResult();
                if(!result.isSimilar(r.getResult())){
                    backup.add(recipe);
                }
            }
        }

        instance.getServer().clearRecipes();
        for (Recipe recipe : backup)
            instance.getServer().addRecipe(recipe);
    }


}
