package methods;

import events.OnMentionEvent;
import net.hypexmon5ter.pm.PlayerMention;
import org.apache.commons.lang3.EnumUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import utils.Sounds;

public class Mentioned {

    Misc misc;
    private PlayerMention PM;

    public Mentioned(PlayerMention PM) {
        this.PM = PM;
        misc = new Misc(PM);
    }


    public void mention(Player sender, Player p) {
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
