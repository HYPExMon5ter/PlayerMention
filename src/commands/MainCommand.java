package commands;

import net.hypexmon5ter.pm.PlayerMention;
import org.apache.commons.lang3.EnumUtils;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class MainCommand implements CommandExecutor {

    private PlayerMention PM;

    public MainCommand(PlayerMention PM) {
        this.PM = PM;
    }

    private String particles = EnumUtils.getEnumList(Particle.class).toString().replaceAll("\\[", "").replaceAll("]", "");
    private String sounds = EnumUtils.getEnumList(Sound.class).toString().replaceAll("\\[", "").replaceAll("]", "");

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender.hasPermission("pm.admin"))) {
            sender.sendMessage("You don't have permission");
            return false;
        }

        if (args.length == 0) {
            sender.sendMessage("Help menu.");
            return false;
        }

        if (args[0].equalsIgnoreCase("config")) {
            String configStrings = PM.getConfig().getKeys(true).toString().replaceAll("\\[", "").replaceAll("]", "");
            sender.sendMessage(configStrings);
        } else if (args[0].equalsIgnoreCase("messages")) {
            String messageStrings = PM.msgs.getConfig().getKeys(true).toString().replaceAll("\\[", "").replaceAll("]", "");
            sender.sendMessage(messageStrings);
        } else if (args[0].equalsIgnoreCase("reload")) {
            PM.reloadConfig();
            sender.sendMessage("Successfully reloaded PlayerMention.");
        } else if (args[0].equalsIgnoreCase("everyone")) {
            sender.sendMessage("Everyone");
        } else {
            sender.sendMessage("Not a valid argument");
        }

        return true;
    }
}
