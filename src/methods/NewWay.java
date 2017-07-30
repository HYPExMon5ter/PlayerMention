package methods;

import net.hypexmon5ter.pm.PlayerMention;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class NewWay {

    Misc misc;
    Mentioned mentioned;
    private PlayerMention PM;

    public NewWay(PlayerMention PM) {
        this.PM = PM;
        misc = new Misc(PM);
        mentioned = new Mentioned(PM);
    }

    public void checkIfMentionedNewWay(String message, Player sender) {
        String msg = message.toLowerCase();
        String[] split = msg.split(" ");
        //String newMessage = message;
        for (final Player p : Bukkit.getServer().getOnlinePlayers()) {
            if (Arrays.asList(split).contains(PM.needsPrefix ? PM.regPrefix + p.getName().toLowerCase() : p.getName().toLowerCase())) {
                if (!(PM.excluded.contains(p.getPlayer().getUniqueId()) || (PM.cooldown.contains(p.getPlayer())/* || (sender.getName() == p.getName())*/))) {
                    mentioned.mention(sender, p);
                }
            }
        }
    }
}