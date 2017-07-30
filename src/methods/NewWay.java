package methods;

import events.OnMentionEvent;
import net.hypexmon5ter.pm.PlayerMention;
import org.apache.commons.lang3.EnumUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import utils.Sounds;

import java.util.Arrays;

public class NewWay {

    Misc misc;
    private PlayerMention PM;

    public NewWay(PlayerMention PM) {
        this.PM = PM;
        misc = new Misc(PM);
    }

    public void checkIfMentionedNewWay(String message, Player sender) {
        String msg = message.toLowerCase();
        String[] split = msg.split(" ");
        //String newMessage = message;
        for (final Player p : Bukkit.getServer().getOnlinePlayers()) {
            if (Arrays.asList(split).contains(PM.needsPrefix ? PM.regPrefix + p.getName().toLowerCase() : p.getName().toLowerCase())) {
                if (!(PM.excluded.contains(p.getPlayer().getUniqueId()) || (PM.cooldown.contains(p.getPlayer())/* || (sender.getName() == p.getName())*/))) {

                    if (misc.handleHooks(p, sender))
                        return;

                    OnMentionEvent event = new OnMentionEvent(sender, p);
                    Bukkit.getPluginManager().callEvent(event);
                    if (event.isCancelled())
                        return;

                    if (PM.regMessageEnabled) {
                        p.sendMessage(PM.convertPlaceholders(p.getPlayer(), PM.regMessage.replaceAll("%player%", p.getName()).replaceAll("%nick%", p.getDisplayName())));
                    }

                    try {
                        if (EnumUtils.isValidEnum(Sound.class, PM.regSound)) {
                            p.playSound(p.getLocation(), Sound.valueOf(PM.regSound), 100.0F, 1.0F);
                        } else {
                            p.playSound(p.getLocation(), Sounds.valueOf(PM.regSound).bukkitSound(), 100.0F, 1.0F);
                        }
                    } catch (Exception e) {
                        System.err.println(PM.consolePrefix + ChatColor.RED + "That is not a valid sound (Error at 'Regular.sounds.sound')");
                    }

                    if (!(p.hasPermission("pm.bypass") || p.hasPermission("pm.admin"))) {
                        misc.addToCooldown(sender);
                    }
                }
            }
        }
    }
}