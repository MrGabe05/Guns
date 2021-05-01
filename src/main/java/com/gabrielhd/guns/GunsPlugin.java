package com.gabrielhd.guns;

import com.gabrielhd.guns.Commands.Commands;
import com.gabrielhd.guns.Listeners.Listeners;
import com.gabrielhd.guns.Manager.ConfigManager;
import com.gabrielhd.guns.Manager.WeaponManager;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GunsPlugin extends JavaPlugin implements Listener
{
    private static GunsPlugin instance;
    private static WeaponManager weaponManager;
    private static final Map<UUID, Long> cooldownPlayers = new HashMap<>();

    @Override
    public void onEnable() {
        instance = this;

        new ConfigManager(this);
        weaponManager = new WeaponManager();

        this.getServer().getPluginManager().registerEvents(new Listeners(), this);
        this.getCommand("guns").setExecutor(new Commands());

        this.getLogger().info("Has been sucessfully enabled!");
    }

    public static GunsPlugin getInstance() {
        return instance;
    }

    public static WeaponManager getWeaponManager() {
        return weaponManager;
    }

    public static Map<UUID, Long> getCooldownPlayers() {
        return cooldownPlayers;
    }

    public static String Color(String a) {
        return a.replaceAll("&", "§");
    }
}
