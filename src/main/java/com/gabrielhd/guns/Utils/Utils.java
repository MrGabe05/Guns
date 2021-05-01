package com.gabrielhd.guns.Utils;

import com.gabrielhd.guns.GunsPlugin;
import com.gabrielhd.guns.Utils.NBT.VersionMatcher;
import com.gabrielhd.guns.Utils.NBT.VersionWrapper;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Utils {

    private static final VersionWrapper WRAPPER = new VersionMatcher().match();

    public static boolean is1_8() {
        String serverVersion = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3].substring(1);
        if(serverVersion.toLowerCase().startsWith("1_8".toLowerCase())) {
            return true;
        }
        return false;
    }

    public static void sendActionbar(Player p, String message) {
        WRAPPER.sendActionText(p, GunsPlugin.Color(message));
    }

    public static boolean isInt(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}
