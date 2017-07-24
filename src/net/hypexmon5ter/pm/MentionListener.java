package net.hypexmon5ter.pm;

import methods.NewWay;
import methods.OldWay;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class MentionListener implements Listener {

    OldWay oldway;
    NewWay newway;
    private PlayerMention PM;

    public MentionListener(PlayerMention PM) {
        this.PM = PM;
        oldway = new OldWay(PM);
        newway = new NewWay(PM);
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        if (PM.getConfig().getBoolean("needPermissionToMention")) {
            if (e.getPlayer().hasPermission("pm.use")) {
                if (PM.useOldWay) {
                    oldway.checkIfMentionedOldWay(e.getMessage(), e.getPlayer());
                } else {
                    newway.checkIfMentionedNewWay(e.getMessage(), e.getPlayer());
                }
            }
        } else {
            if (PM.useOldWay) {
                oldway.checkIfMentionedOldWay(e.getMessage(), e.getPlayer());
            } else {
                newway.checkIfMentionedNewWay(e.getMessage(), e.getPlayer());
            }
        }
    }
}