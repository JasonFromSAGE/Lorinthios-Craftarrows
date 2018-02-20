package me.lorinth.craftarrows.Listener;

import me.lorinth.craftarrows.Arrows.ArrowVariant;
import me.lorinth.craftarrows.Arrows.MedicArrowVariant;
import me.lorinth.craftarrows.Constants.MetadataTags;
import me.lorinth.craftarrows.LorinthsCraftArrows;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

public class CraftArrowListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onBowShoot(EntityShootBowEvent event){
        if(event.getProjectile() instanceof Arrow){
            Arrow arrow = (Arrow) event.getProjectile();

            if(event.getEntity() instanceof Player) {
                ItemStack arrowItem = getArrowBeingShot((Player) event.getEntity());
                if(arrowItem != null && arrowItem.hasItemMeta()) {
                    ArrowVariant variant = LorinthsCraftArrows.getArrowVariantForName(arrowItem.getItemMeta().getDisplayName().trim());
                    if(variant != null) {
                        arrow.setMetadata(MetadataTags.ArrowVariant, new FixedMetadataValue(LorinthsCraftArrows.instance, variant));
                        variant.onShoot(event);
                    }
                }
            }
            if(event.getEntity() instanceof Skeleton){
                //Randomly select an arrow
            }
        }
    }

    private ItemStack getArrowBeingShot(Player player){
        for(ItemStack item : player.getInventory().getContents())
            if(item != null && item.getType() == Material.ARROW)
                return item;
        return null;
    }

    @EventHandler(ignoreCancelled = true)
    public void onProjectileHitEntity(EntityDamageByEntityEvent event){
        if(event.getDamager() instanceof Arrow) {
            if (event.getDamager() instanceof Arrow && event.getDamager().hasMetadata(MetadataTags.ArrowVariant) && event.getEntity() instanceof LivingEntity) {
                LivingEntity entity = (LivingEntity) event.getEntity();
                ArrowVariant variant = event.getDamager().getMetadata(MetadataTags.ArrowVariant) != null ? (ArrowVariant) event.getDamager().getMetadata(MetadataTags.ArrowVariant).get(0).value() : null;
                if(variant instanceof MedicArrowVariant)
                    ((MedicArrowVariant) variant).onEntityHit(event, entity);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onProjectileHit(ProjectileHitEvent event){
        Projectile projectile = event.getEntity();
        if(projectile instanceof Arrow && projectile.hasMetadata(MetadataTags.ArrowVariant)){
            Entity entity = event.getHitEntity();
            Block block = event.getHitBlock();

            ArrowVariant variant = projectile.getMetadata(MetadataTags.ArrowVariant) != null ? (ArrowVariant) projectile.getMetadata(MetadataTags.ArrowVariant).get(0).value() : null;

            if(variant != null){
                if(entity != null)
                    variant.onEntityHit(event);
                else if(block != null)
                    variant.onBlockHit(event);
            }
        }
        projectile.remove();
    }

    @EventHandler(ignoreCancelled = true)
    public void onSkeletonDeath(EntityDeathEvent event){
        if(event.getEntity() instanceof Player){
            //Add random arrow and count to drops
            //event.getDrops();
        }
    }

}
