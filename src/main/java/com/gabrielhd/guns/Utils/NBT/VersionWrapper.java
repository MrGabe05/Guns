package com.gabrielhd.guns.Utils.NBT;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface VersionWrapper {

    boolean hasKey(ItemStack item, String s);

    String getString(ItemStack item, String s);

    ItemStack setString(ItemStack item, String s1, String s2);

    double getDouble(ItemStack item, String s);

    ItemStack setDouble(ItemStack itemStack, String s1, double d1);

    int getInteger(ItemStack item, String s);

    ItemStack setInteger(ItemStack item, String s1, int i1);

    boolean getBoolean(ItemStack item, String s);

    ItemStack setBoolean(ItemStack item, String s1, boolean b1);

    long getLong(ItemStack item, String s);

    ItemStack setLong(ItemStack item, String s1, long l1);

    ItemStack remove(ItemStack item, String s);

    void sendActionText(Player player, String message);
}
