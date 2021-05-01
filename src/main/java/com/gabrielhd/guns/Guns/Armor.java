package com.gabrielhd.guns.Guns;

import com.gabrielhd.guns.Enums.DiscountType;
import com.gabrielhd.guns.Enums.Rarity;
import com.gabrielhd.guns.GunsPlugin;
import com.gabrielhd.guns.Manager.ConfigManager;
import com.gabrielhd.guns.Utils.NBTItem;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class Armor {

    @Getter private final String name;
    @Getter @Setter private ItemStack helmet;
    @Getter @Setter private ItemStack chestplate;
    @Getter @Setter private ItemStack leggings;
    @Getter @Setter private ItemStack boots;
    @Getter @Setter private DiscountType discountType;

    public Armor(String name) {
        this.name = name;
    }

    public ItemStack setNbtData(ItemStack item, String rarity, String section) {
        FileConfiguration armorConfig = ConfigManager.getArmor();

        NBTItem nbtItem = new NBTItem(item);

        return nbtItem.getItem();
    }

    public void giveHelmet(Player player, Rarity rarity, boolean set) {
        FileConfiguration armorConfig = ConfigManager.getArmor();

        ItemStack helmet = this.helmet;

        NBTItem nbtItem = new NBTItem(helmet);
        nbtItem.setBoolean("CustomArmor", true);
        nbtItem.setString("Rarity", rarity.name());
        nbtItem.setInteger("Durability", armorConfig.getInt(this.name+".ArmorSettings."+rarity+".Helmet.MaxDurability"));
        nbtItem.setDouble("Protection", armorConfig.getDouble(this.name+".ArmorSettings."+rarity+".Helmet.ShootProtection"));

        ItemMeta helmetMeta = helmet.getItemMeta();

        List<String> newHelmetLore = Lists.newArrayList();
        for (String lore : armorConfig.getStringList(this.name + ".ItemSettings.Helmet.Description")) {
            newHelmetLore.add(GunsPlugin.Color(lore.replace("%rarity%", ConfigManager.getSettings().getString("Messages.Rarities."+rarity))));
        }
        helmetMeta.setLore(newHelmetLore);
        helmet.setItemMeta(helmetMeta);

        if(set) {
            player.getInventory().setHelmet(helmet);
        } else {
            player.getInventory().addItem(helmet);
        }
    }

    public void giveChestplate(Player player, Rarity rarity, boolean set) {
        FileConfiguration armorConfig = ConfigManager.getArmor();

        ItemStack chestplate = this.chestplate;

        NBTItem nbtItem = new NBTItem(chestplate);
        nbtItem.setBoolean("CustomArmor", true);
        nbtItem.setString("Rarity", rarity.name());
        nbtItem.setInteger("Durability", armorConfig.getInt(this.name+".ArmorSettings."+rarity+".Chestplate.MaxDurability"));
        nbtItem.setDouble("Protection", armorConfig.getDouble(this.name+".ArmorSettings."+rarity+".Chestplate.ShootProtection"));

        ItemMeta chestplateMeta = chestplate.getItemMeta();

        List<String> newChestLore = Lists.newArrayList();
        for (String lore : armorConfig.getStringList(this.name + ".ItemSettings.Chestplate.Description")) {
            newChestLore.add(GunsPlugin.Color(lore.replace("%rarity%", ConfigManager.getSettings().getString("Messages.Rarities."+rarity))));
        }
        chestplateMeta.setLore(newChestLore);
        chestplate.setItemMeta(chestplateMeta);

        if(set) {
            player.getInventory().setHelmet(chestplate);
        } else {
            player.getInventory().addItem(chestplate);
        }
    }

    public void giveLeggings(Player player, Rarity rarity, boolean set) {
        FileConfiguration armorConfig = ConfigManager.getArmor();

        ItemStack leggings = this.leggings;

        NBTItem nbtItem = new NBTItem(leggings);
        nbtItem.setBoolean("CustomArmor", true);
        nbtItem.setString("Rarity", rarity.name());
        nbtItem.setInteger("Durability", armorConfig.getInt(this.name+".ArmorSettings."+rarity+".Leggings.MaxDurability"));
        nbtItem.setDouble("Protection", armorConfig.getDouble(this.name+".ArmorSettings."+rarity+".Leggings.ShootProtection"));
        ItemMeta leggingsMeta = leggings.getItemMeta();

        List<String> newLeggingsLore = Lists.newArrayList();
        for (String lore : armorConfig.getStringList(this.name + ".ItemSettings.Leggings.Description")) {
            newLeggingsLore.add(GunsPlugin.Color(lore.replace("%rarity%", ConfigManager.getSettings().getString("Messages.Rarities."+rarity))));
        }
        leggingsMeta.setLore(newLeggingsLore);
        leggings.setItemMeta(leggingsMeta);

        if(set) {
            player.getInventory().setHelmet(leggings);
        } else {
            player.getInventory().addItem(leggings);
        }
    }

    public void giveBoots(Player player, Rarity rarity, boolean set) {
        FileConfiguration armorConfig = ConfigManager.getArmor();

        ItemStack boots = this.boots;

        NBTItem nbtItem = new NBTItem(boots);
        nbtItem.setBoolean("CustomArmor", true);
        nbtItem.setString("Rarity", rarity.name());
        nbtItem.setInteger("Durability", armorConfig.getInt(this.name+".ArmorSettings."+rarity+".Boots.MaxDurability"));
        nbtItem.setDouble("Protection", armorConfig.getDouble(this.name+".ArmorSettings."+rarity+".Boots.ShootProtection"));
        ItemMeta bootsMeta = boots.getItemMeta();

        List<String> newBootsLore = Lists.newArrayList();
        for (String lore : armorConfig.getStringList(this.name + ".ItemSettings.Boots.Description")) {
            newBootsLore.add(GunsPlugin.Color(lore.replace("%rarity%", ConfigManager.getSettings().getString("Messages.Rarities."+rarity))));
        }
        bootsMeta.setLore(newBootsLore);
        boots.setItemMeta(bootsMeta);

        if(set) {
            player.getInventory().setHelmet(boots);
        } else {
            player.getInventory().addItem(boots);
        }
    }
}
