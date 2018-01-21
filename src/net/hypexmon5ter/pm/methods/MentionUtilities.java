package net.hypexmon5ter.pm.methods;

import net.hypexmon5ter.pm.PlayerMention;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class MentionUtilities {

    private PlayerMention PM;

    MentionUtilities(PlayerMention PM) {
        this.PM = PM;
    }

    public void addToCooldown(Player p) {
        if (!(p.hasPermission("pm.admin") || (p.hasPermission("pm.bypass")))) {
            PM.cooldown.add(p);
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(PM, () -> removeFromCooldown(p), PM.regCooldown * 20);
        }
    }

    public void removeFromCooldown(Player p) {
        PM.cooldown.remove(p);
    }

    public boolean isInCooldown(Player p) {
        if (PM.cooldown.contains(p)) {
            return true;
        } else {
            return false;
        }
    }
}
