package com.gabrielhd.guns.Guns;

import com.gabrielhd.guns.Enums.FireType;
import com.gabrielhd.guns.Enums.GunType;
import com.gabrielhd.guns.Enums.Rarity;
import com.gabrielhd.guns.GunsPlugin;
import com.gabrielhd.guns.Manager.ConfigManager;
import com.gabrielhd.guns.Utils.NBTItem;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Guns {

    @Getter private final String name;
    @Getter @Setter private String permission;
    @Getter @Setter private ItemStack gunItem;
    @Getter @Setter private Ammo ammo;
    @Getter @Setter private GunType gunType;
    @Getter @Setter private FireType fireType;
    @Getter @Setter private Sounds shootingSound;
    @Getter @Setter private Sounds reloadingSound;
    @Getter @Setter private Sounds noBulletsSound;
    @Getter @Setter private boolean automaticReload;
    @Getter @Setter private boolean usePermission;
    @Getter @Setter private boolean bounce;
    @Getter @Setter private double recoil;
    @Getter @Setter private int projectileAmount;
    @Getter @Setter private int reloadDelay;
    @Getter @Setter private int shootDelay;
    @Getter @Setter private int ammoCapacity;
    @Getter @Setter private int zoomLevel;
    @Getter @Setter private int velocity;

    @Getter private final Map<UUID, Long> shootDelayList = Maps.newHashMap();
    private final Map<Rarity, Double> normalDamages = Maps.newHashMap();
    private final Map<Rarity, Double> headshotDamages = Maps.newHashMap();
    
    public Guns(String name) {
        this.name = name;
    }

    public double getNormalDamage(Rarity rarity) {
        return this.normalDamages.get(rarity);
    }

    public void addNormalDamage(Rarity rarity, double damage) {
        this.normalDamages.put(rarity, damage);
    }

    public double getHeadshotDamage(Rarity rarity) {
        return this.headshotDamages.get(rarity);
    }

    public void addHeadshotDamage(Rarity rarity, double damage) {
        this.headshotDamages.put(rarity, damage);
    }

    public void giveGunItem(Player player, Rarity rarity) {
        NBTItem nbtItem = new NBTItem(this.gunItem);
        nbtItem.setBoolean("Weapon", true);
        nbtItem.setBoolean("Armed", true);
        nbtItem.setBoolean("Gun", true);
        nbtItem.setBoolean("Scope", false);
        nbtItem.setBoolean("Reloading", false);
        nbtItem.setString("WeaponType", this.name);
        nbtItem.setString("UUID", UUID.randomUUID().toString());
        nbtItem.setString("Rarity", rarity.name());
        nbtItem.setInteger("CurrentAmount", this.ammoCapacity);

        ItemStack gun = nbtItem.getItem();
        ItemMeta gunMeta = gun.getItemMeta();
        List<String> newGunLore = Lists.newArrayList();
        for(String lore : ConfigManager.getGuns().getStringList(this.name+".ItemSettings.Gun.Description")) {
            newGunLore.add(GunsPlugin.Color(lore.replace("%rarity%", ConfigManager.getSettings().getString("Messages.Rarities."+rarity.name()))));
        }
        gunMeta.setLore(newGunLore);
        gun.setItemMeta(gunMeta);

        player.getInventory().addItem(gun);
    }

    public void giveAmmoItem(Player player) {
        NBTItem nbtItem2 = new NBTItem(this.ammo.getAmmoItem());
        nbtItem2.setBoolean("Ammo", true);
        nbtItem2.setString("AmmoType", this.ammo.getName());

        player.getInventory().addItem(nbtItem2.getItem());
    }
}
