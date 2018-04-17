package net.hypexmon5ter.pm.methods;

import com.earth2me.essentials.Essentials;
import com.gmail.nossr50.api.ChatAPI;
import com.lenis0012.bukkit.marriage2.internal.MarriagePlugin;
import de.myzelyam.api.vanish.VanishAPI;
import net.hypexmon5ter.pm.PlayerMention;
import nz.co.lolnet.james137137.FactionChat.API.FactionChatAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class MentionChecks {

    private MentionUtilities MU;
    private PlayerMention PM;

    MentionChecks(PlayerMention PM) {
        this.PM = PM;
        MU = new MentionUtilities(PM);
    }

    //private boolean canMentionHasPerm = false;
    private boolean notInCooldown = false;
    private boolean notInFactionChat = false;
    private boolean notInMcmmoChat = false;
    private boolean notInMarriageChat = false;

    public boolean canMention(Player mentioner) {
        if (PM.factionChatHook) {
            if (FactionChatAPI.getChatMode(mentioner).equals("PUBLIC")) {
                notInFactionChat = true;
            }
        } else {
            notInFactionChat = true;
        }
        if (PM.mcmmoHook) {
            if (!(ChatAPI.isUsingAdminChat(mentioner) || ChatAPI.isUsingPartyChat(mentioner))) {
                notInMcmmoChat = true;
            }
        } else {
            notInMcmmoChat = true;
        }
        if (PM.marriageHook) {
            if (!(MarriagePlugin.getCore().getMPlayer(mentioner).isInChat())) {
                notInMarriageChat = true;
            }
        } else {
            notInMarriageChat = true;
        }
        /*if (PM.needsPermission) {
            if (mentioner.hasPermission("pm.use")) {
                canMentionHasPerm = true;
            }
        } else {
            canMentionHasPerm = true;
        }*/
        if (!(MU.isInCooldown(mentioner))) {
            notInCooldown = true;
        }
        return /*canMentionHasPerm && */notInCooldown && notInFactionChat && notInMcmmoChat && notInMarriageChat;
    }

    private boolean canBeMentionedHasPerm = false;
    private boolean isNotExcluded = false;
    //private boolean notVanishedEss = false;
    private boolean notVanishedPremV = false;

    public boolean canBeMentioned(Player target) {
        if (target.hasPermission("pm.receive")) {
            canBeMentionedHasPerm = true;
        }
        if (!(PM.excluded.contains(target.getUniqueId()))) {
            isNotExcluded = true;
        }
        /*if (PM.essentialsHook) {
            if (Bukkit.getPluginManager().isPluginEnabled("Essentials")) {
                Essentials ess = (Essentials) Bukkit.getServer().getPluginManager().getPlugin("Essentials");
                if (!(ess.getUser(target).isVanished())) {
                    notVanishedEss = true;
                }
            } else {
                notVanishedEss = true;
            }
        } else {
            notVanishedEss = true;
        }*/
        if (PM.premiumVanishHook || PM.superVanishHook) {
            if (!(VanishAPI.isInvisible(target))) {
                notVanishedPremV = true;
            }
        } else {
            notVanishedPremV = true;
        }
        return canBeMentionedHasPerm && isNotExcluded /*&& notVanishedEss*/ && notVanishedPremV;
    }
}
