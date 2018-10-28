package me.lorinth.craftarrows.Data;

import me.lorinth.craftarrows.Arrows.ArrowVariant;
import me.lorinth.craftarrows.Constants.MetadataTags;
import me.lorinth.craftarrows.LorinthsCraftArrows;
import org.bukkit.entity.Entity;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.HashMap;

public class ArrowManager {

    private static HashMap<String, ArrowVariant> entityArrowVariants = new HashMap<>();
    private static HashMap<String, Double> entityDamage = new HashMap<>();

    public static void RegisterArrow(Entity entity, ArrowVariant variant){
        entity.setMetadata(MetadataTags.ArrowVariant, new FixedMetadataValue(LorinthsCraftArrows.instance,true));
        entityArrowVariants.put(entity.getUniqueId().toString(), variant);
    }

    public static ArrowVariant GetVariant(Entity entity){
        if(entity.hasMetadata(MetadataTags.ArrowVariant))
            return entityArrowVariants.getOrDefault(entity.getUniqueId().toString(), null);
        return null;
    }

    public static void RegisterDamage(Entity entity, double damage){
        entity.setMetadata(MetadataTags.Damage, new FixedMetadataValue(LorinthsCraftArrows.instance, true));
        entityDamage.put(entity.getUniqueId().toString(), damage);
    }

    public static Double GetDamage(Entity entity){
        if(entity.hasMetadata(MetadataTags.Damage))
            return entityDamage.getOrDefault(entity.getUniqueId().toString(), null);
        return null;
    }

    public static void RemoveArrow(Entity entity){
        entityArrowVariants.remove(entity.getUniqueId().toString());
        entityDamage.remove(entity.getUniqueId().toString());

        entity.removeMetadata(MetadataTags.Damage, LorinthsCraftArrows.instance);
        entity.removeMetadata(MetadataTags.ArrowVariant, LorinthsCraftArrows.instance);
    }
}
