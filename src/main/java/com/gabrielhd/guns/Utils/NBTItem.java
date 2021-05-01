package com.gabrielhd.guns.Utils;

import com.gabrielhd.guns.Utils.NBT.VersionMatcher;
import com.gabrielhd.guns.Utils.NBT.VersionWrapper;
import org.bukkit.inventory.ItemStack;

public class NBTItem {

    private ItemStack item;
    private final VersionWrapper WRAPPER = new VersionMatcher().match();

    public NBTItem(ItemStack item) {
        this.item = item;
    }

    public boolean hasKey(String path) {
        return this.WRAPPER.hasKey(this.item, path);
    }

    public void setString(String path, String value) {
        this.item = this.WRAPPER.setString(this.item, path, value);
    }

    public String getString(String path) {
        return this.WRAPPER.getString(this.item, path);
    }

    public void setDouble(String path, double value) {
        this.item = this.WRAPPER.setDouble(this.item, path, value);
    }

    public Double getDouble(String path) {
        return this.WRAPPER.getDouble(this.item, path);
    }

    public void setInteger(String path, int value) {
        this.item = this.WRAPPER.setInteger(this.item, path, value);
    }

    public Integer getInteger(String path) {
        return this.WRAPPER.getInteger(this.item, path);
    }

    public void setBoolean(String path, boolean value) {
        this.item = this.WRAPPER.setBoolean(this.item, path, value);
    }

    public Boolean getBoolean(String path) {
        return this.WRAPPER.getBoolean(this.item, path);
    }

    public void setLong(String path, long value) {
        this.item = this.WRAPPER.setLong(this.item, path, value);
    }

    public long getLong(String path) {
        return this.WRAPPER.getLong(this.item, path);
    }

    public void remove(String path) {
        this.item = this.WRAPPER.remove(this.item, path);
    }

    public ItemStack getItem() {
        return this.item;
    }
}
