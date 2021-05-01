package com.gabrielhd.guns.Guns;

import com.gabrielhd.guns.Utils.NBTItem;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Ammo {

    @Getter private final String name;
    @Getter @Setter private ItemStack ammoItem;

    public Ammo(String name) {
        this.name = name;
    }

    public void giveAmmoItem(Player player) {
        NBTItem nbtItem2 = new NBTItem(this.ammoItem);
        nbtItem2.setBoolean("Ammo", true);
        nbtItem2.setString("AmmoType", this.name);

        player.getInventory().addItem(nbtItem2.getItem());
    }
}
