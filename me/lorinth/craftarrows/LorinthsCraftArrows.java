package me.lorinth.craftarrows;

import me.lorinth.craftarrows.Arrows.*;
import me.lorinth.craftarrows.Constants.ArrowNames;
import me.lorinth.craftarrows.Listener.CraftArrowListener;
import me.lorinth.craftarrows.Util.OutputHandler;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

public class LorinthsCraftArrows extends JavaPlugin {

    public static LorinthsCraftArrows instance;
    private static HashMap<String, ArrowVariant> arrowVariants = new HashMap<>();

    @Override
    public void onEnable(){
        instance = this;
        firstRun();
        loadArrows();
        Bukkit.getPluginManager().registerEvents(new CraftArrowListener(), this);
    }

    public void onDisable(){

    }

    public static ArrowVariant getArrowVariantForName(String name){
        if(arrowVariants.containsKey(name))
            return arrowVariants.get(name);
        return null;
    }

    private void firstRun(){
        try{
            File arrowEffects = new File(getDataFolder(), "ArrowEffects.yml");

            if(!arrowEffects.exists()) {
                arrowEffects.getParentFile().mkdirs();
                copy(getResource("ArrowEffects.yml"), arrowEffects);
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

    private void loadArrows(){
        File file = new File(getDataFolder(), "ArrowEffects.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        addVariant(new PotionArrowVariant(config, ArrowNames.Blinding, PotionEffectType.BLINDNESS));
        addVariant(new BloodArrowVariant(config));
        addVariant(new PotionArrowVariant(config, ArrowNames.Confusion, PotionEffectType.CONFUSION));
        addVariant(new PotionArrowVariant(config, ArrowNames.Crippling, PotionEffectType.SLOW));
        addVariant(new ExplosiveArrowVariant(config));
        addVariant(new FireArrowVariant(config));
        addVariant(new ForcefieldArrowVariant(config));
        addVariant(new IceArrowVariant(config));
        addVariant(new LightningArrowVariant(config));
        addVariant(new MedicArrowVariant(config));
        addVariant(new NetArrowVariant(config));
        addVariant(new PiercingArrowVariant(config));
        addVariant(new PotionArrowVariant(config, ArrowNames.Poison, PotionEffectType.POISON));
        addVariant(new PullArrowVariant(config));
        addVariant(new PushArrowVariant(config));
        addVariant(new RazorArrowVariant(config));
        addVariant(new ShuffleArrowVariant(config));
        addVariant(new SoundArrowVariant(config));
        addVariant(new TeleportArrowVariant(config));
        addVariant(new TntArrowVariant(config));
        addVariant(new TorchArrowVariant(config));
        addVariant(new VortexArrowVariant(config));
        addVariant(new WaterArrowVariant(config));
        addVariant(new PotionArrowVariant(config, ArrowNames.Weakness, PotionEffectType.WEAKNESS));
        addVariant(new WitherArrowVariant(config));
    }

    private void addVariant(ArrowVariant variant){
        if(!variant.getRecipe().isDisabled())
            arrowVariants.put(variant.getRecipe().getItemName().trim(), variant);
    }

}
