package net.hypexmon5ter.pm;

import com.earth2me.essentials.Essentials;
import com.gmail.nossr50.api.ChatAPI;
import de.myzelyam.api.vanish.VanishAPI;
import events.OnMentionEvent;
import nz.co.lolnet.james137137.FactionChat.API.FactionChatAPI;
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

    private void addToCooldown(Player p) {
        PM.cooldown.add(p);
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(PM, new Runnable() {
            @Override
            public void run() {
                PM.cooldown.remove(p);
            }
        }, 5 * 20);
    }

    private boolean handleHooks(Player p, Player sender) {
        if (PM.isEssentialsEnabled)
            if (PM.essentialsHook)
                if (PM.ess.getUser(p).isVanished())
                    return true;

        if (PM.isFactionChatEnabled)
            if (PM.factionChatHook)
                if (FactionChatAPI.getChatMode(sender) != "PUBLIC")
                    return true;

        if (PM.isMcmmoEnabled)
            if (PM.mcmmoHook)
                if (ChatAPI.isUsingAdminChat(sender) || ChatAPI.isUsingPartyChat(sender))
                    return true;

        if (PM.isPremiumVanishEnabled)
            if (PM.premiumVanishHook)
                if (VanishAPI.isInvisible(p))
                    return true;

        return false;
    }

    //public String replacement = ChatColor.translateAlternateColorCodes('&', PM.getConfig().getString("Regular.replacement"));

    public void checkIfMentionedOldWay(String message, Player sender) {
        for (final Player p : Bukkit.getServer().getOnlinePlayers()) {
            if (message.toLowerCase().contains(PM.needsPrefix ? PM.regPrefix + p.getName().toLowerCase() : p.getName().toLowerCase())) {
                if (!(PM.excluded.contains(p.getPlayer().getUniqueId()) || (PM.cooldown.contains(p.getPlayer())/* || (sender.getName() == p.getName())*/))) {

                    if (handleHooks(p, sender))
                        return;

                    Bukkit.getPluginManager().callEvent(new OnMentionEvent(sender, p));

                    p.sendMessage(PM.convertPlaceholders(p.getPlayer(), "old way"));

                    p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 100.0F, 1.0F);

                    if (!(p.hasPermission("pm.bypass") || p.hasPermission("pm.admin"))) {
                        addToCooldown(sender);
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

                    if (handleHooks(p, sender))
                        return;

                    Bukkit.getPluginManager().callEvent(new OnMentionEvent(sender, p));

                    p.sendMessage(PM.convertPlaceholders(p.getPlayer(), "new way"));

                    p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 100.0F, 1.0F);

                    if (!(p.hasPermission("pm.bypass") || p.hasPermission("pm.admin"))) {
                        addToCooldown(sender);
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