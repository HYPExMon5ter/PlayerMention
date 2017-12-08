package methods;

import net.hypexmon5ter.pm.PlayerMention;
import org.bukkit.entity.Player;

public class MentionEveryone {

    private PlayerMention PM;

    public MentionEveryone(PlayerMention PM) {
        this.PM = PM;
    }

    public void mentionEveryone(Player mentioner) {
        mentioner.sendMessage("mentioned everyone");
        //mention everyone code
    }
}
