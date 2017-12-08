package methods;

import com.gmail.nossr50.api.ChatAPI;
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

    private boolean canMentionHasPerm = false;
    private boolean notInCooldown = false;
    private boolean notVanishedEss = false;
    private boolean notInFactionChat = false;
    private boolean notInMcmmoChat = false;
    private boolean notVanishedPremV = false;

    public boolean canMention(Player mentioner) {
        if (PM.isEssentialsEnabled) {
            if (PM.essentialsHook) {
                if (!(PM.ess.getUser(mentioner).isVanished())) {
                    notVanishedEss = true;
                }
            }
        } else {
            notVanishedEss = true;
        }
        if (PM.isFactionChatEnabled) {
            if (PM.factionChatHook) {
                if (FactionChatAPI.getChatMode(mentioner).equals("PUBLIC")) {
                    notInFactionChat = true;
                }
            }
        } else {
            notInFactionChat = true;
        }
        if (PM.isMcmmoEnabled) {
            if (PM.mcmmoHook) {
                if (!(ChatAPI.isUsingAdminChat(mentioner) || ChatAPI.isUsingPartyChat(mentioner))) {
                    notInMcmmoChat = true;
                }
            }
        } else {
            notInMcmmoChat = true;
        }
        if (PM.isPremiumVanishEnabled) {
            if (PM.premiumVanishHook) {
                if (!(VanishAPI.isInvisible(mentioner))) {
                    notVanishedPremV = true;
                }
            }
        } else {
            notVanishedPremV = true;
        }
        if (PM.needsPermission) {
            if (mentioner.hasPermission("pm.use")) {
                canMentionHasPerm = true;
            }
        } else {
            canMentionHasPerm = true;
        }
        if (!(MU.isInCooldown(mentioner))) {
            notInCooldown = true;
        }
        return canMentionHasPerm && notInCooldown && notVanishedEss && notInFactionChat && notInMcmmoChat && notVanishedPremV;
    }

    private boolean canBeMentionedHasPerm = false;
    private boolean isNotExcluded = false;

    public boolean canBeMentioned(Player target) {
        if (target.hasPermission("pm.receive")) {
            canBeMentionedHasPerm = true;
        }
        if (!(PM.excluded.contains(target.getUniqueId()))) {
            isNotExcluded = true;
        }
        return canBeMentionedHasPerm && isNotExcluded;
    }
}
