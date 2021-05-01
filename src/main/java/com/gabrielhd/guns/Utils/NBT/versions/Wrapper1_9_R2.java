package com.gabrielhd.guns.Utils.NBT.versions;

import com.gabrielhd.guns.Utils.NBT.VersionWrapper;
import net.minecraft.server.v1_9_R2.*;
import org.bukkit.craftbukkit.v1_9_R2.enchantments.CraftEnchantment;
import org.bukkit.craftbukkit.v1_9_R2.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_9_R2.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;

public class Wrapper1_9_R2 implements VersionWrapper {

    private org.bukkit.enchantments.Enchantment glow;

    @Override
    public long getLong(ItemStack item, String s) {
        net.minecraft.server.v1_9_R2.ItemStack stack = CraftItemStack.asNMSCopy(item);
        if (stack.getTag() == null) stack.setTag(new NBTTagCompound());
        return stack.getTag().getLong(s);
    }

    @Override
    public ItemStack setLong(ItemStack item, String s1, long l1) {
        net.minecraft.server.v1_9_R2.ItemStack stack = CraftItemStack.asNMSCopy(item);
        if (stack.getTag() == null) stack.setTag(new NBTTagCompound());
        stack.getTag().setLong(s1, l1);
        return CraftItemStack.asCraftMirror(stack);
    }

    @Override
    public ItemStack remove(ItemStack item, String s) {
        net.minecraft.server.v1_9_R2.ItemStack stack = CraftItemStack.asNMSCopy(item);
        if (stack.getTag() == null) stack.setTag(new NBTTagCompound());
        stack.getTag().remove(s);
        return CraftItemStack.asCraftMirror(stack);
    }

    @Override
    public void sendActionText(Player player, String message) {
        PacketPlayOutChat packet = new PacketPlayOutChat(new ChatComponentText(message), (byte) 2);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }

    @Override
    public boolean hasKey(ItemStack item, String s) {
        net.minecraft.server.v1_9_R2.ItemStack stack = CraftItemStack.asNMSCopy(item);
        if (stack.getTag() == null) stack.setTag(new NBTTagCompound());
        return stack.getTag().hasKey(s);
    }

    @Override
    public String getString(ItemStack item, String s) {
        net.minecraft.server.v1_9_R2.ItemStack stack = CraftItemStack.asNMSCopy(item);
        if (stack.getTag() == null) stack.setTag(new NBTTagCompound());
        return stack.getTag().getString(s);
    }

    @Override
    public ItemStack setString(ItemStack item, String s1, String s2) {
        net.minecraft.server.v1_9_R2.ItemStack stack = CraftItemStack.asNMSCopy(item);
        if (stack.getTag() == null) stack.setTag(new NBTTagCompound());
        stack.getTag().setString(s1, s2);
        return CraftItemStack.asCraftMirror(stack);
    }

    @Override
    public double getDouble(ItemStack item, String s) {
        net.minecraft.server.v1_9_R2.ItemStack stack = CraftItemStack.asNMSCopy(item);
        if (stack.getTag() == null) stack.setTag(new NBTTagCompound());
        return stack.getTag().getDouble(s);
    }

    @Override
    public ItemStack setDouble(ItemStack item, String s1, double d1) {
        net.minecraft.server.v1_9_R2.ItemStack stack = CraftItemStack.asNMSCopy(item);
        if (stack.getTag() == null) stack.setTag(new NBTTagCompound());
        stack.getTag().setDouble(s1, d1);
        return CraftItemStack.asCraftMirror(stack);
    }

    @Override
    public int getInteger(ItemStack item, String s) {
        net.minecraft.server.v1_9_R2.ItemStack stack = CraftItemStack.asNMSCopy(item);
        if (stack.getTag() == null) stack.setTag(new NBTTagCompound());
        return stack.getTag().getInt(s);
    }

    @Override
    public ItemStack setInteger(ItemStack item, String s1, int i1) {
        net.minecraft.server.v1_9_R2.ItemStack stack = CraftItemStack.asNMSCopy(item);
        if (stack.getTag() == null) stack.setTag(new NBTTagCompound());
        stack.getTag().setInt(s1, i1);
        return CraftItemStack.asCraftMirror(stack);
    }

    @Override
    public boolean getBoolean(ItemStack item, String s) {
        net.minecraft.server.v1_9_R2.ItemStack stack = CraftItemStack.asNMSCopy(item);
        if (stack.getTag() == null) stack.setTag(new NBTTagCompound());
        return stack.getTag().getBoolean(s);
    }

    @Override
    public ItemStack setBoolean(ItemStack item, String s1, boolean b1) {
        net.minecraft.server.v1_9_R2.ItemStack stack = CraftItemStack.asNMSCopy(item);
        if (stack.getTag() == null) stack.setTag(new NBTTagCompound());
        stack.getTag().setBoolean(s1, b1);
        return CraftItemStack.asCraftMirror(stack);
    }
}
