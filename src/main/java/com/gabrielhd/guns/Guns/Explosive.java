package com.gabrielhd.guns.Guns;

import com.gabrielhd.guns.Enums.ExplosiveType;
import com.gabrielhd.guns.Utils.NBTItem;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Explosive {

    @Getter private final String name;
    @Getter @Setter private String permission;
    @Getter @Setter private ItemStack explosiveItem;
    @Getter @Setter private ExplosiveType explosiveType;
    @Getter @Setter private boolean usePermission;
    @Getter @Setter private long explosionDelay;
    @Getter @Setter private int shootDelay;
    @Getter @Setter private int velocity;
    @Getter @Setter private double radius;

    @Getter private final Map<UUID, Long> shootDelayList = new HashMap<>();
    @Getter private final List<Material> blockToBreak = Lists.newArrayList();

    public Explosive(String name) {
        this.name = name;
    }

    public void giveExplosive(Player player) {
        NBTItem nbtItem = new NBTItem(this.explosiveItem);
        nbtItem.setBoolean("Explosive", true);
        nbtItem.setString("ExplosiveType", this.name);

        player.getInventory().addItem(nbtItem.getItem());
    }
}
