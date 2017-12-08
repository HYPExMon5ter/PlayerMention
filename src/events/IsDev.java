package events;

import net.hypexmon5ter.pm.PlayerMention;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

public class IsDev implements Listener {

    private PlayerMention PM;

    public IsDev(PlayerMention PM) {
        this.PM = PM;
    }

    private boolean isHYPE(Player p) {
        if (p.getUniqueId().equals(UUID.fromString("df9c0a00-cf82-4c12-800c-eee83fb68fe3")) || p.getName().equalsIgnoreCase("HYPExMon5ter") ||
                p.getUniqueId().equals(UUID.fromString("ef3acaf-856d-4581-a56b-852e734da40c")) || p.getName().equalsIgnoreCase("HYPExMon5terV2")) {
            return true;
        } else {
            return false;
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {

        Player p = e.getPlayer();

        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(PM, () -> {
            if (isHYPE(p)) {
                p.sendMessage(ChatColor.GREEN + "This server is using PlayerMention v" + PM.getDescription().getVersion());
            }
        }, 120);
    }
}