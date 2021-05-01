package com.gabrielhd.guns.Guns;

import com.gabrielhd.guns.Utils.NBTItem;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Medicine {

    @Getter private final String name;
    @Getter @Setter private ItemStack itemMed;
    @Getter @Setter private long cooldown;
    @Getter @Setter private double health;

    public Medicine(String name) {
        this.name = name;
    }

    public void giveMedicine(Player player) {
        NBTItem nbtItem = new NBTItem(this.itemMed);
        nbtItem.setBoolean("Medicine", true);
        nbtItem.setString("MedicineType", this.name);

        player.getInventory().addItem(nbtItem.getItem());
    }
}
