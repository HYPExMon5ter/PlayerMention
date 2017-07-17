package net.hypexmon5ter.pm;

import org.bukkit.Bukkit;
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

    public void checkIfMentioned(String message) {

        if (PM.useOldWay) {
            for (final Player p : Bukkit.getServer().getOnlinePlayers()) {
                if (PM.needsPrefix) {
                    if (message.toLowerCase().contains(PM.prefix + p.getName().toLowerCase())) {
                        if (!(PM.exluded.contains(p.getPlayer().getUniqueId()))) {
                            p.sendMessage("(prefix) old way");
                        }
                    }
                } else {
                    if (message.toLowerCase().contains(p.getName().toLowerCase())) {
                        if (!(PM.exluded.contains(p.getPlayer().getUniqueId()))) {
                            p.sendMessage("old way");
                        }
                    }
                }
            }
        } else {
            String msg = message.toLowerCase();
            String[] split = msg.split(" ");
            //String newMessage = message;
            for (final Player p : Bukkit.getServer().getOnlinePlayers()) {
                if (PM.needsPrefix) {
                    if (Arrays.asList(split).contains(PM.prefix + p.getName().toLowerCase())) {
                        if (!(PM.exluded.contains(p.getPlayer().getUniqueId()))) {
                            p.sendMessage("(prefix) new way");
                        }
                    }
                } else {
                    if (Arrays.asList(split).contains(p.getName().toLowerCase())) {
                        if (!(PM.exluded.contains(p.getPlayer().getUniqueId()))) {
                            p.sendMessage("new way");
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        if (PM.getConfig().getBoolean("needPermissionToMention")) {
            if (e.getPlayer().hasPermission("pm.use")) {
                checkIfMentioned(e.getMessage());
            }
        } else {
            checkIfMentioned(e.getMessage());
        }
    }
}
