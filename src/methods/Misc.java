package methods;

import com.gmail.nossr50.api.ChatAPI;
import de.myzelyam.api.vanish.VanishAPI;
import net.hypexmon5ter.pm.PlayerMention;
import nz.co.lolnet.james137137.FactionChat.API.FactionChatAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

class Misc {

    private PlayerMention PM;

    Misc(PlayerMention PM) {
        this.PM = PM;
    }

    void addToCooldown(Player p) {
        PM.cooldown.add(p);
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(PM, () -> PM.cooldown.remove(p), PM.regCooldown * 20);
    }

    boolean handleHooks(Player p, Player sender) {
        if (PM.isEssentialsEnabled)
            if (PM.essentialsHook)
                if (PM.ess.getUser(p).isVanished())
                    return true;

        if (PM.isFactionChatEnabled)
            if (PM.factionChatHook)
                if (!FactionChatAPI.getChatMode(sender).equals("PUBLIC"))
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
}