package me.lorinth.craftarrows.Listener;

import me.lorinth.craftarrows.Arrows.ArrowVariant;
import me.lorinth.craftarrows.Arrows.CombatArrowVariant;
import me.lorinth.craftarrows.Data.ArrowManager;
import me.lorinth.craftarrows.LorinthsCraftArrows;
import me.lorinth.craftarrows.Util.NmsHelper;
import me.lorinth.craftarrows.WorldGuard.NoCraftArrowFlag;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

import static me.lorinth.craftarrows.LorinthsCraftArrows.properties;

public class CraftArrowListener implements Listener {

    public static ArrayList<Entity> ignoredEntities = new ArrayList<>();
    public static ArrayList<Location> ignoredExplosions = new ArrayList<>();

    @EventHandler(ignoreCancelled = true)
    public void onBowShoot(EntityShootBowEvent event){
        if(event.getProjectile() instanceof Arrow){
            Arrow arrow = (Arrow) event.getProjectile();
            if(event.getEntity() instanceof Player) {
                ItemStack arrowItem = getArrowBeingShot((Player) event.getEntity());
                if(arrowItem != null && arrowItem.hasItemMeta()) {
                    ArrowVariant variant = LorinthsCraftArrows.getArrowVariantForItemName(arrowItem.getItemMeta().getDisplayName().trim());
                    if(properties.UsePermissions && (!event.getEntity().hasPermission("craftarrow." + variant.getName().toLowerCase()) && !event.getEntity().hasPermission("craftarrow.all"))) {
                        event.getEntity().sendMessage(ChatColor.RED + "[CraftArrows] : You don't have permission to shoot, " + variant.getName() + " arrows");
                        event.setCancelled(true);
                        return;
                    }
                    if(variant != null) {
                        ArrowManager.RegisterArrow(arrow, variant);
                        variant.onShoot(event);

                        if(!properties.InfinityBowWorks && ((Player) event.getEntity()).getGameMode() != GameMode.CREATIVE)
                            if(((Player) event.getEntity()).getInventory().getItemInMainHand().containsEnchantment(Enchantment.ARROW_INFINITE))
                                arrowItem.setAmount(arrowItem.getAmount() - 1);
                    }
                }
            }
            else if(properties.SkeletonCanShootArrow){
                ArrowVariant variant = LorinthsCraftArrows.getRandomArrowVariant();
                if(variant != null) {
                    ArrowManager.RegisterArrow(arrow, variant);
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
        if (event.getDamager() instanceof Arrow){
            Entity arrow = event.getDamager();
            Entity target = event.getEntity();
            ArrowVariant variant = ArrowManager.GetVariant(arrow);
            if(variant != null && target instanceof LivingEntity ){
                LivingEntity entity = (LivingEntity) target;

                if(ignoredEntities.contains(entity)) {
                    event.setCancelled(true);
                    return;
                }

                if(NmsHelper.getSimpleVersion() <= 12){
                    if(LorinthsCraftArrows.WorldGuardEnabled) {
                        if (NoCraftArrowFlag.Enabled && NoCraftArrowFlag.IsProtectedRegion(entity.getLocation())) {
                            return;
                        }
                    }
                }

                if (variant instanceof CombatArrowVariant) {
                    ((CombatArrowVariant) variant).onEntityHit(event, (Projectile) event.getDamager(), entity);
                    event.getDamager().remove();
                }

                arrow.remove();
            }
            Double damage = ArrowManager.GetDamage(arrow);
            if(damage != null)
                event.setDamage(damage);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onProjectileHit(ProjectileHitEvent event){
        Projectile projectile = event.getEntity();
        if(projectile instanceof Arrow){
            Arrow arrow = (Arrow) event.getEntity();
            ArrowVariant variant = ArrowManager.GetVariant(arrow);
            if(variant != null) {
                Entity entity = event.getHitEntity();
                Block block = event.getHitBlock();
                if(block == null)
                    block = event.getEntity().getLocation().getBlock();

                if(ignoredEntities.contains(entity)) {
                    return;
                }

                if(NmsHelper.getSimpleVersion() <= 12){
                    if(LorinthsCraftArrows.WorldGuardEnabled) {
                        if (NoCraftArrowFlag.Enabled && NoCraftArrowFlag.IsProtectedRegion(arrow.getLocation())) {
                            return;
                        }
                    }
                }

                if (entity != null)
                    variant.onEntityHit(event);
                else if (block != null)
                    variant.onBlockHit(event);
                arrow.remove();
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onExplosionDamage(EntityDamageEvent event){
        if(event.getCause() == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION ||
                event.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION){
            if(ignoredEntities.contains(event.getEntity())) {
                Location entityLocation = event.getEntity().getLocation();
                for(Location loc : ignoredExplosions){
                    if(loc.distanceSquared(entityLocation) < 25){
                        event.setCancelled(true);
                        return;
                    }
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onSkeletonDeath(EntityDeathEvent event){
        if(event.getEntity() instanceof Skeleton && properties.SkeletonsDropArrows){
            List<ItemStack> drops = event.getDrops();
            for(int i=0; i<drops.size(); i++){
                ItemStack item = drops.get(i);
                if(item != null){
                    if(item.getType() == Material.ARROW){
                        drops.set(i, LorinthsCraftArrows.getRandomArrowDrop());
                    }
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
