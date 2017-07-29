package methods;

import events.OnMentionEvent;
import net.hypexmon5ter.pm.PlayerMention;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import utils.Sounds;

public class OldWay {

    Misc misc;
    private PlayerMention PM;

    public OldWay(PlayerMention PM) {
        this.PM = PM;
        misc = new Misc(PM);
    }

    public void checkIfMentionedOldWay(String message, Player sender) {
        for (final Player p : Bukkit.getServer().getOnlinePlayers()) {
            if (message.toLowerCase().contains(PM.needsPrefix ? PM.regPrefix + p.getName().toLowerCase() : p.getName().toLowerCase())) {
                if (!(PM.excluded.contains(p.getPlayer().getUniqueId()) || (PM.cooldown.contains(p.getPlayer())/* || (sender.getName() == p.getName())*/))) {

                    if (misc.handleHooks(p, sender))
                        return;

                    OnMentionEvent event = new OnMentionEvent(sender, p);
                    Bukkit.getPluginManager().callEvent(event);
                    if (event.isCancelled())
                        return;

                    p.sendMessage(PM.convertPlaceholders(p.getPlayer(), "old way"));

                    p.playSound(p.getLocation(), Sounds.LEVEL_UP.bukkitSound(), 100.0F, 1.0F);

                    if (!(p.hasPermission("pm.bypass") || p.hasPermission("pm.admin"))) {
                        misc.addToCooldown(sender);
                    }
                }
            }
        }
    }
}