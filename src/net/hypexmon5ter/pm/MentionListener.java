package net.hypexmon5ter.pm;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Arrays;

public class MentionListener implements Listener {

    private PlayerMention PM;

    public MentionListener(PlayerMention PM) {
        this.PM = PM;
    }

    //public String replacement = ChatColor.translateAlternateColorCodes('&', PM.getConfig().getString("Regular.replacement"));

    public void checkIfMentionedOldWay(String message, Player sender) {
        for (final Player p : Bukkit.getServer().getOnlinePlayers()) {
            if (message.toLowerCase().contains(PM.needsPrefix ? PM.regPrefix + p.getName().toLowerCase() : p.getName().toLowerCase())) {
                if (!(PM.excluded.contains(p.getPlayer().getUniqueId()) || (PM.cooldown.contains(p.getPlayer())/* || (sender.getName() == p.getName())*/))) {
                    p.sendMessage("old way");
                    p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 100.0F, 1.0F);

                    if (!(sender.hasPermission("pm.bypass") || sender.hasPermission("pm.admin"))) {
                        PM.cooldown.add(sender);
                        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(PM, new Runnable() {
                            @Override
                            public void run() {
                                PM.cooldown.remove(sender);
                            }
                        }, 5 * 20);
                    }
                }
            }
        }
    }

    public void checkIfMentionedNewWay(String message, Player sender) {
        String msg = message.toLowerCase();
        String[] split = msg.split(" ");
        //String newMessage = message;
        for (final Player p : Bukkit.getServer().getOnlinePlayers()) {
            if (Arrays.asList(split).contains(PM.needsPrefix ? PM.regPrefix + p.getName().toLowerCase() : p.getName().toLowerCase())) {
                if (!(PM.excluded.contains(p.getPlayer().getUniqueId()) || (PM.cooldown.contains(p.getPlayer())/* || (sender.getName() == p.getName())*/))) {
                    p.sendMessage("new way");
                    p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 100.0F, 1.0F);

                    if (!(sender.hasPermission("pm.bypass") || sender.hasPermission("pm.admin"))) {
                        PM.cooldown.add(sender);
                        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(PM, new Runnable() {
                            @Override
                            public void run() {
                                PM.cooldown.remove(sender);
                            }
                        }, 5 * 20);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        if (PM.getConfig().getBoolean("needPermissionToMention")) {
            if (e.getPlayer().hasPermission("pm.use")) {
                if (PM.useOldWay) {
                    checkIfMentionedOldWay(e.getMessage(), e.getPlayer());
                } else {
                    checkIfMentionedNewWay(e.getMessage(), e.getPlayer());
                }
            }
        } else {
            if (PM.useOldWay) {
                checkIfMentionedOldWay(e.getMessage(), e.getPlayer());
            } else {
                checkIfMentionedNewWay(e.getMessage(), e.getPlayer());
            }
        }
    }
}
