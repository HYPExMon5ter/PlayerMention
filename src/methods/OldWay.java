package methods;

import net.hypexmon5ter.pm.PlayerMention;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class OldWay {

    Misc misc;
    Mentioned mentioned;
    private PlayerMention PM;

    public OldWay(PlayerMention PM) {
        this.PM = PM;
        misc = new Misc(PM);
        mentioned = new Mentioned(PM);
    }

    public void checkIfMentionedOldWay(String message, Player sender) {
        for (final Player p : Bukkit.getServer().getOnlinePlayers()) {
            if (message.toLowerCase().contains(PM.needsPrefix ? PM.regPrefix + p.getName().toLowerCase() : p.getName().toLowerCase())) {
                if (!(PM.excluded.contains(p.getPlayer().getUniqueId()) || (PM.cooldown.contains(p.getPlayer())/* || (sender.getName() == p.getName())*/))) {
                    mentioned.mention(sender, p);
                }
            }
        }
    }
}