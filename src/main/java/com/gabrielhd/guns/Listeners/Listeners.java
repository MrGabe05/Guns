package com.gabrielhd.guns.Listeners;

import com.gabrielhd.guns.Enums.*;
import com.gabrielhd.guns.Guns.Armor;
import com.gabrielhd.guns.Guns.Explosive;
import com.gabrielhd.guns.Guns.Guns;
import com.gabrielhd.guns.Guns.Medicine;
import com.gabrielhd.guns.GunsPlugin;
import com.gabrielhd.guns.Manager.ConfigManager;
import com.gabrielhd.guns.Utils.CustomStack;
import com.gabrielhd.guns.Utils.NBT.VersionMatcher;
import com.gabrielhd.guns.Utils.NBT.VersionWrapper;
import com.gabrielhd.guns.Utils.NBTItem;
import com.gabrielhd.guns.Utils.Utils;
import com.google.common.collect.Lists;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Listeners implements Listener {

    private final VersionWrapper WRAPPER = new VersionMatcher().match();
    private final List<TNTPrimed> tnts = Lists.newArrayList();

    @EventHandler
    public void onInteractEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        if(item != null && item.getType() != Material.AIR) {
            NBTItem nbtItem = new NBTItem(item);
            if(nbtItem.hasKey("Weapon") && nbtItem.getBoolean("Weapon") && nbtItem.hasKey("WeaponType")) {
                Guns guns = GunsPlugin.getWeaponManager().getGun(nbtItem.getString("WeaponType"));
                if(guns != null) {
                    if(guns.isUsePermission() && !guns.getPermission().equalsIgnoreCase("") && !player.hasPermission(guns.getPermission())) {
                        player.sendMessage(GunsPlugin.Color(ConfigManager.getSettings().getString("Messages.NoPermissions")));
                        return;
                    }
                    if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                        if(event.getAction() == Action.RIGHT_CLICK_BLOCK && item.getType().isBlock()) {
                            event.setCancelled(true);
                        }
                        if(guns.getShootDelayList().containsKey(player.getUniqueId()) && System.currentTimeMillis() - guns.getShootDelayList().get(player.getUniqueId()) < guns.getShootDelay()) {
                            return;
                        }
                        this.shootGun(player, guns, nbtItem, guns.getFireType());
                        return;
                    }
                    return;
                }
                return;
            }

            if(nbtItem.hasKey("Explosive") && nbtItem.getBoolean("Explosive") && nbtItem.hasKey("ExplosiveType")) {
                Explosive explosive = GunsPlugin.getWeaponManager().getExplosive(nbtItem.getString("ExplosiveType"));
                if(explosive != null) {
                    event.setCancelled(true);
                    if(explosive.isUsePermission() && !explosive.getPermission().equalsIgnoreCase("") && !player.hasPermission(explosive.getPermission())) {
                        player.sendMessage(GunsPlugin.Color(ConfigManager.getSettings().getString("Messages.NoPermissions")));
                        return;
                    }
                    if(explosive.getExplosiveType() == ExplosiveType.THROWING) {
                        if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
                            if(explosive.getShootDelayList().containsKey(player.getUniqueId()) && System.currentTimeMillis() - explosive.getShootDelayList().get(player.getUniqueId()) < explosive.getShootDelay()) {
                                return;
                            }

                            Vector vector = player.getLocation().getDirection().multiply(explosive.getVelocity());
                            TNTPrimed tnt = player.getWorld().spawn(player.getLocation(), TNTPrimed.class);
                            tnt.setFuseTicks((int)TimeUnit.MILLISECONDS.toSeconds(explosive.getExplosionDelay()) * 20);
                            tnt.setVelocity(vector);

                            this.tnts.add(tnt);

                            if(item.getAmount() > 1) {
                                item.setAmount(item.getAmount()-1);
                            } else {
                                player.getInventory().remove(item);
                            }
                        }
                    }
                }
                return;
            }

            if(nbtItem.hasKey("Medicine") && nbtItem.getBoolean("Medicine") && nbtItem.hasKey("MedicineType")) {
                Medicine medicine = GunsPlugin.getWeaponManager().getMedicine(nbtItem.getString("MedicineType"));
                if(medicine != null) {
                    event.setCancelled(true);
                    if (GunsPlugin.getCooldownPlayers().containsKey(player.getUniqueId()) && System.currentTimeMillis() - GunsPlugin.getCooldownPlayers().get(player.getUniqueId()) < medicine.getCooldown()) {
                        int time = (int) TimeUnit.MILLISECONDS.toSeconds(medicine.getCooldown() - (System.currentTimeMillis() - GunsPlugin.getCooldownPlayers().get(player.getUniqueId())));
                        player.sendMessage(GunsPlugin.Color(ConfigManager.getSettings().getString("Messages.InCooldown").replace("%time%", String.valueOf(time))));
                        return;
                    }

                    if(player.getHealth() < player.getMaxHealth()) {
                        GunsPlugin.getCooldownPlayers().remove(player.getUniqueId());
                        double newHealth = player.getHealth()+ medicine.getHealth();
                        if(newHealth > player.getMaxHealth()) {
                            newHealth = player.getMaxHealth();
                        }

                        player.setHealth(newHealth);
                        player.sendMessage(GunsPlugin.Color(ConfigManager.getSettings().getString("Messages.MedicineUse").replace("%medicine%", medicine.getName()).replace("%health%", String.valueOf((medicine.getHealth()/2)))));

                        if(item.getAmount() > 1) {
                            item.setAmount(item.getAmount()-1);
                        } else {
                            player.getInventory().remove(item);
                        }
                        player.updateInventory();
                        GunsPlugin.getCooldownPlayers().put(player.getUniqueId(), System.currentTimeMillis());
                    }
                }
            }
        }
    }

    @EventHandler
    public void onHeld(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItem(event.getPreviousSlot());
        if(item != null && item.getType() != Material.AIR) {
            NBTItem nbtItem = new NBTItem(item);
            if (item != null && nbtItem.hasKey("Reloading") && nbtItem.getBoolean("Reloading")) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onDropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItemDrop().getItemStack();
        if (item != null && item.getType() != Material.AIR && player.isSneaking()) {
            NBTItem nbtItem = new NBTItem(item);
            if (nbtItem.hasKey("Weapon") && nbtItem.getBoolean("Weapon") && nbtItem.hasKey("WeaponType")) {
                Guns guns = GunsPlugin.getWeaponManager().getGun(nbtItem.getString("WeaponType"));
                if (guns != null) {
                    if(!nbtItem.getBoolean("Reloading")) {
                        if(!this.reloadGun(player, guns, nbtItem)) {
                            event.setCancelled(true);
                        } else {
                            event.getItemDrop().remove();
                        }
                    } else {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    public List<Block> getNearbyBlocks(Location location, int radius) {
        List<Block> blocks = new ArrayList<>();
        for(int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; x++) {
            for(int y = location.getBlockY() - radius; y <= location.getBlockY() + radius; y++) {
                for (int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; z++) {
                    blocks.add(location.getWorld().getBlockAt(x, y, z));
                }
            }
        }
        return blocks;
    }

    @EventHandler
    public void onBlockExplode(BlockExplodeEvent event) {
        Block block = event.getBlock();
        if(block.hasMetadata("Blocks")) {
            List<MetadataValue> value = block.getMetadata("Blocks");
            if(!value.isEmpty()) {
                Explosive explosive = (Explosive) value.get(0).value();
                if(explosive.getBlockToBreak().isEmpty()) {
                    event.blockList().clear();
                    return;
                }
                List<Block> blockToBreak = this.getNearbyBlocks(block.getLocation(), (int)explosive.getRadius()+2);
                event.blockList().removeIf(toBroken -> blockToBreak.contains(toBroken) && !explosive.getBlockToBreak().contains(toBroken.getType()));
                block.setType(Material.AIR);
            }
        }
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        if(event.getEntity() instanceof TNTPrimed) {
            TNTPrimed tnt = (TNTPrimed) event.getEntity();
            if(this.tnts.contains(tnt)) {
                event.blockList().clear();
            }
        }

        if(event.getEntity().hasMetadata("Explosive")) {
            event.blockList().clear();
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if(event.getCause() == EntityDamageEvent.DamageCause.PROJECTILE && event.getDamager() instanceof Projectile) {
            Projectile projectile = (Projectile) event.getDamager();

            Rarity rarity = null;
            if(projectile.hasMetadata("Rarity")) {
                List<MetadataValue> metadataValues = projectile.getMetadata("Rarity");
                if(!metadataValues.isEmpty()) {
                    rarity = Rarity.valueOf((String) metadataValues.get(0).value());
                }
            }

            if(projectile.hasMetadata("Projectile")) {
                List<MetadataValue> value = projectile.getMetadata("Projectile");
                if (!value.isEmpty()) {
                    Guns guns = (Guns) value.get(0).value();
                    if (rarity != null && guns != null && event.getEntity() instanceof LivingEntity) {
                        LivingEntity damaged = (LivingEntity) event.getEntity();
                        double damage;
                        double projectileY = projectile.getLocation().getY();
                        double damagedY = damaged.getLocation().getY();
                        if (projectileY - damagedY > 1.35) {
                            damage = guns.getHeadshotDamage(rarity);
                        } else {
                            damage = guns.getNormalDamage(rarity);
                        }

                        if (damaged instanceof Player) {
                            Player playerDamaged = (Player) damaged;
                            for (ItemStack itemStack : playerDamaged.getInventory().getArmorContents()) {
                                if (itemStack != null && itemStack.getType() != Material.AIR) {
                                    NBTItem nbtItem = new NBTItem(itemStack);
                                    if (nbtItem.hasKey("CustomArmor") && nbtItem.getBoolean("CustomArmor")) {
                                        Armor armor = GunsPlugin.getWeaponManager().getArmor(nbtItem.getString("Armor"));
                                        if (armor != null) {
                                            if (armor.getDiscountType() == DiscountType.AMOUNT) {
                                                damage -= nbtItem.getDouble("Protection");
                                            } else if (armor.getDiscountType() == DiscountType.PERCENTAGE) {
                                                damage -= (damage * (nbtItem.getDouble("Protection") / 100));
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        damaged.damage(damage, (Entity) projectile.getShooter());
                        damaged.setNoDamageTicks(1);

                        Bukkit.getScheduler().runTaskLater(GunsPlugin.getInstance(), () -> damaged.setVelocity(new Vector()), 5L);
                        event.setCancelled(true);
                    }
                }
            }
        }

        if(event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();

            ItemStack helmet = player.getInventory().getHelmet();
            if(helmet != null && helmet.getType() != Material.AIR) {
                NBTItem nbtItem = new NBTItem(helmet);
                if(nbtItem.hasKey("CustomArmor") && nbtItem.getBoolean("CustomArmor")) {
                    nbtItem.setInteger("Durability", nbtItem.getInteger("Durability")-1);

                    if(nbtItem.getInteger("Durability") <= 0) {
                        player.getInventory().setHelmet(null);
                        if(Utils.is1_8()) {
                            player.playSound(player.getLocation(), Sound.valueOf("ITEM_BREAK"), 1.0f, 1.0f);
                        } else {
                            player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0f, 1.0f);
                        }
                    } else {
                        player.getInventory().setHelmet(nbtItem.getItem());
                    }

                    player.updateInventory();
                }
            }

            ItemStack chestplate = player.getInventory().getChestplate();
            if(chestplate != null && chestplate.getType() != Material.AIR) {
                NBTItem nbtItem = new NBTItem(chestplate);
                if(nbtItem.hasKey("CustomArmor") && nbtItem.getBoolean("CustomArmor")) {
                    nbtItem.setInteger("Durability", nbtItem.getInteger("Durability")-1);

                    if(nbtItem.getInteger("Durability") <= 0) {
                        player.getInventory().setChestplate(null);
                        if(Utils.is1_8()) {
                            player.playSound(player.getLocation(), Sound.valueOf("ITEM_BREAK"), 1.0f, 1.0f);
                        } else {
                            player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0f, 1.0f);
                        }
                    } else {
                        player.getInventory().setChestplate(nbtItem.getItem());
                    }

                    player.updateInventory();
                }
            }

            ItemStack leggings = player.getInventory().getLeggings();
            if(leggings != null && leggings.getType() != Material.AIR) {
                NBTItem nbtItem = new NBTItem(leggings);
                if(nbtItem.hasKey("CustomArmor") && nbtItem.getBoolean("CustomArmor")) {
                    nbtItem.setInteger("Durability", nbtItem.getInteger("Durability")-1);

                    if(nbtItem.getInteger("Durability") <= 0) {
                        player.getInventory().setLeggings(null);
                        if(Utils.is1_8()) {
                            player.playSound(player.getLocation(), Sound.valueOf("ITEM_BREAK"), 1.0f, 1.0f);
                        } else {
                            player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0f, 1.0f);
                        }
                    } else {
                        player.getInventory().setLeggings(nbtItem.getItem());
                    }

                    player.updateInventory();
                }
            }

            ItemStack boots = player.getInventory().getBoots();
            if(boots != null && boots.getType() != Material.AIR) {
                NBTItem nbtItem = new NBTItem(boots);
                if(nbtItem.hasKey("CustomArmor") && nbtItem.getBoolean("CustomArmor")) {
                    nbtItem.setInteger("Durability", nbtItem.getInteger("Durability")-1);

                    if(nbtItem.getInteger("Durability") <= 0) {
                        player.getInventory().setBoots(null);
                        if(Utils.is1_8()) {
                            player.playSound(player.getLocation(), Sound.valueOf("ITEM_BREAK"), 1.0f, 1.0f);
                        } else {
                            player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0f, 1.0f);
                        }
                    } else {
                        player.getInventory().setBoots(nbtItem.getItem());
                    }

                    player.updateInventory();
                }
            }
        }
    }

    @EventHandler
    public void onHit(ProjectileHitEvent event) {
        if(event.getEntity().getShooter() != null && event.getEntity().getShooter() instanceof Player) {
            Projectile projectile = event.getEntity();
            Player player = (Player) event.getEntity().getShooter();

            Rarity rarity = null;
            if(projectile.hasMetadata("Rarity")) {
                List<MetadataValue> metadataValues = projectile.getMetadata("Rarity");
                if(!metadataValues.isEmpty()) {
                    rarity = Rarity.valueOf((String) metadataValues.get(0).value());
                }
            }

            if(projectile.hasMetadata("Projectile")) {
                projectile.remove();
            }

            if(rarity != null && projectile.hasMetadata("Explosive")) {
                List<MetadataValue> metadataValues = projectile.getMetadata("Explosive");
                if(!metadataValues.isEmpty()) {
                    Guns guns = (Guns) metadataValues.get(0).value();

                    Location loc = projectile.getLocation();
                    loc.getWorld().playEffect(loc, Effect.EXPLOSION, 5);
                    loc.getWorld().playEffect(loc, Effect.EXPLOSION_HUGE, 5);
                    loc.getWorld().playEffect(loc, Effect.EXPLOSION_LARGE, 5);
                    if (Utils.is1_8()) {
                        loc.getWorld().playSound(loc, Sound.valueOf("EXPLODE"), 3.0f, 1.0f);
                    } else {
                        loc.getWorld().playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 3.0f, 1.0f);
                    }

                    for (Entity entity : event.getEntity().getNearbyEntities(4.0, 4.0, 4.0)) {
                        if (entity instanceof LivingEntity) {
                            LivingEntity livingEntity = (LivingEntity) entity;
                            livingEntity.damage(guns.getNormalDamage(rarity), player);
                        }
                    }
                    projectile.remove();
                }
            }
        }
    }

    @EventHandler
    public void onMoveItem(InventoryMoveItemEvent event) {
        ItemStack item = event.getItem();
        NBTItem nbtItem = new NBTItem(item);
        if(item != null && nbtItem.hasKey("Reloading") && nbtItem.getBoolean("Reloading")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDrag(InventoryDragEvent event) {
        Player player = (Player) event.getWhoClicked();
        if(event.getInventory().equals(player.getInventory())) {
            ItemStack item = event.getCursor();
            NBTItem nbtItem = new NBTItem(item);
            if(item != null && nbtItem.hasKey("Reloading") && nbtItem.getBoolean("Reloading")) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onClickItem(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if(event.getInventory().equals(player.getInventory())) {
            ItemStack item = event.getCurrentItem();
            NBTItem nbtItem = new NBTItem(item);
            if(item != null && nbtItem.hasKey("Reloading") && nbtItem.getBoolean("Reloading")) {
                event.setCancelled(true);
            }
        }
    }

    public static double randomInt(double low, double high) {
        return Math.random() * (high - low) + low;
    }

    public boolean shootGun(Player player, Guns guns, NBTItem nbtItem, FireType fireType) {
        if(guns.getShootDelayList().containsKey(player.getUniqueId())) {
            long oldtime = guns.getShootDelayList().get(player.getUniqueId());
            guns.getShootDelayList().replace(player.getUniqueId(), oldtime, System.currentTimeMillis());
        } else {
            guns.getShootDelayList().put(player.getUniqueId(), System.currentTimeMillis());
        }

        if(nbtItem.getBoolean("Reloading")) {
            return false;
        }
        if(!nbtItem.getBoolean("Armed")) {
            player.getWorld().playSound(player.getLocation(), guns.getNoBulletsSound().getSound(), guns.getNoBulletsSound().getVolume(), 1f);
            if(guns.isAutomaticReload() && !nbtItem.getBoolean("Reloading")) {
                this.reloadGun(player, guns, nbtItem);
            }
            return false;
        }

        if(nbtItem.getInteger("CurrentAmount") <= 0) {
            nbtItem.setBoolean("Armed", false);
            player.getWorld().playSound(player.getLocation(), guns.getNoBulletsSound().getSound(), guns.getNoBulletsSound().getVolume(), 1f);
            if(guns.isAutomaticReload() && !nbtItem.getBoolean("Reloading")) {
                this.reloadGun(player, guns, nbtItem);
            }
            return false;
        }

        if(fireType == FireType.BURST && guns.getGunType() != GunType.EXPLOSIVE) {
            new BukkitRunnable() {
                int count = guns.getProjectileAmount();
                @Override
                public void run() {
                    if(count <= 0 || !Listeners.this.shootGun(player, guns, nbtItem, FireType.SEMIAUTOMATIC)) {
                        this.cancel();
                        return;
                    }
                    count--;
                }
            }.runTaskTimer(GunsPlugin.getInstance(), 0L, TimeUnit.MILLISECONDS.toSeconds((guns.getShootDelay()/guns.getProjectileAmount())) * 3L);
        } else {
            player.getWorld().playSound(player.getLocation(), guns.getShootingSound().getSound(), guns.getShootingSound().getVolume(), 1f);
            nbtItem.setInteger("CurrentAmount", nbtItem.getInteger("CurrentAmount") - 1);
            Vector vector = player.getLocation().getDirection();
            Snowball snow = player.launchProjectile(Snowball.class);
            snow.setBounce(guns.isBounce());
            snow.setShooter(player);
            snow.setMetadata("Rarity", new FixedMetadataValue(GunsPlugin.getInstance(), nbtItem.getString("Rarity")));
            if (guns.getGunType() == GunType.EXPLOSIVE) {
                snow.setMetadata("Explosive", new FixedMetadataValue(GunsPlugin.getInstance(), guns));
            } else {
                snow.setMetadata("Projectile", new FixedMetadataValue(GunsPlugin.getInstance(), guns));
            }
            snow.setVelocity(vector.multiply(4.5));
        }

        Utils.sendActionbar(player, GunsPlugin.Color(ConfigManager.getSettings().getString("Messages.WeaponStatus").replace("%weapon%", guns.getName()).replace("%totalammo%", String.valueOf(this.getAmmoAmount(player, guns))).replace("%currentamount%", String.valueOf(nbtItem.getInteger("CurrentAmount")))));

        if(nbtItem.getInteger("CurrentAmount") <= 0) {
            nbtItem.setBoolean("Armed", false);
            if(guns.isAutomaticReload() && !nbtItem.getBoolean("Reloading")) {
                this.reloadGun(player, guns, nbtItem);
            }
        }

        player.getInventory().setItem(this.getItemSlot(player, nbtItem.getItem()), nbtItem.getItem());
        return true;
    }

    public boolean reloadGun(Player player, Guns guns, NBTItem nbtItem) {
        CustomStack customStack = this.getAmmo(player, guns);
        if(customStack == null) {
            return false;
        }
        ItemStack item = customStack.getItem();
        if((item != null && nbtItem.getInteger("CurrentAmount") < guns.getAmmoCapacity()) && item.getAmount() >= 1) {
            Utils.sendActionbar(player, GunsPlugin.Color(ConfigManager.getSettings().getString("Messages.ReloadingWeapon").replace("%weapon%", guns.getName())));
            int ammoCount = guns.getAmmoCapacity() - nbtItem.getInteger("CurrentAmount");

            if(item.getAmount() > ammoCount) {
                item.setAmount(item.getAmount()-ammoCount);
            } else if(item.getAmount() < ammoCount) {
                int amount = item.getAmount();
                for (int ticks = 3; ticks > 0; ticks--) {
                    if(amount == ammoCount) {
                        break;
                    }

                    if (amount < ammoCount) {
                        CustomStack customStack2 = this.getAmmo(player, guns, customStack.getSlot());
                        if(customStack2 == null) {
                            break;
                        }
                        ItemStack item2 = customStack2.getItem();
                        if (item2 != null && item != item2) {
                            int required = (ammoCount - amount);
                            if (item2.getAmount() > required) {
                                amount += required;

                                item2.setAmount(item2.getAmount() - required);
                            } else {
                                amount += item2.getAmount();

                                player.getInventory().setItem(customStack2.getSlot(), new ItemStack(Material.AIR));
                            }
                        }
                    }
                }

                ammoCount = amount;
                player.getInventory().setItem(customStack.getSlot(), new ItemStack(Material.AIR));
            } else {
                ammoCount = item.getAmount();
                player.getInventory().setItem(customStack.getSlot(), new ItemStack(Material.AIR));
            }
            player.updateInventory();

            nbtItem.setBoolean("Armed", true);
            nbtItem.setBoolean("Reloading", true);
            nbtItem.setInteger("CurrentAmount", nbtItem.getInteger("CurrentAmount") + ammoCount);

            player.setItemInHand(nbtItem.getItem());

            new BukkitRunnable() {
                double count = 0.0;
                @Override
                public void run() {
                    if(nbtItem.getItem() == null || Listeners.this.getItemSlot(player, nbtItem.getItem()) == -1) {
                        this.cancel();
                        return;
                    }
                    if(count >= TimeUnit.MILLISECONDS.toSeconds(guns.getReloadDelay())) {
                        nbtItem.setBoolean("Reloading", false);
                        Utils.sendActionbar(player, GunsPlugin.Color(ConfigManager.getSettings().getString("Messages.WeaponReloaded").replace("%weapon%", guns.getName())));
                        player.getInventory().setItem(Listeners.this.getItemSlot(player, nbtItem.getItem()), nbtItem.getItem());
                        this.cancel();
                        return;
                    }
                    Utils.sendActionbar(player, GunsPlugin.Color(ConfigManager.getSettings().getString("Messages.ReloadingWeapon").replace("%weapon%", guns.getName())));
                    player.getWorld().playSound(player.getLocation(), guns.getReloadingSound().getSound(), guns.getReloadingSound().getVolume(), 1f);
                    count += 0.5;
                }
            }.runTaskTimer(GunsPlugin.getInstance(), 0L, 10L);
            return true;
        }
        if(item == null) {
            Utils.sendActionbar(player, GunsPlugin.Color(ConfigManager.getSettings().getString("Messages.NoBullets").replace("%weapon%", guns.getName())));
        }
        return false;
    }

    public CustomStack getAmmo(Player player, Guns guns) {
        for(int i = 0; i < player.getInventory().getSize(); i++) {
            ItemStack item = player.getInventory().getItem(i);
            if (item != null && item.getType() != Material.AIR) {
                NBTItem ammoNbt = new NBTItem(item);
                if(ammoNbt.hasKey("AmmoType") && ammoNbt.getString("AmmoType").equalsIgnoreCase(guns.getAmmo().getName())) {
                    return new CustomStack(item, i);
                }
            }
        }
        return null;
    }

    public CustomStack getAmmo(Player player, Guns guns, int slot) {
        for(int i = 0; i < player.getInventory().getSize(); i++) {
            if(i == slot) {
                continue;
            }
            ItemStack item = player.getInventory().getItem(i);
            if (item != null && item.getType() != Material.AIR) {
                NBTItem ammoNbt = new NBTItem(item);
                if(ammoNbt.hasKey("AmmoType") && ammoNbt.getString("AmmoType").equalsIgnoreCase(guns.getAmmo().getName())) {
                    return new CustomStack(item, i);
                }
            }
        }
        return null;
    }

    public int getAmmoAmount(Player player, Guns guns) {
        int amount = 0;
        for(int i = 0; i < player.getInventory().getSize(); i++) {
            ItemStack item = player.getInventory().getItem(i);
            if (item != null && item.getType() != Material.AIR) {
                NBTItem ammoNbt = new NBTItem(item);
                if(ammoNbt.hasKey("AmmoType") && ammoNbt.getString("AmmoType").equalsIgnoreCase(guns.getAmmo().getName())) {
                    amount += item.getAmount();
                }
            }
        }
        return amount;
    }

    public int getItemSlot(Player player, ItemStack itemStack) {
        for(int i = 0; i < player.getInventory().getSize(); i++) {
            ItemStack item = player.getInventory().getItem(i);
            if (item != null && item.getType() != Material.AIR) {
                NBTItem nbtItem = new NBTItem(item);
                NBTItem nbtItem2 = new NBTItem(itemStack);
                if(nbtItem.hasKey("UUID") && nbtItem2.hasKey("UUID") && nbtItem.getString("UUID").equalsIgnoreCase(nbtItem2.getString("UUID"))) {
                    return i;
                }
            }
        }
        return -1;
    }
}
