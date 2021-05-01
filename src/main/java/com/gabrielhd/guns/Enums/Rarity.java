package com.gabrielhd.guns.Enums;

import lombok.Getter;

public enum Rarity {

    Common(0),
    Uncommon(1),
    Rare(2),
    Legendary(3),
    Exotic(4);

    @Getter private final int id;

    Rarity(int id) {
        this.id = id;
    }

    public static Rarity getRarity(String name) {
        for(Rarity rarity : Rarity.values()) {
            if(rarity.name().equalsIgnoreCase(name)) {
                return rarity;
            }
        }
        return null;
    }
}
