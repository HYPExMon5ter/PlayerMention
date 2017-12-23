package events;

import methods.MentionEveryone;
import methods.MentionPlayer;
import net.hypexmon5ter.pm.PlayerMention;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Arrays;

public class MentionListener implements Listener {

    private MentionPlayer MP;
    private MentionEveryone ME;
    private PlayerMention PM;

    public MentionListener(PlayerMention PM) {
        this.PM = PM;
        MP = new MentionPlayer(PM);
        ME = new MentionEveryone(PM);
    }

    @EventHandler
    public void mentionListener(AsyncPlayerChatEvent e) {
        String msg = e.getMessage().toLowerCase();
        if (msg.contains(PM.everyonePrefix + "everyone")) {
            ME.mentionEveryone(e.getPlayer());
            /*if (PM.everyoneReplacementEnabled) {
                e.setMessage(msg.replaceAll(PM.everyonePrefix + "everyone", PM.convertPlaceholders(e.getPlayer(), PM.everyoneReplacement.replaceAll("%player%", e.getPlayer().getName()).replaceAll("%nick%", e.getPlayer().getDisplayName()))));
            }*/
        }
        String[] split = msg.split(" ");
        for (final Player p : Bukkit.getOnlinePlayers()) {
            if (p != e.getPlayer()) {
                if (PM.useOldWay ? msg.contains(PM.needsPrefix ? PM.regPrefix + p.getName().toLowerCase() : p.getName().toLowerCase()) : Arrays.asList(split).contains(PM.needsPrefix ? PM.regPrefix + p.getName().toLowerCase() : p.getName().toLowerCase())) {
                    MP.mention(e.getPlayer(), p.getPlayer());
                    /*if (PM.regReplacementEnabled) {
                        e.setMessage(e.getMessage().replaceAll(PM.needsPrefix ? PM.regPrefix + p.getName() : p.getName(), PM.convertPlaceholders(p, PM.regReplacement.replaceAll("%player%", p.getName()).replaceAll("%nick%", p.getDisplayName()))));
                    }*/
                }
            }
        }
    }
}
