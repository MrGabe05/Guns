package com.gabrielhd.guns.Utils;

import lombok.Getter;
import org.bukkit.inventory.ItemStack;

public class CustomStack {

    @Getter private final ItemStack item;
    @Getter private final int slot;

    public CustomStack(ItemStack item, int slot) {
        this.item = item;
        this.slot = slot;
    }
}
