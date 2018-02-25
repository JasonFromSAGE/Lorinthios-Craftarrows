package me.lorinth.craftarrows.Listener;

import me.lorinth.craftarrows.Arrows.ArrowVariant;
import me.lorinth.craftarrows.Arrows.CombatArrowVariant;
import me.lorinth.craftarrows.Arrows.MedicArrowVariant;
import me.lorinth.craftarrows.Constants.MetadataTags;
import me.lorinth.craftarrows.LorinthsCraftArrows;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.List;

public class CraftArrowListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onBowShoot(EntityShootBowEvent event){
        if(event.getProjectile() instanceof Arrow){
            Arrow arrow = (Arrow) event.getProjectile();

            if(event.getEntity() instanceof Player) {
                ItemStack arrowItem = getArrowBeingShot((Player) event.getEntity());
                if(arrowItem != null && arrowItem.hasItemMeta()) {
                    ArrowVariant variant = LorinthsCraftArrows.getArrowVariantForItemName(arrowItem.getItemMeta().getDisplayName().trim());
                    if(LorinthsCraftArrows.properties.UsePermissions && (!event.getEntity().hasPermission("craftarrow." + variant.getName().toLowerCase()) && !event.getEntity().hasPermission("craftarrow.all"))) {
                        event.getEntity().sendMessage(ChatColor.RED + "[CraftArrows] : You don't have permission to shoot, " + variant.getName() + " arrows");
                        event.setCancelled(true);
                        return;
                    }
                    if(variant != null) {
                        arrow.setMetadata(MetadataTags.ArrowVariant, new FixedMetadataValue(LorinthsCraftArrows.instance, variant));
                        variant.onShoot(event);
                    }
                }
            }
            if(event.getEntity() instanceof Skeleton && LorinthsCraftArrows.properties.SkeletonCanShootArrow){
                ArrowVariant variant = LorinthsCraftArrows.getRandomArrowVariant();
                if(variant != null) {
                    arrow.setMetadata(MetadataTags.ArrowVariant, new FixedMetadataValue(LorinthsCraftArrows.instance, variant));
                    variant.onShoot(event);
                }
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
            if (event.getDamager() instanceof Arrow){
                if(event.getDamager().hasMetadata(MetadataTags.ArrowVariant) && event.getEntity() instanceof LivingEntity)
                {
                    LivingEntity entity = (LivingEntity) event.getEntity();
                    ArrowVariant variant = event.getDamager().getMetadata(MetadataTags.ArrowVariant) != null ? (ArrowVariant) event.getDamager().getMetadata(MetadataTags.ArrowVariant).get(0).value() : null;
                    if (variant instanceof CombatArrowVariant) {
                        ((CombatArrowVariant) variant).onEntityHit(event, (Projectile) event.getDamager(), entity);
                        event.getDamager().remove();
                    }
                }
                if(event.getDamager().hasMetadata("LCA.Damage"))
                    event.setDamage(event.getDamager().getMetadata("LCA.Damage").get(0).asDouble());

            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onProjectileHit(ProjectileHitEvent event){
        Projectile projectile = event.getEntity();
        if(projectile instanceof Arrow){
            if(projectile.hasMetadata(MetadataTags.ArrowVariant)) {
                Entity entity = event.getHitEntity();
                Block block = event.getHitBlock();

                ArrowVariant variant = projectile.getMetadata(MetadataTags.ArrowVariant) != null ? (ArrowVariant) projectile.getMetadata(MetadataTags.ArrowVariant).get(0).value() : null;

                if (variant != null) {
                    if (entity != null)
                        variant.onEntityHit(event);
                    else if (block != null)
                        variant.onBlockHit(event);
                }
                projectile.remove();
            }
            else if(projectile.hasMetadata("LCA.Remove"))
                projectile.remove();
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onSkeletonDeath(EntityDeathEvent event){
        if(event.getEntity() instanceof Skeleton && LorinthsCraftArrows.properties.SkeletonsDropArrows){
            List<ItemStack> drops = event.getDrops();
            for(int i=0; i<drops.size(); i++){
                ItemStack item = drops.get(i);
                if(item.getType() == Material.ARROW){
                    drops.set(i, LorinthsCraftArrows.getRandomArrowDrop());
                }
            }
        }
    }

    public void onEntityExplosion(EntityExplodeEvent event){
        Entity entity = event.getEntity();
        if(entity instanceof WitherSkull){
            if(entity.hasMetadata("BreakBlocks")){
                if(entity.getMetadata("BreakBlocks").get(0).asBoolean())
                    event.blockList().clear();
            }
        }
    }

}
