package com.gabrielhd.guns.Manager;

import com.gabrielhd.guns.Enums.*;
import com.gabrielhd.guns.Guns.*;
import com.gabrielhd.guns.GunsPlugin;
import com.gabrielhd.guns.Utils.Utils;
import com.google.common.collect.Lists;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WeaponManager {

    private final Map<String, Guns> guns = new HashMap<>();
    private final Map<String, Ammo> ammo = new HashMap<>();
    private final Map<String, Armor> armors = new HashMap<>();
    private final Map<String, Medicine> medicines = new HashMap<>();
    private final Map<String, Explosive> explosives = new HashMap<>();

    public WeaponManager() {
        this.loadAmmos();
        this.loadGuns();
        this.loadArmors();
        this.loadMedicines();
        this.loadExplosives();
    }

    public Ammo getAmmo(String name) {
        return this.ammo.get(name.toLowerCase());
    }

    public Collection<Ammo> getAmmos() {
        return this.ammo.values();
    }

    public Explosive getExplosive(String name) {
        return this.explosives.get(name.toLowerCase());
    }

    public Collection<Explosive> getExplosives() {
        return this.explosives.values();
    }

    public Medicine getMedicine(String name) {
        return this.medicines.get(name.toLowerCase());
    }

    public Collection<Medicine> getMedicines() {
        return this.medicines.values();
    }

    public Armor getArmor(String name) {
        return this.armors.get(name.toLowerCase());
    }

    public Collection<Armor> getArmors() {
        return this.armors.values();
    }

    public Guns getGun(String name) {
        return this.guns.get(name.toLowerCase());
    }

    public Collection<Guns> getGuns() {
        return this.guns.values();
    }

    public void loadExplosives() {
        this.explosives.clear();

        FileConfiguration expConfig = ConfigManager.getExplosive();
        for(String section : expConfig.getKeys(false)) {
            ItemStack expItem;
            if(Utils.isInt(expConfig.getString(section+".ItemSettings.Explosive.ID"))) {
                expItem = new ItemStack(expConfig.getInt(section+".ItemSettings.Explosive.ID"), 1, (short) expConfig.getInt(section+".ItemSettings.Explosive.Data"));
            } else {
                expItem = new ItemStack(Material.getMaterial(expConfig.getString(section+".ItemSettings.Explosive.ID")), 1, (short) expConfig.getInt(section+".ItemSettings.Explosive.Data"));
            }
            ItemMeta expMeta = expItem.getItemMeta();
            expMeta.setDisplayName(GunsPlugin.Color(expConfig.getString(section+".ItemSettings.Explosive.Name")));
            List<String> newExpLore = Lists.newArrayList();
            for(String lore : expConfig.getStringList(section+".ItemSettings.Explosive.Description")) {
                newExpLore.add(GunsPlugin.Color(lore));
            }
            expMeta.setLore(newExpLore);
            expMeta.spigot().setUnbreakable(true);
            expItem.setItemMeta(expMeta);

            Explosive explosive = new Explosive(section);
            explosive.setUsePermission(expConfig.getBoolean(section+".ItemSettings.RequiredPermission"));
            explosive.setPermission(expConfig.getString(section+".ItemSettings.Permission"));
            explosive.setExplosiveType(ExplosiveType.valueOf(expConfig.getString(section+".ExplosiveSettings.ExplosiveType")));
            explosive.setExplosionDelay(expConfig.getInt(section+".ExplosiveSettings.ExplosionDelay"));
            explosive.setRadius(expConfig.getDouble(section+".ExplosiveSettings.ExplosionRadius"));
            explosive.setShootDelay(expConfig.getInt(section+".ExplosiveSettings.ShootDelay"));
            explosive.setVelocity(expConfig.getInt(section+".ExplosiveSettings.VelocityMuliplier"));
            explosive.setExplosiveItem(expItem);

            if(expConfig.isSet(section+".ExplosiveSettings.BlocksToBreak")) {
                for (String stringBlock : expConfig.getStringList(section + ".ExplosiveSettings.BlocksToBreak")) {
                    if (!stringBlock.equalsIgnoreCase("")) {
                        Material material = Material.getMaterial(stringBlock);
                        if (material != null && !explosive.getBlockToBreak().contains(material)) {
                            explosive.getBlockToBreak().add(material);
                        }
                    }
                }
            }

            this.explosives.put(section.toLowerCase(), explosive);
        }
    }

    public void loadMedicines() {
        this.medicines.clear();

        FileConfiguration mediConfig = ConfigManager.getMedicines();
        for(String section : mediConfig.getKeys(false)) {
            ItemStack mediItem;
            if(Utils.isInt(mediConfig.getString(section+".ItemSettings.ID"))) {
                mediItem = new ItemStack(mediConfig.getInt(section+".ItemSettings.ID"), 1, (short) mediConfig.getInt(section+".ItemSettings.Data"));
            } else {
                mediItem = new ItemStack(Material.getMaterial(mediConfig.getString(section+".ItemSettings.ID")), 1, (short) mediConfig.getInt(section+".ItemSettings.Data"));
            }
            ItemMeta mediMeta = mediItem.getItemMeta();
            mediMeta.setDisplayName(GunsPlugin.Color(mediConfig.getString(section+".ItemSettings.Name")));
            List<String> newExpLore = Lists.newArrayList();
            for(String lore : mediConfig.getStringList(section+".ItemSettings.Description")) {
                newExpLore.add(GunsPlugin.Color(lore));
            }
            mediMeta.setLore(newExpLore);
            mediMeta.spigot().setUnbreakable(true);
            mediItem.setItemMeta(mediMeta);

            Medicine medicine = new Medicine(section);
            medicine.setItemMed(mediItem);
            medicine.setCooldown(mediConfig.getLong(section+".MedicineSettings.Cooldown"));
            medicine.setHealth(mediConfig.getDouble(section+".MedicineSettings.HealthToHeal"));

            this.medicines.put(section.toLowerCase(), medicine);
        }
    }

    public void loadArmors() {
        this.armors.clear();

        FileConfiguration armorConfig = ConfigManager.getArmor();
        for(String section : armorConfig.getKeys(false)) {
            ItemStack helmet = null;
            if(armorConfig.isSet(section+".ItemSettings.Helmet")) {
                if (Utils.isInt(armorConfig.getString(section + ".ItemSettings.Helmet.ID"))) {
                    helmet = new ItemStack(armorConfig.getInt(section + ".ItemSettings.Helmet.ID"), 1, (short) armorConfig.getInt(section + ".ItemSettings.Helmet.Data"));
                } else {
                    helmet = new ItemStack(Material.getMaterial(armorConfig.getString(section + ".ItemSettings.Helmet.ID")), 1, (short) armorConfig.getInt(section + ".ItemSettings.Helmet.Data"));
                }

                ItemMeta helmetItemMeta = helmet.getItemMeta();
                helmetItemMeta.setDisplayName(GunsPlugin.Color(armorConfig.getString(section + ".ItemSettings.Helmet.Name")));
                List<String> newHelmetLore = Lists.newArrayList();
                for (String lore : armorConfig.getStringList(section + ".ItemSettings.Helmet.Description")) {
                    newHelmetLore.add(GunsPlugin.Color(lore));
                }
                helmetItemMeta.setLore(newHelmetLore);
                helmetItemMeta.spigot().setUnbreakable(true);
                helmet.setItemMeta(helmetItemMeta);
            }

            ItemStack chestplate = null;
            if(armorConfig.isSet(section+".ItemSettings.Chestplate")) {
                if (Utils.isInt(armorConfig.getString(section + ".ItemSettings.Chestplate.ID"))) {
                    chestplate = new ItemStack(armorConfig.getInt(section + ".ItemSettings.Chestplate.ID"), 1, (short) armorConfig.getInt(section + ".ItemSettings.Chestplate.Data"));
                } else {
                    chestplate = new ItemStack(Material.getMaterial(armorConfig.getString(section + ".ItemSettings.Chestplate.ID")), 1, (short) armorConfig.getInt(section + ".ItemSettings.Chestplate.Data"));
                }
                ItemMeta chestplateItemMeta = chestplate.getItemMeta();
                chestplateItemMeta.setDisplayName(GunsPlugin.Color(armorConfig.getString(section + ".ItemSettings.Chestplate.Name")));
                List<String> newChestLore = Lists.newArrayList();
                for (String lore : armorConfig.getStringList(section + ".ItemSettings.Chestplate.Description")) {
                    newChestLore.add(GunsPlugin.Color(lore));
                }
                chestplateItemMeta.setLore(newChestLore);
                chestplateItemMeta.spigot().setUnbreakable(true);
                chestplate.setItemMeta(chestplateItemMeta);
            }

            ItemStack leggings = null;
            if(armorConfig.isSet(section+".ItemSettings.Leggings")) {
                if (Utils.isInt(armorConfig.getString(section + ".ItemSettings.Leggings.ID"))) {
                    leggings = new ItemStack(armorConfig.getInt(section + ".ItemSettings.Leggings.ID"), 1, (short) armorConfig.getInt(section + ".ItemSettings.Leggings.Data"));
                } else {
                    leggings = new ItemStack(Material.getMaterial(armorConfig.getString(section + ".ItemSettings.Leggings.ID")), 1, (short) armorConfig.getInt(section + ".ItemSettings.Leggings.Data"));
                }
                ItemMeta leggingsItemMeta = leggings.getItemMeta();
                leggingsItemMeta.setDisplayName(GunsPlugin.Color(armorConfig.getString(section + ".ItemSettings.Leggings.Name")));
                List<String> newLeggingsLore = Lists.newArrayList();
                for (String lore : armorConfig.getStringList(section + ".ItemSettings.Leggings.Description")) {
                    newLeggingsLore.add(GunsPlugin.Color(lore));
                }
                leggingsItemMeta.setLore(newLeggingsLore);
                leggingsItemMeta.spigot().setUnbreakable(true);
                leggings.setItemMeta(leggingsItemMeta);
            }

            ItemStack boots = null;
            if(armorConfig.isSet(section+".ItemSettings.Boots")) {
                if (Utils.isInt(armorConfig.getString(section + ".ItemSettings.Boots.ID"))) {
                    boots = new ItemStack(armorConfig.getInt(section + ".ItemSettings.Boots.ID"), 1, (short) armorConfig.getInt(section + ".ItemSettings.Boots.Data"));
                } else {
                    boots = new ItemStack(Material.getMaterial(armorConfig.getString(section + ".ItemSettings.Boots.ID")), 1, (short) armorConfig.getInt(section + ".ItemSettings.Boots.Data"));
                }
                ItemMeta bootsItemMeta = boots.getItemMeta();
                bootsItemMeta.setDisplayName(GunsPlugin.Color(armorConfig.getString(section + ".ItemSettings.Boots.Name")));
                List<String> newBootsLore = Lists.newArrayList();
                for (String lore : armorConfig.getStringList(section + ".ItemSettings.Boots.Description")) {
                    newBootsLore.add(GunsPlugin.Color(lore));
                }
                bootsItemMeta.setLore(newBootsLore);
                bootsItemMeta.spigot().setUnbreakable(true);
                boots.setItemMeta(bootsItemMeta);
            }

            Armor armor = new Armor(section);
            armor.setDiscountType(DiscountType.valueOf(armorConfig.getString(section+".ArmorSettings.DiscountDamageType")));

            if(helmet != null) {
                armor.setHelmet(helmet);
            }
            if(chestplate != null) {
                armor.setChestplate(chestplate);
            }
            if(leggings != null) {
                armor.setLeggings(leggings);
            }
            if(boots != null) {
                armor.setBoots(boots);
            }

            this.armors.put(section.toLowerCase(), armor);
        }
    }

    public void loadGuns() {
        this.guns.clear();

        FileConfiguration gunsConfig = ConfigManager.getGuns();
        for(String section : gunsConfig.getKeys(false)) {
            ItemStack gunItem;
            if(Utils.isInt(gunsConfig.getString(section+".ItemSettings.Gun.ID"))) {
                gunItem = new ItemStack(gunsConfig.getInt(section+".ItemSettings.Gun.ID"), 1, (short) gunsConfig.getInt(section+".ItemSettings.Gun.Data"));
            } else {
                gunItem = new ItemStack(Material.getMaterial(gunsConfig.getString(section+".ItemSettings.Gun.ID")), 1, (short) gunsConfig.getInt(section+".ItemSettings.Gun.Data"));
            }
            ItemMeta gunMeta = gunItem.getItemMeta();
            gunMeta.setDisplayName(GunsPlugin.Color(gunsConfig.getString(section+".ItemSettings.Gun.Name")));
            List<String> newGunLore = Lists.newArrayList();
            for(String lore : gunsConfig.getStringList(section+".ItemSettings.Gun.Description")) {
                newGunLore.add(GunsPlugin.Color(lore));
            }
            gunMeta.setLore(newGunLore);
            gunMeta.spigot().setUnbreakable(true);
            gunItem.setItemMeta(gunMeta);

            Guns guns = new Guns(section);
            guns.setUsePermission(gunsConfig.getBoolean(section+".ItemSettings.RequiredPermission"));
            guns.setPermission(gunsConfig.getString(section+".ItemSettings.Permission"));
            guns.setFireType(FireType.valueOf(gunsConfig.getString(section+".GunSettings.FireMode")));
            guns.setGunType(GunType.valueOf(gunsConfig.getString(section+".GunSettings.GunType")));
            guns.setAutomaticReload(gunsConfig.getBoolean(section+".GunSettings.AutomaticReload"));
            guns.setBounce(gunsConfig.getBoolean(section+".GunSettings.AmmoProjectile.Bounce"));
            guns.setReloadDelay(gunsConfig.getInt(section+".GunSettings.ReloadDelay"));
            guns.setShootDelay(gunsConfig.getInt(section+".GunSettings.ShootDelay"));
            guns.setAmmoCapacity(gunsConfig.getInt(section+".GunSettings.AmmoCapacity"));
            guns.setZoomLevel(gunsConfig.getInt(section+".GunSettings.ZoomLevel"));
            guns.setVelocity(gunsConfig.getInt(section+".GunSettings.AmmoProjectile.Velocity"));
            guns.setProjectileAmount(gunsConfig.getInt(section+".GunSettings.AmmoProjectile.ProjectileAmount"));
            guns.setGunItem(gunItem);
            for(String s : gunsConfig.getConfigurationSection(section+".GunSettings.NormalDamage").getKeys(false)) {
                guns.addNormalDamage(Rarity.valueOf(s), gunsConfig.getDouble(section+".GunSettings.NormalDamage."+s));
            }
            for(String s : gunsConfig.getConfigurationSection(section+".GunSettings.HeadShotDamage").getKeys(false)) {
                guns.addHeadshotDamage(Rarity.valueOf(s), gunsConfig.getDouble(section+".GunSettings.HeadShotDamage."+s));
            }
            Ammo ammo = this.getAmmo(gunsConfig.getString(section+".GunSettings.Ammo"));
            if(ammo != null) {
                guns.setAmmo(ammo);
            }
            if(gunsConfig.isSet(section+".GunSettings.Sound")) {
                if (gunsConfig.isSet(section + ".GunSettings.Sound.Shooting")) {
                    Sounds soundShooting = new Sounds();
                    soundShooting.setSound(Sound.valueOf(gunsConfig.getString(section + ".GunSettings.Sound.Shooting.SoundType")));
                    soundShooting.setVolume((float) gunsConfig.getDouble(section + ".GunSettings.Sound.Shooting.Volume"));
                    guns.setShootingSound(soundShooting);
                }
                if (gunsConfig.isSet(section + ".GunSettings.Sound.Reloading")) {
                    Sounds reloadingSound = new Sounds();
                    reloadingSound.setSound(Sound.valueOf(gunsConfig.getString(section + ".GunSettings.Sound.Reloading.SoundType")));
                    reloadingSound.setVolume((float) gunsConfig.getDouble(section + ".GunSettings.Sound.Reloading.Volume"));
                    guns.setReloadingSound(reloadingSound);
                }
                if (gunsConfig.isSet(section + ".GunSettings.Sound.NoBullets")) {
                    Sounds noBulletsSound = new Sounds();
                    noBulletsSound.setSound(Sound.valueOf(gunsConfig.getString(section + ".GunSettings.Sound.NoBullets.SoundType")));
                    noBulletsSound.setVolume((float) gunsConfig.getDouble(section + ".GunSettings.Sound.NoBullets.Volume"));
                    guns.setNoBulletsSound(noBulletsSound);
                }
            }

            this.guns.put(section.toLowerCase(), guns);
        }
    }

    public void loadAmmos() {
        this.ammo.clear();

        FileConfiguration ammoConfig = ConfigManager.getAmmo();
        for(String section : ammoConfig.getKeys(false)) {
            ItemStack ammoItem;
            if(Utils.isInt(ammoConfig.getString(section+".ItemSettings.Ammo.ID"))) {
                ammoItem = new ItemStack(ammoConfig.getInt(section+".ItemSettings.Ammo.ID"), 1, (short) ammoConfig.getInt(section+".ItemSettings.Ammo.Data"));
            } else {
                ammoItem = new ItemStack(Material.getMaterial(ammoConfig.getString(section+".ItemSettings.Ammo.ID")), 1, (short) ammoConfig.getInt(section+".ItemSettings.Ammo.Data"));
            }
            ItemMeta ammoMeta = ammoItem.getItemMeta();
            ammoMeta.setDisplayName(GunsPlugin.Color(ammoConfig.getString(section+".ItemSettings.Ammo.Name")));
            List<String> newAmmoLore = Lists.newArrayList();
            for(String lore : ammoConfig.getStringList(section+".ItemSettings.Ammo.Description")) {
                newAmmoLore.add(GunsPlugin.Color(lore));
            }
            ammoMeta.setLore(newAmmoLore);
            ammoMeta.spigot().setUnbreakable(true);
            ammoItem.setItemMeta(ammoMeta);

            Ammo ammo = new Ammo(section);
            ammo.setAmmoItem(ammoItem);

            this.ammo.put(section.toLowerCase(), ammo);
        }
    }
}
