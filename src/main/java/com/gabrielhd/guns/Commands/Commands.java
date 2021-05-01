package com.gabrielhd.guns.Commands;

import com.gabrielhd.guns.Enums.Rarity;
import com.gabrielhd.guns.Guns.*;
import com.gabrielhd.guns.GunsPlugin;
import com.gabrielhd.guns.Manager.ConfigManager;
import com.gabrielhd.guns.Utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if((sender instanceof Player)) {
            Player player = (Player) sender;
            if(!player.hasPermission("guns.command.admin")) {
                player.sendMessage(GunsPlugin.Color(ConfigManager.getSettings().getString("Messages.NoPermissions")));
                return true;
            }
            if(args.length == 0 || args.length > 5) {
                player.sendMessage(GunsPlugin.Color("&e/guns list"));
                player.sendMessage(GunsPlugin.Color("&e/guns give [Weapon Name] <Rarity> <Player>"));
                player.sendMessage(GunsPlugin.Color("&e/guns medicine [Medicine Name] <Player>"));
                player.sendMessage(GunsPlugin.Color("&e/guns ammo [Weapon Name] [Amount] <Player>"));
                player.sendMessage(GunsPlugin.Color("&e/guns armor [Armor Name] [Armor piece] <Rarity> <Player>"));
                player.sendMessage(GunsPlugin.Color("&e/guns list [All/Weapons/Explosives/Armors/Medicines]"));
                return true;
            }
            if(args.length == 5) {
                if(args[0].equalsIgnoreCase("armor")) {
                    String name = args[1];
                    Player target = Bukkit.getPlayer(args[4]);
                    Armor armor = GunsPlugin.getWeaponManager().getArmor(name);
                    if(target != null) {
                        if (armor != null) {
                            Rarity rarity = Rarity.getRarity(args[3]);
                            if (args[2].equalsIgnoreCase("helmet")) {
                                if (armor.getHelmet() != null) {
                                    armor.giveHelmet(target, rarity, false);
                                }
                                player.sendMessage(GunsPlugin.Color("&aYou were given the " + armor.getName() + " Helmet to "+target.getName()));
                                return true;
                            }
                            if (args[2].equalsIgnoreCase("chestplate")) {
                                if (armor.getChestplate() != null) {
                                    armor.giveChestplate(target, rarity, false);
                                }
                                player.sendMessage(GunsPlugin.Color("&aYou were given the " + armor.getName() + " Chestplate to "+target.getName()));
                                return true;
                            }
                            if (args[2].equalsIgnoreCase("leggings")) {
                                if (armor.getLeggings() != null) {
                                    armor.giveLeggings(target, rarity, false);
                                }
                                player.sendMessage(GunsPlugin.Color("&aYou were given the " + armor.getName() + " Leggings to "+target.getName()));
                                return true;
                            }
                            if (args[2].equalsIgnoreCase("boots")) {
                                if (armor.getBoots() != null) {
                                    armor.giveBoots(target, rarity, false);
                                }
                                player.sendMessage(GunsPlugin.Color("&aYou were given the " + armor.getName() + " Boots to "+target.getName()));
                                return true;
                            }
                            player.sendMessage(GunsPlugin.Color("&eArmor pieces: Helmet, Chestplate, Leggings, Boots."));
                            return true;
                        }
                        return true;
                    }
                    return true;
                }
            }
            if(args.length == 4) {
                if(args[0].equalsIgnoreCase("armor")) {
                    String name = args[1];
                    Armor armor = GunsPlugin.getWeaponManager().getArmor(name);
                    if(armor != null) {
                        Rarity rarity = Rarity.getRarity(args[3]);
                        if (args[2].equalsIgnoreCase("helmet")) {
                            if (armor.getHelmet() != null) {
                                armor.giveHelmet(player, rarity, false);
                            }
                            player.sendMessage(GunsPlugin.Color("&aYou were given the "+armor.getName()+" Helmet"));
                            return true;
                        }
                        if (args[2].equalsIgnoreCase("chestplate")) {
                            if (armor.getChestplate() != null) {
                                armor.giveChestplate(player, rarity, false);
                            }
                            player.sendMessage(GunsPlugin.Color("&aYou were given the "+armor.getName()+" Chestplate"));
                            return true;
                        }
                        if (args[2].equalsIgnoreCase("leggings")) {
                            if (armor.getLeggings() != null) {
                                armor.giveLeggings(player, rarity, false);
                            }
                            player.sendMessage(GunsPlugin.Color("&aYou were given the "+armor.getName()+" Leggings"));
                            return true;
                        }
                        if (args[2].equalsIgnoreCase("boots")) {
                            if (armor.getBoots() != null) {
                                armor.giveBoots(player, rarity, false);
                            }
                            player.sendMessage(GunsPlugin.Color("&aYou were given the "+armor.getName()+" Boots"));
                            return true;
                        }
                        player.sendMessage(GunsPlugin.Color("&eArmor pieces: Helmet, Chestplate, Leggings, Boots."));
                        return true;
                    }
                    return true;
                }
                if(args[0].equalsIgnoreCase("give")) {
                    String name = args[1];
                    Rarity rarity = Rarity.getRarity(args[2]);
                    Player target = Bukkit.getPlayer(args[3]);
                    if(target != null) {
                        Guns guns = GunsPlugin.getWeaponManager().getGun(name);
                        if (guns != null && guns.getGunItem() != null) {
                            guns.giveGunItem(target, rarity);
                            player.sendMessage(GunsPlugin.Color("&aYou were given the " + guns.getName() + " weapon to "+target.getName()));
                            return true;
                        }
                        Explosive explosive = GunsPlugin.getWeaponManager().getExplosive(name);
                        if (explosive != null && explosive.getExplosiveItem() != null) {
                            explosive.giveExplosive(target);
                            player.sendMessage(GunsPlugin.Color("&aYou were given the " + explosive.getName() + " explosive to "+target.getName()));
                            return true;
                        }
                        player.sendMessage(GunsPlugin.Color("&6Weapons: "));
                        for (Guns gunsList : GunsPlugin.getWeaponManager().getGuns()) {
                            player.sendMessage(GunsPlugin.Color("  &7- &e" + gunsList.getName() + " &7Ammo: &e" + (gunsList.getAmmo() == null ? "Unknown" : gunsList.getAmmo().getName())));
                        }
                        player.sendMessage(GunsPlugin.Color("&6Explosives: "));
                        for (Explosive explosives : GunsPlugin.getWeaponManager().getExplosives()) {
                            player.sendMessage(GunsPlugin.Color("  &7- &e" + explosives.getName()));
                        }
                        return true;
                    }
                    return true;
                }
                if(args[0].equalsIgnoreCase("ammo")) {
                    String name = args[1];
                    Player target = Bukkit.getPlayer(args[3]);
                    if(target != null) {
                        Ammo ammo = GunsPlugin.getWeaponManager().getAmmo(name);
                        if(ammo != null && ammo.getAmmoItem() != null) {
                            if (Utils.isInt(args[2])) {
                                int amount = Integer.parseInt(args[2]);
                                for (int i = 0; i < amount; i++) {
                                    ammo.giveAmmoItem(target);
                                }
                                sender.sendMessage(GunsPlugin.Color("&aYou were given " + amount + " ammo " + ammo.getName()+ " to " + target.getName()));
                                return true;
                            }
                            return true;
                        }

                        Guns guns = GunsPlugin.getWeaponManager().getGun(name);
                        if (guns != null && guns.getAmmo().getAmmoItem() != null) {
                            if (Utils.isInt(args[2])) {
                                int amount = Integer.parseInt(args[2]);
                                for (int i = 0; i < amount; i++) {
                                    guns.giveAmmoItem(target);
                                }
                                player.sendMessage(GunsPlugin.Color("&aYou were given " + amount + " ammo for the " + guns.getName() + " weapon to " + target.getName()));
                                return true;
                            }
                            return true;
                        }

                        Explosive explosive = GunsPlugin.getWeaponManager().getExplosive(name);
                        if (explosive != null && explosive.getExplosiveItem() != null) {
                            if (Utils.isInt(args[2])) {
                                int amount = Integer.parseInt(args[2]);
                                for (int i = 0; i < amount; i++) {
                                    explosive.giveExplosive(target);
                                }
                                player.sendMessage(GunsPlugin.Color("&aYou were given " + amount + " " + explosive.getName() + " explosive to " + target.getName()));
                                return true;
                            }
                            return true;
                        }

                        player.sendMessage(GunsPlugin.Color("&6Weapons: "));
                        for (Guns gunsList : GunsPlugin.getWeaponManager().getGuns()) {
                            player.sendMessage(GunsPlugin.Color("  &7- &e" + gunsList.getName() + " &7Ammo: &e" + (gunsList.getAmmo() == null ? "Unknown" : gunsList.getAmmo().getName())));
                        }
                        player.sendMessage(GunsPlugin.Color("&6Explosives: "));
                        for (Explosive explosives : GunsPlugin.getWeaponManager().getExplosives()) {
                            player.sendMessage(GunsPlugin.Color("  &7- &e" + explosives.getName()));
                        }
                        return true;
                    }
                    return true;
                }
                return true;
            }
            if(args.length == 3) {
                if(args[0].equalsIgnoreCase("ammo")) {
                    String name = args[1];
                    Ammo ammo = GunsPlugin.getWeaponManager().getAmmo(name);
                    if(ammo != null && ammo.getAmmoItem() != null) {
                        if (Utils.isInt(args[2])) {
                            int amount = Integer.parseInt(args[2]);
                            for (int i = 0; i < amount; i++) {
                                ammo.giveAmmoItem(player);
                            }
                            sender.sendMessage(GunsPlugin.Color("&aYou were given " + amount + " ammo " + ammo.getName()+ " to " + player.getName()));
                            return true;
                        }
                        return true;
                    }

                    Guns guns = GunsPlugin.getWeaponManager().getGun(name);
                    if(guns != null && guns.getAmmo().getAmmoItem() != null) {
                        if(Utils.isInt(args[2])) {
                            int amount = Integer.parseInt(args[2]);
                            for (int i = 0; i < amount; i++) {
                                guns.giveAmmoItem(player);
                            }
                            player.sendMessage(GunsPlugin.Color("&aYou were given "+amount+" ammo for the "+guns.getName()+" weapon"));
                            return true;
                        }
                        return true;
                    }

                    Explosive explosive = GunsPlugin.getWeaponManager().getExplosive(name);
                    if(explosive != null && explosive.getExplosiveItem() != null) {
                        if(Utils.isInt(args[2])) {
                            int amount = Integer.parseInt(args[2]);
                            for (int i = 0; i < amount; i++) {
                                explosive.giveExplosive(player);
                            }
                            player.sendMessage(GunsPlugin.Color("&aYou were given "+amount+" "+explosive.getName()+" explosive"));
                            return true;
                        }
                        return true;
                    }

                    player.sendMessage(GunsPlugin.Color("&6Weapons: "));
                    for(Guns gunsList : GunsPlugin.getWeaponManager().getGuns()) {
                        player.sendMessage(GunsPlugin.Color("  &7- &e"+gunsList.getName()+" &7Ammo: &e"+(gunsList.getAmmo() == null ? "Unknown" : gunsList.getAmmo().getName())));
                    }
                    player.sendMessage(GunsPlugin.Color("&6Explosives: "));
                    for(Explosive explosives : GunsPlugin.getWeaponManager().getExplosives()) {
                        player.sendMessage(GunsPlugin.Color("  &7- &e"+explosives.getName()));
                    }
                    return true;
                }
                if(args[0].equalsIgnoreCase("armor")) {
                    String name = args[1];
                    Armor armor = GunsPlugin.getWeaponManager().getArmor(name);
                    if(armor != null) {
                        if (args[2].equalsIgnoreCase("helmet")) {
                            if (armor.getHelmet() != null) {
                                armor.giveHelmet(player, Rarity.Common, false);
                            }
                            player.sendMessage(GunsPlugin.Color("&aYou were given the "+armor.getName()+" Helmet"));
                            return true;
                        }
                        if (args[2].equalsIgnoreCase("chestplate")) {
                            if (armor.getChestplate() != null) {
                                armor.giveChestplate(player, Rarity.Common, false);
                            }
                            player.sendMessage(GunsPlugin.Color("&aYou were given the "+armor.getName()+" Chestplate"));
                            return true;
                        }
                        if (args[2].equalsIgnoreCase("leggings")) {
                            if (armor.getLeggings() != null) {
                                armor.giveLeggings(player, Rarity.Common, false);
                            }
                            player.sendMessage(GunsPlugin.Color("&aYou were given the "+armor.getName()+" Leggings"));
                            return true;
                        }
                        if (args[2].equalsIgnoreCase("boots")) {
                            if (armor.getBoots() != null) {
                                armor.giveBoots(player, Rarity.Common, false);
                            }
                            player.sendMessage(GunsPlugin.Color("&aYou were given the "+armor.getName()+" Boots"));
                            return true;
                        }
                        player.sendMessage(GunsPlugin.Color("&eArmor pieces: Helmet, Chestplate, Leggings, Boots."));
                        return true;
                    }
                    return true;
                }
                if(args[0].equalsIgnoreCase("give")) {
                    String name = args[1];
                    Rarity rarity = Rarity.getRarity(args[2]);
                    Guns guns = GunsPlugin.getWeaponManager().getGun(name);
                    if(guns != null && guns.getGunItem() != null) {
                        guns.giveGunItem(player, rarity);
                        player.sendMessage(GunsPlugin.Color("&aYou were given the "+guns.getName()+" weapon"));
                        return true;
                    }
                    Explosive explosive = GunsPlugin.getWeaponManager().getExplosive(name);
                    if(explosive != null && explosive.getExplosiveItem() != null) {
                        explosive.giveExplosive(player);
                        player.sendMessage(GunsPlugin.Color("&aYou were given the "+explosive.getName()+" explosive"));
                        return true;
                    }
                    player.sendMessage(GunsPlugin.Color("&6Weapons: "));
                    for(Guns gunsList : GunsPlugin.getWeaponManager().getGuns()) {
                        player.sendMessage(GunsPlugin.Color("  &7- &e"+gunsList.getName()+" &7Ammo: &e"+(gunsList.getAmmo() == null ? "Unknown" : gunsList.getAmmo().getName())));
                    }
                    player.sendMessage(GunsPlugin.Color("&6Explosives: "));
                    for(Explosive explosives : GunsPlugin.getWeaponManager().getExplosives()) {
                        player.sendMessage(GunsPlugin.Color("  &7- &e"+explosives.getName()));
                    }
                    return true;
                }
                if(args[0].equalsIgnoreCase("medicine")) {
                    String name = args[1];
                    Player target = Bukkit.getPlayer(args[2]);
                    if(target != null) {
                        Medicine medicine = GunsPlugin.getWeaponManager().getMedicine(name);
                        if (medicine != null && medicine.getItemMed() != null) {
                            medicine.giveMedicine(target);
                            player.sendMessage(GunsPlugin.Color("&aYou were given the " + medicine.getName() + " medicine to "+target.getName()));
                            return true;
                        }
                        player.sendMessage(GunsPlugin.Color("&6Medicines: "));
                        for (Medicine medicines : GunsPlugin.getWeaponManager().getMedicines()) {
                            player.sendMessage(GunsPlugin.Color("  &7- &e" + medicines.getName()));
                        }
                        return true;
                    }
                    return true;
                }

                player.sendMessage(GunsPlugin.Color("&e/guns list"));
                player.sendMessage(GunsPlugin.Color("&e/guns give [Weapon Name] <Rarity> <Player>"));
                player.sendMessage(GunsPlugin.Color("&e/guns medicine [Medicine Name] <Player>"));
                player.sendMessage(GunsPlugin.Color("&e/guns ammo [Weapon Name] [Amount] <Player>"));
                player.sendMessage(GunsPlugin.Color("&e/guns armor [Armor Name] [Armor piece] <Rarity> <Player>"));
                player.sendMessage(GunsPlugin.Color("&e/guns list [All/Weapons/Explosives/Armors/Medicines]"));
                return true;
            }
            if(args.length == 2) {
                if(args[0].equalsIgnoreCase("armor")) {
                    player.sendMessage(GunsPlugin.Color("&eArmor pieces: Helmet, Chestplate, Leggings, Boots."));
                    return true;
                }
                if(args[0].equalsIgnoreCase("give")) {
                    String name = args[1];
                    Guns guns = GunsPlugin.getWeaponManager().getGun(name);
                    if(guns != null && guns.getGunItem() != null) {
                        guns.giveGunItem(player, Rarity.Common);
                        player.sendMessage(GunsPlugin.Color("&aYou were given the "+guns.getName()+" weapon"));
                        return true;
                    }
                    Explosive explosive = GunsPlugin.getWeaponManager().getExplosive(name);
                    if(explosive != null && explosive.getExplosiveItem() != null) {
                        explosive.giveExplosive(player);
                        player.sendMessage(GunsPlugin.Color("&aYou were given the "+explosive.getName()+" explosive"));
                        return true;
                    }
                    player.sendMessage(GunsPlugin.Color("&6Weapons: "));
                    for(Guns gunsList : GunsPlugin.getWeaponManager().getGuns()) {
                        player.sendMessage(GunsPlugin.Color("  &7- &e"+gunsList.getName()+" &7Ammo: &e"+(gunsList.getAmmo() == null ? "Unknown" : gunsList.getAmmo().getName())));
                    }
                    player.sendMessage(GunsPlugin.Color("&6Explosives: "));
                    for(Explosive explosives : GunsPlugin.getWeaponManager().getExplosives()) {
                        player.sendMessage(GunsPlugin.Color("  &7- &e"+explosives.getName()));
                    }
                    return true;
                }
                if(args[0].equalsIgnoreCase("medicine")) {
                    String name = args[1];
                    Medicine medicine = GunsPlugin.getWeaponManager().getMedicine(name);
                    if(medicine != null && medicine.getItemMed() != null) {
                        medicine.giveMedicine(player);
                        player.sendMessage(GunsPlugin.Color("&aYou were given the "+medicine.getName()+" medicine"));
                        return true;
                    }
                    player.sendMessage(GunsPlugin.Color("&6Medicines: "));
                    for (Medicine medicines : GunsPlugin.getWeaponManager().getMedicines()) {
                        player.sendMessage(GunsPlugin.Color("  &7- &e" + medicines.getName()));
                    }
                    return true;
                }
                if(args[0].equalsIgnoreCase("list")) {
                    if (args[1].equalsIgnoreCase("all")) {
                        player.sendMessage(GunsPlugin.Color("&6Weapons: "));
                        for (Guns guns : GunsPlugin.getWeaponManager().getGuns()) {
                            player.sendMessage(GunsPlugin.Color("  &7- &e" + guns.getName() + " &7Ammo: &e" + guns.getAmmo().getName()));
                        }
                        player.sendMessage(GunsPlugin.Color("&6Explosives: "));
                        for (Explosive explosive : GunsPlugin.getWeaponManager().getExplosives()) {
                            player.sendMessage(GunsPlugin.Color("  &7- &e" + explosive.getName()));
                        }
                        player.sendMessage(GunsPlugin.Color("&6Armors: "));
                        for (Armor armor : GunsPlugin.getWeaponManager().getArmors()) {
                            player.sendMessage(GunsPlugin.Color("  &7- &e" + armor.getName()));
                        }
                        player.sendMessage(GunsPlugin.Color("&6Medicines: "));
                        for (Medicine medicine : GunsPlugin.getWeaponManager().getMedicines()) {
                            player.sendMessage(GunsPlugin.Color("  &7- &e" + medicine.getName()));
                        }
                        return true;
                    }
                    if (args[1].equalsIgnoreCase("weapons")) {
                        player.sendMessage(GunsPlugin.Color("&6Weapons: "));
                        for (Guns guns : GunsPlugin.getWeaponManager().getGuns()) {
                            player.sendMessage(GunsPlugin.Color("  &7- &e" + guns.getName() + " &7Ammo: &e" + guns.getAmmo().getName()));
                        }
                        return true;
                    }
                    if (args[1].equalsIgnoreCase("explosives")) {
                        player.sendMessage(GunsPlugin.Color("&6Explosives: "));
                        for (Explosive explosive : GunsPlugin.getWeaponManager().getExplosives()) {
                            player.sendMessage(GunsPlugin.Color("  &7- &e" + explosive.getName()));
                        }
                        return true;
                    }
                    if (args[1].equalsIgnoreCase("armors")) {
                        player.sendMessage(GunsPlugin.Color("&6Armors: "));
                        for (Armor armor : GunsPlugin.getWeaponManager().getArmors()) {
                            player.sendMessage(GunsPlugin.Color("  &7- &e" + armor.getName()));
                        }
                        return true;
                    }
                    if (args[1].equalsIgnoreCase("medicines")) {
                        player.sendMessage(GunsPlugin.Color("&6Medicines: "));
                        for (Medicine medicine : GunsPlugin.getWeaponManager().getMedicines()) {
                            player.sendMessage(GunsPlugin.Color("  &7- &e" + medicine.getName()));
                        }
                        return true;
                    }
                }

                player.sendMessage(GunsPlugin.Color("&e/guns list"));
                player.sendMessage(GunsPlugin.Color("&e/guns give [Weapon Name] <Rarity> <Player>"));
                player.sendMessage(GunsPlugin.Color("&e/guns medicine [Medicine Name] <Player>"));
                player.sendMessage(GunsPlugin.Color("&e/guns ammo [Weapon Name] [Amount] <Player>"));
                player.sendMessage(GunsPlugin.Color("&e/guns armor [Armor Name] [Armor piece] <Rarity> <Player>"));
                player.sendMessage(GunsPlugin.Color("&e/guns list [All/Weapons/Explosives/Armors/Medicines]"));
                return true;
            }
            if(args.length == 1) {
                if(args[0].equalsIgnoreCase("list") || args[0].equalsIgnoreCase("give")) {
                    player.sendMessage(GunsPlugin.Color("&6Weapons: "));
                    for(Guns guns : GunsPlugin.getWeaponManager().getGuns()) {
                        player.sendMessage(GunsPlugin.Color("  &7- &e"+guns.getName()+" &7Ammo: &e"+(guns.getAmmo() == null ? "Unknown" : guns.getAmmo().getName())));
                    }
                    player.sendMessage(GunsPlugin.Color("&6Explosives: "));
                    for(Explosive explosive : GunsPlugin.getWeaponManager().getExplosives()) {
                        player.sendMessage(GunsPlugin.Color("  &7- &e"+explosive.getName()));
                    }
                    if(args[0].equalsIgnoreCase("list")) {
                        player.sendMessage(GunsPlugin.Color("&6Armors: "));
                        for(Armor armor : GunsPlugin.getWeaponManager().getArmors()) {
                            player.sendMessage(GunsPlugin.Color("  &7- &e"+armor.getName()));
                        }
                        player.sendMessage(GunsPlugin.Color("&6Medicines: "));
                        for (Medicine medicine : GunsPlugin.getWeaponManager().getMedicines()) {
                            player.sendMessage(GunsPlugin.Color("  &7- &e" + medicine.getName()));
                        }
                        return true;
                    }
                    return true;
                }

                player.sendMessage(GunsPlugin.Color("&e/guns list"));
                player.sendMessage(GunsPlugin.Color("&e/guns give [Weapon Name] <Rarity> <Player>"));
                player.sendMessage(GunsPlugin.Color("&e/guns medicine [Medicine Name] <Player>"));
                player.sendMessage(GunsPlugin.Color("&e/guns ammo [Weapon Name] [Amount] <Player>"));
                player.sendMessage(GunsPlugin.Color("&e/guns armor [Armor Name] [Armor piece] <Rarity> <Player>"));
                player.sendMessage(GunsPlugin.Color("&e/guns list [All/Weapons/Explosives/Armors/Medicines]"));
                return true;
            }
        } else {
            if(args.length <= 0 || args.length == 2 || args.length >= 6) {
                sender.sendMessage(GunsPlugin.Color("&e/guns list"));
                sender.sendMessage(GunsPlugin.Color("&e/guns give [Weapon Name] [Rarity] [Player]"));
                sender.sendMessage(GunsPlugin.Color("&e/guns medicine [Medicine Name] [Player]"));
                sender.sendMessage(GunsPlugin.Color("&e/guns ammo [Weapon Name] [Amount] [Player]"));
                sender.sendMessage(GunsPlugin.Color("&e/guns armor [Armor Name] [Armor piece] [Rarity] [Player]"));
                sender.sendMessage(GunsPlugin.Color("&e/guns list [All/Weapons/Explosives/Armors/Medicines]"));
                return true;
            }

            if(args.length == 5) {
                if(args[0].equalsIgnoreCase("armor")) {
                    String name = args[1];
                    Player target = Bukkit.getPlayer(args[4]);
                    Armor armor = GunsPlugin.getWeaponManager().getArmor(name);
                    if(target != null) {
                        if (armor != null) {
                            Rarity rarity = Rarity.getRarity(args[3]);
                            if (args[2].equalsIgnoreCase("helmet")) {
                                if (armor.getHelmet() != null) {
                                    armor.giveHelmet(target, rarity, false);
                                }
                                sender.sendMessage(GunsPlugin.Color("&aYou were given the " + armor.getName() + " Helmet to "+target.getName()));
                                return true;
                            }
                            if (args[2].equalsIgnoreCase("chestplate")) {
                                if (armor.getChestplate() != null) {
                                    armor.giveChestplate(target, rarity, false);
                                }
                                sender.sendMessage(GunsPlugin.Color("&aYou were given the " + armor.getName() + " Chestplate to "+target.getName()));
                                return true;
                            }
                            if (args[2].equalsIgnoreCase("leggings")) {
                                if (armor.getLeggings() != null) {
                                    armor.giveLeggings(target, rarity, false);
                                }
                                sender.sendMessage(GunsPlugin.Color("&aYou were given the " + armor.getName() + " Leggings to "+target.getName()));
                                return true;
                            }
                            if (args[2].equalsIgnoreCase("boots")) {
                                if (armor.getBoots() != null) {
                                    armor.giveBoots(target, rarity, false);
                                }
                                sender.sendMessage(GunsPlugin.Color("&aYou were given the " + armor.getName() + " Boots to "+target.getName()));
                                return true;
                            }
                            sender.sendMessage(GunsPlugin.Color("&eArmor pieces: Helmet, Chestplate, Leggings, Boots."));
                            return true;
                        }
                        return true;
                    }
                    return true;
                }

                sender.sendMessage(GunsPlugin.Color("&e/guns list"));
                sender.sendMessage(GunsPlugin.Color("&e/guns give [Weapon Name] [Rarity] [Player]"));
                sender.sendMessage(GunsPlugin.Color("&e/guns medicine [Medicine Name] [Player]"));
                sender.sendMessage(GunsPlugin.Color("&e/guns ammo [Weapon Name] [Amount] [Player]"));
                sender.sendMessage(GunsPlugin.Color("&e/guns armor [Armor Name] [Armor piece] [Rarity] [Player]"));
                sender.sendMessage(GunsPlugin.Color("&e/guns list [All/Weapons/Explosives/Armors/Medicines]"));
                return true;
            }

            if(args.length == 4) {
                if(args[0].equalsIgnoreCase("ammo")) {
                    String name = args[1];
                    Player target = Bukkit.getPlayer(args[3]);
                    if(target != null) {
                        Ammo ammo = GunsPlugin.getWeaponManager().getAmmo(name);
                        if(ammo != null && ammo.getAmmoItem() != null) {
                            if (Utils.isInt(args[2])) {
                                int amount = Integer.parseInt(args[2]);
                                for (int i = 0; i < amount; i++) {
                                    ammo.giveAmmoItem(target);
                                }
                                sender.sendMessage(GunsPlugin.Color("&aYou were given " + amount + " ammo " + ammo.getName()+ " to " + target.getName()));
                                return true;
                            }
                            return true;
                        }

                        Guns guns = GunsPlugin.getWeaponManager().getGun(name);
                        if (guns != null && guns.getAmmo().getAmmoItem() != null) {
                            if (Utils.isInt(args[2])) {
                                int amount = Integer.parseInt(args[2]);
                                for (int i = 0; i < amount; i++) {
                                    guns.giveAmmoItem(target);
                                }
                                sender.sendMessage(GunsPlugin.Color("&aYou were given " + amount + " ammo for the " + guns.getName() + " weapon to " + target.getName()));
                                return true;
                            }
                            return true;
                        }

                        Explosive explosive = GunsPlugin.getWeaponManager().getExplosive(name);
                        if (explosive != null && explosive.getExplosiveItem() != null) {
                            if (Utils.isInt(args[2])) {
                                int amount = Integer.parseInt(args[2]);
                                for (int i = 0; i < amount; i++) {
                                    explosive.giveExplosive(target);
                                }
                                sender.sendMessage(GunsPlugin.Color("&aYou were given " + amount + " " + explosive.getName() + " explosive to " + target.getName()));
                                return true;
                            }
                            return true;
                        }

                        sender.sendMessage(GunsPlugin.Color("&6Weapons: "));
                        for (Guns gunsList : GunsPlugin.getWeaponManager().getGuns()) {
                            sender.sendMessage(GunsPlugin.Color("  &7- &e" + gunsList.getName() + " &7Ammo: &e" + (gunsList.getAmmo() == null ? "Unknown" : gunsList.getAmmo().getName())));
                        }
                        sender.sendMessage(GunsPlugin.Color("&6Explosives: "));
                        for (Explosive explosives : GunsPlugin.getWeaponManager().getExplosives()) {
                            sender.sendMessage(GunsPlugin.Color("  &7- &e" + explosives.getName()));
                        }
                        return true;
                    }
                    return true;
                }
                if(args[0].equalsIgnoreCase("give")) {
                    String name = args[1];
                    Rarity rarity = Rarity.getRarity(args[2]);
                    Player target = Bukkit.getPlayer(args[3]);
                    if(target != null) {
                        Guns guns = GunsPlugin.getWeaponManager().getGun(name);
                        if (guns != null && guns.getGunItem() != null) {
                            guns.giveGunItem(target, rarity);
                            sender.sendMessage(GunsPlugin.Color("&aYou were given the " + guns.getName() + " weapon to "+target.getName()));
                            return true;
                        }
                        Explosive explosive = GunsPlugin.getWeaponManager().getExplosive(name);
                        if (explosive != null && explosive.getExplosiveItem() != null) {
                            explosive.giveExplosive(target);
                            sender.sendMessage(GunsPlugin.Color("&aYou were given the " + explosive.getName() + " explosive to "+target.getName()));
                            return true;
                        }
                        sender.sendMessage(GunsPlugin.Color("&6Weapons: "));
                        for (Guns gunsList : GunsPlugin.getWeaponManager().getGuns()) {
                            sender.sendMessage(GunsPlugin.Color("  &7- &e" + gunsList.getName() + " &7Ammo: &e" + (gunsList.getAmmo() == null ? "Unknown" : gunsList.getAmmo().getName())));
                        }
                        sender.sendMessage(GunsPlugin.Color("&6Explosives: "));
                        for (Explosive explosives : GunsPlugin.getWeaponManager().getExplosives()) {
                            sender.sendMessage(GunsPlugin.Color("  &7- &e" + explosives.getName()));
                        }
                        return true;
                    }
                    return true;
                }

                sender.sendMessage(GunsPlugin.Color("&e/guns list"));
                sender.sendMessage(GunsPlugin.Color("&e/guns give [Weapon Name] [Rarity] [Player]"));
                sender.sendMessage(GunsPlugin.Color("&e/guns medicine [Medicine Name] [Player]"));
                sender.sendMessage(GunsPlugin.Color("&e/guns ammo [Weapon Name] [Amount] [Player]"));
                sender.sendMessage(GunsPlugin.Color("&e/guns armor [Armor Name] [Armor piece] [Rarity] [Player]"));
                sender.sendMessage(GunsPlugin.Color("&e/guns list [All/Weapons/Explosives/Armors/Medicines]"));
                return true;
            }

            if(args.length == 3) {
                if(args[0].equalsIgnoreCase("medicine")) {
                    String name = args[1];
                    Player target = Bukkit.getPlayer(args[2]);
                    if(target != null) {
                        Medicine medicine = GunsPlugin.getWeaponManager().getMedicine(name);
                        if (medicine != null && medicine.getItemMed() != null) {
                            medicine.giveMedicine(target);
                            sender.sendMessage(GunsPlugin.Color("&aYou were given the " + medicine.getName() + " medicine to "+target.getName()));
                            return true;
                        }
                        sender.sendMessage(GunsPlugin.Color("&6Medicines: "));
                        for (Medicine medicines : GunsPlugin.getWeaponManager().getMedicines()) {
                            sender.sendMessage(GunsPlugin.Color("  &7- &e" + medicines.getName()));
                        }
                        return true;
                    }
                    return true;
                }

                sender.sendMessage(GunsPlugin.Color("&e/guns list"));
                sender.sendMessage(GunsPlugin.Color("&e/guns give [Weapon Name] [Rarity] [Player]"));
                sender.sendMessage(GunsPlugin.Color("&e/guns medicine [Medicine Name] [Player]"));
                sender.sendMessage(GunsPlugin.Color("&e/guns ammo [Weapon Name] [Amount] [Player]"));
                sender.sendMessage(GunsPlugin.Color("&e/guns armor [Armor Name] [Armor piece] [Rarity] [Player]"));
                sender.sendMessage(GunsPlugin.Color("&e/guns list [All/Weapons/Explosives/Armors/Medicines]"));
                return true;
            }

            if(args.length == 1) {
                if (args[0].equalsIgnoreCase("list") || args[0].equalsIgnoreCase("give")) {
                    sender.sendMessage(GunsPlugin.Color("&6Weapons: "));
                    for (Guns guns : GunsPlugin.getWeaponManager().getGuns()) {
                        sender.sendMessage(GunsPlugin.Color("  &7- &e" + guns.getName() + " &7Ammo: &e" + (guns.getAmmo() == null ? "Unknown" : guns.getAmmo().getName())));
                    }
                    sender.sendMessage(GunsPlugin.Color("&6Explosives: "));
                    for (Explosive explosive : GunsPlugin.getWeaponManager().getExplosives()) {
                        sender.sendMessage(GunsPlugin.Color("  &7- &e" + explosive.getName()));
                    }
                    if (args[0].equalsIgnoreCase("list")) {
                        sender.sendMessage(GunsPlugin.Color("&6Armors: "));
                        for (Armor armor : GunsPlugin.getWeaponManager().getArmors()) {
                            sender.sendMessage(GunsPlugin.Color("  &7- &e" + armor.getName()));
                        }
                        sender.sendMessage(GunsPlugin.Color("&6Medicines: "));
                        for (Medicine medicine : GunsPlugin.getWeaponManager().getMedicines()) {
                            sender.sendMessage(GunsPlugin.Color("  &7- &e" + medicine.getName()));
                        }
                        return true;
                    }
                    return true;
                }

                sender.sendMessage(GunsPlugin.Color("&e/guns list"));
                sender.sendMessage(GunsPlugin.Color("&e/guns give [Weapon Name] [Rarity] [Player]"));
                sender.sendMessage(GunsPlugin.Color("&e/guns medicine [Medicine Name] [Player]"));
                sender.sendMessage(GunsPlugin.Color("&e/guns ammo [Weapon Name] [Amount] [Player]"));
                sender.sendMessage(GunsPlugin.Color("&e/guns armor [Armor Name] [Armor piece] [Rarity] [Player]"));
                sender.sendMessage(GunsPlugin.Color("&e/guns list [All/Weapons/Explosives/Armors/Medicines]"));
                return true;
            }
        }
        return false;
    }
}
