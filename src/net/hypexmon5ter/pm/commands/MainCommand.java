package net.hypexmon5ter.pm.commands;

import net.hypexmon5ter.pm.methods.MentionEveryone;
import net.hypexmon5ter.pm.PlayerMention;
import net.hypexmon5ter.pm.utils.ActionbarAPI;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.apache.commons.lang3.EnumUtils;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import net.hypexmon5ter.pm.utils.Sounds;

import java.util.Arrays;

public class MainCommand implements CommandExecutor {

    private String ver = ActionbarAPI.getServerVersion();

    private String particles;
    private String sounds;

    private PlayerMention PM;
    private MentionEveryone ME;

    public MainCommand(PlayerMention PM) {
        this.PM = PM;
        ME = new MentionEveryone(PM);
    }

    private void helpMenu(Player sender) {
        sender.sendMessage(PM.parseColors("&b&l&m----------&8&l[&a&lPlayerMention Help Menu&8&l]&b&l&m----------"));
        sender.sendMessage(PM.parseColors("&e&oTIP: &7&oClick on commands to paste them in your chat or to run them."));
        sender.sendMessage("");
        if (sender.hasPermission("pm.admin")) {
            sender.spigot().sendMessage(new ComponentBuilder("○ ").color(net.md_5.bungee.api.ChatColor.GRAY)
                    .append("/playermention config <key> <value>")
                    .color(net.md_5.bungee.api.ChatColor.GREEN)
                    .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/playermention config <key> <value>"))
                    .create());
            sender.spigot().sendMessage(new ComponentBuilder("○ ").color(net.md_5.bungee.api.ChatColor.GRAY)
                    .append("/playermention messages <key> <value>")
                    .color(net.md_5.bungee.api.ChatColor.GREEN)
                    .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/playermention messages <key> <value>"))
                    .create());
            sender.spigot().sendMessage(new ComponentBuilder("○ ").color(net.md_5.bungee.api.ChatColor.GRAY)
                    .append("/playermention reload")
                    .color(net.md_5.bungee.api.ChatColor.GREEN)
                    .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/playermention reload"))
                    .create());
        }
        if (sender.hasPermission("pm.everyone")) {
            sender.spigot().sendMessage(new ComponentBuilder("○ ").color(net.md_5.bungee.api.ChatColor.GRAY)
                    .append("/playermention everyone")
                    .color(net.md_5.bungee.api.ChatColor.GREEN)
                    .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/playermention everyone"))
                    .create());
        }
        if (sender.hasPermission("pm.toggle")) {
            sender.spigot().sendMessage(new ComponentBuilder("○ ").color(net.md_5.bungee.api.ChatColor.GRAY)
                    .append("/playermention toggle")
                    .color(net.md_5.bungee.api.ChatColor.GREEN)
                    .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/playermention toggle"))
                    .create());
        }
    }

    private boolean isBoolean(String s) {
        try {
            Boolean.parseBoolean(s);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private boolean isInt(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        Player p = (Player) sender;

        if (args.length == 0) {
            helpMenu(p);
            return false;
        }

        if (args[0].equalsIgnoreCase("config")) {
            if (sender.hasPermission("pm.admin")) {
                String configStrings = PM.getConfig().getKeys(true).toString().replaceAll("\\[", "").replaceAll("]", "");
                String[] splitConfigStrings = configStrings.split(", ");
                if (args.length == 1) {
                    sender.sendMessage(PM.availablePaths.replaceAll("%paths%", configStrings));
                } else {
                    if (Arrays.asList(splitConfigStrings).contains(args[1])) {
                        if (args.length == 2) {
                            sender.sendMessage(PM.currentValue.replaceAll("%path%", args[1]).replaceAll("%value%", ChatColor.translateAlternateColorCodes('&', PM.getConfig().get(args[1]).toString())));
                        } else {
                            String[] boolStrings = {"Regular.titles.enabled", "Regular.actionbar.enabled", "Regular.particles.enabled"
                                    , "Regular.cooldown.enabled", "Regular.replacement.enabled", "Regular.message.enabled", "Everyone.titles.enabled"
                                    , "Everyone.actionbar.enabled", "Everyone.particles.enabled", "Everyone.cooldown.enabled", "Everyone.replacement.enabled"
                                    , "Everyone.message.enabled", "useOldWay", "needPermissionToMention", "needsPrefix", "updateNotifications"};
                            if (Arrays.asList(boolStrings).contains(args[1])) {
                                if (!(args[2].equalsIgnoreCase("true") || args[2].equalsIgnoreCase("false"))) {
                                    sender.sendMessage(PM.canOnlyBeBool);
                                } else {
                                    PM.getConfig().set(args[1], Boolean.valueOf(args[2].toLowerCase()));
                                    PM.saveConfig();
                                    PM.cacheConfigs();
                                    sender.sendMessage(PM.success.replaceAll("%path%", args[1]).replaceAll("%value%", args[2]));
                                }
                            } else if (args[1].equals("Regular.cooldown.time") || args[1].equals("Everyone.cooldown.time")) {
                                if (!(isInt(args[2]))) {
                                    sender.sendMessage(PM.canOnlyBeNumber);
                                } else {
                                    PM.getConfig().set(args[1], Integer.valueOf(args[2]));
                                    PM.saveConfig();
                                    PM.cacheConfigs();
                                    sender.sendMessage(PM.success.replaceAll("%path%", args[1]).replaceAll("%value%", args[2]));
                                }
                            } else if (args[1].equals("Regular.sound") || (args[1].equalsIgnoreCase("Everyone.sound"))) {
                                sounds = EnumUtils.getEnumList(Sound.class).toString().replaceAll("\\[", "").replaceAll("]", "");
                                if (!(EnumUtils.isValidEnum(Sound.class, args[2]))) {
                                    sender.sendMessage(PM.canOnlyBeSound.replaceAll("%sounds%", sounds));
                                } else {
                                    PM.getConfig().set(args[1], Sounds.valueOf(args[2]));
                                    PM.saveConfig();
                                    PM.cacheConfigs();
                                    sender.sendMessage(PM.success.replaceAll("%path%", args[1]).replaceAll("%value%", args[2]));
                                }
                            } else if (args[1].equals("Regular.particles.particle-type") || (args[1].equals("Everyone.particles.particle-type"))) {
                                if (!(ver.startsWith("v1_8_"))) {
                                    particles = EnumUtils.getEnumList(Particle.class).toString().replaceAll("\\[", "").replaceAll("]", "");
                                    if (!(EnumUtils.isValidEnum(Particle.class, args[2]))) {
                                        sender.sendMessage(PM.canOnlyBeParticle.replaceAll("%particles%", particles));
                                    } else {
                                        PM.getConfig().set(args[1], Particle.valueOf(args[2]));
                                        PM.saveConfig();
                                        PM.cacheConfigs();
                                        sender.sendMessage(PM.success.replaceAll("%path%", args[1]).replaceAll("%value%", args[2]));
                                    }
                                } else {
                                    sender.sendMessage(PM.prefix + PM.parseColors("&cParticles don't work on 1.8."));
                                }
                            } else {
                                StringBuilder sb = new StringBuilder();
                                for (int i = 2; i < args.length; i++) {
                                    sb.append(args[i]).append(" ");
                                }

                                String c = ChatColor.translateAlternateColorCodes('&', sb.toString().trim());

                                PM.getConfig().set(args[1], c);
                                PM.saveConfig();
                                PM.cacheConfigs();
                                sender.sendMessage(PM.success.replaceAll("%path%", args[1]).replaceAll("%value%", c));
                            }
                        }
                    } else {
                        sender.sendMessage(PM.notAValidPath);
                    }
                }
            } else {
                sender.sendMessage(PM.noPermission);
            }

        } else if (args[0].equalsIgnoreCase("messages")) {
            if (sender.hasPermission("pm.admin")) {
                String messageStrings = PM.msgs.getConfig().getKeys(true).toString().replaceAll("\\[", "").replaceAll("]", "");
                String[] splitMessageStrings = messageStrings.split(", ");
                if (args.length == 1) {
                    sender.sendMessage(PM.availablePaths.replaceAll("%paths%", messageStrings));
                } else {
                    if (Arrays.asList(splitMessageStrings).contains(args[1])) {
                        if (args.length == 2) {
                            sender.sendMessage(PM.currentValue.replaceAll("%path%", args[1]).replaceAll("%value%", ChatColor.translateAlternateColorCodes('&', PM.msgs.getConfig().get(args[1]).toString())));
                        } else {
                            StringBuilder sb = new StringBuilder();
                            for (int i = 2; i < args.length; i++) {
                                sb.append(args[i]).append(" ");
                            }

                            String m = ChatColor.translateAlternateColorCodes('&', sb.toString().trim());

                            PM.msgs.getConfig().set(args[1], m);
                            PM.msgs.saveConfig();
                            PM.cacheConfigs();
                            sender.sendMessage(PM.success.replaceAll("%path%", args[1]).replaceAll("%value%", m));
                        }
                    } else {
                        sender.sendMessage(PM.notAValidPath);
                    }
                }
            } else {
                sender.sendMessage(PM.noPermission);
            }

        } else if (args[0].equalsIgnoreCase("reload")) {
            if (sender.hasPermission("pm.admin")) {
                PM.reloadConfig();
                PM.msgs.reloadConfig();
                PM.cacheConfigs();
                sender.sendMessage(PM.reloadSuccess);
            } else {
                sender.sendMessage(PM.noPermission);
            }

        } else if (args[0].equalsIgnoreCase("everyone")) {
            if (sender.hasPermission("pm.everyone")) {
                ME.mentionEveryone(p);
                sender.sendMessage(PM.everyoneSuccess);
            } else {
                sender.sendMessage(PM.noPermission);
            }

        } else if (args[0].equalsIgnoreCase("toggle")) {
            if (sender.hasPermission("pm.toggle")) {
                if (PM.excluded.contains(p.getUniqueId())) {
                    PM.excluded.remove(p.getUniqueId());
                    sender.sendMessage(PM.toggleOff);
                } else {
                    PM.excluded.add(p.getUniqueId());
                    sender.sendMessage(PM.toggleOn);
                }
            } else {
                sender.sendMessage(PM.noPermission);
            }

        } else {
            helpMenu(p);
        }

        return true;
    }
}
