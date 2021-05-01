package com.gabrielhd.guns.Manager;

import com.gabrielhd.guns.GunsPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class ConfigManager {

    private static FileConfiguration Settings;
    private static FileConfiguration Explosive;
    private static FileConfiguration Medicines;
    private static FileConfiguration Armor;
    private static FileConfiguration Ammo;
    private static FileConfiguration Guns;
    private static File SettingsFile;
    private static File ExplosiveFile;
    private static File MedicinesFile;
    private static File ArmorFile;
    private static File AmmoFile;
    private static File GunsFile;

    public ConfigManager(GunsPlugin plugin) {
        SettingsFile = new File(plugin.getDataFolder(), "Settings.yml");
        if(!SettingsFile.exists()) {
            plugin.saveResource("Settings.yml", false);
        }
        Settings = YamlConfiguration.loadConfiguration(SettingsFile);

        ExplosiveFile = new File(plugin.getDataFolder(), "Explosives.yml");
        if(!ExplosiveFile.exists()) {
            plugin.saveResource("Explosives.yml", false);
        }
        Explosive = YamlConfiguration.loadConfiguration(ExplosiveFile);

        MedicinesFile = new File(plugin.getDataFolder(), "Medicines.yml");
        if(!MedicinesFile.exists()) {
            plugin.saveResource("Medicines.yml", false);
        }
        Medicines = YamlConfiguration.loadConfiguration(MedicinesFile);

        ArmorFile = new File(plugin.getDataFolder(), "Armor.yml");
        if(!ArmorFile.exists()) {
            plugin.saveResource("Armor.yml", false);
        }
        Armor = YamlConfiguration.loadConfiguration(ArmorFile);

        AmmoFile = new File(plugin.getDataFolder(), "Ammo.yml");
        if(!AmmoFile.exists()) {
            plugin.saveResource("Ammo.yml", false);
        }
        Ammo = YamlConfiguration.loadConfiguration(AmmoFile);

        GunsFile = new File(plugin.getDataFolder(), "Guns.yml");
        if(!GunsFile.exists()) {
            plugin.saveResource("Guns.yml", false);
        }
        Guns = YamlConfiguration.loadConfiguration(GunsFile);
    }

    public static File getSettingsFile() {
        return SettingsFile;
    }

    public static FileConfiguration getSettings() {
        return Settings;
    }

    public static FileConfiguration getGuns() {
        return Guns;
    }

    public static File getGunsFile() {
        return GunsFile;
    }

    public static FileConfiguration getMedicines() {
        return Medicines;
    }

    public static File getMedicinesFile() {
        return MedicinesFile;
    }

    public static File getExplosiveFile() {
        return ExplosiveFile;
    }

    public static FileConfiguration getExplosive() {
        return Explosive;
    }

    public static File getArmorFile() {
        return ArmorFile;
    }

    public static FileConfiguration getArmor() {
        return Armor;
    }

    public static File getAmmoFile() {
        return AmmoFile;
    }

    public static FileConfiguration getAmmo() {
        return Ammo;
    }
}
