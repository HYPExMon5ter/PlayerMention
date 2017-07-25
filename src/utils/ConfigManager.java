package utils;

import net.hypexmon5ter.pm.PlayerMention;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;

public class ConfigManager {

    private PlayerMention PM;

    public ConfigManager(PlayerMention PM) {
        this.PM = PM;
    }

    public YamlConfiguration msgs = new YamlConfiguration();

    public void mkdir() {
        if (!(PM.msgsfile.exists())) {
            PM.saveResource("messages.yml", false);
        }
    }

    public void loadMsgs() {
        try {
            msgs.load(PM.msgsfile);
        } catch (IOException | InvalidConfigurationException e) {
            System.err.println("Couldn't load the messages.yml");
        }
    }

    public YamlConfiguration getMsgs() {
        return msgs;
    }

    public void saveMsgs() {
        try {
            msgs.save(PM.msgsfile);
        } catch (IOException e) {
            System.err.println("Couldn't save the messages.yml");
        }
    }
}

