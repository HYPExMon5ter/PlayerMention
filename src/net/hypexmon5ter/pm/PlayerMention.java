package net.hypexmon5ter.pm;

import utils.ConfigManager;
import ch.njol.skript.Skript;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import com.earth2me.essentials.Essentials;
import events.OnMentionEvent;
import events.PlayerMentionCheck;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import skript.ExprPlayerMentioned;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PlayerMention extends JavaPlugin {

    public Set<UUID> excluded = new HashSet<>();
    public ArrayList<Player> cooldown = new ArrayList<>();

    public boolean useOldWay;
    public boolean needsPrefix;
    public boolean essentialsHook;
    public boolean factionChatHook;
    public boolean mcmmoHook;
    public boolean premiumVanishHook;

    public String consolePrefix = "[PlayerMention] ";
    public String regPrefix = getConfig().getString("Regular.prefix");

    ConsoleCommandSender console;

    public Essentials ess;

    public File msgsfile;
    ConfigManager cm;

    public boolean isPAPIEnabled;
    public boolean isMVdWEnabled;
    public boolean isEssentialsEnabled;
    public boolean isFactionChatEnabled;
    public boolean isMcmmoEnabled;
    public boolean isPremiumVanishEnabled;
    public boolean isSkriptEnabled;

    public void onEnable() {
        checkHooks();

        initiateMsgs();
        getConfig().options().copyDefaults(true);
        saveConfig();

        checkHooksInConfig();

        PluginDescriptionFile pdfFile = getDescription();

        console = Bukkit.getConsoleSender();
        console.sendMessage(consolePrefix + "§ahas successfully loaded, enjoy!");
        console.sendMessage("§8-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
        console.sendMessage("Info:");
        console.sendMessage("Running version: " + pdfFile.getVersion());
        if (isPAPIEnabled)
            console.sendMessage("Hooked into PlaceholderAPI");
        if (isMVdWEnabled)
            console.sendMessage("Hooked into MVdW's Placeholder API");
        if (isEssentialsEnabled)
            ess = (Essentials) Bukkit.getServer().getPluginManager().getPlugin("Essentials");
        console.sendMessage("Hooked into Essentials");
        if (isFactionChatEnabled)
            console.sendMessage("Hooked into FactionChat");
        if (isMcmmoEnabled)
            console.sendMessage("Hooked into mcMMO");
        if (isPremiumVanishEnabled)
            console.sendMessage("Hooked into Premium/Super Vanish");
        if (isSkriptEnabled)
            console.sendMessage("Hooked into Skript");
        console.sendMessage("§8-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");

        getServer().getPluginManager().registerEvents(new MentionListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerMentionCheck(this), this);

        if (isSkriptEnabled)
            Skript.registerAddon(this);
            Skript.registerExpression(ExprPlayerMentioned.class, Player.class, ExpressionType.SIMPLE, "[the] mentioned player");
            Skript.registerEvent("Player Mention", SimpleEvent.class, OnMentionEvent.class, "mention [of player]");

            EventValues.registerEventValue(OnMentionEvent.class, Player.class,
                new Getter<Player, OnMentionEvent>(){
                    @Override
                    public Player get(OnMentionEvent event) {
                        return event.getPlayerMentioned();
                    }
                }, 0);
    }

    public void onDisable() {

    }

    public void initiateMsgs() {
        msgsfile = new File(getDataFolder(), "messages.yml");
        cm = new ConfigManager(this);
        cm.mkdir();
        cm.loadMsgs();
    }

    public String convertPlaceholders(Player p, String msg) {
        if (isPAPIEnabled)
            return PlaceholderAPI.setPlaceholders(p.getPlayer(), msg);
        else if (isMVdWEnabled)
            return be.maximvdw.placeholderapi.PlaceholderAPI.replacePlaceholders(p.getPlayer(), msg);
        else
            return msg;
    }

    public void checkHooks() {
        isPAPIEnabled = Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI");
        isMVdWEnabled = Bukkit.getPluginManager().isPluginEnabled("MVdWPlaceholderAPI");
        isEssentialsEnabled = Bukkit.getPluginManager().isPluginEnabled("Essentials");
        isFactionChatEnabled = Bukkit.getPluginManager().isPluginEnabled("FactionChat");
        isMcmmoEnabled = Bukkit.getPluginManager().isPluginEnabled("mcMMO");
        isPremiumVanishEnabled = Bukkit.getPluginManager().isPluginEnabled("SuperVanish") || Bukkit.getPluginManager().isPluginEnabled("PremiumVanish");
        isSkriptEnabled = Bukkit.getPluginManager().isPluginEnabled("Skript");
    }

    public void checkHooksInConfig() {
        useOldWay = getConfig().getBoolean("useOldWay");
        needsPrefix = getConfig().getBoolean("needsPrefix");
        essentialsHook = getConfig().getBoolean("hooks.Essentials");
        factionChatHook = getConfig().getBoolean("hooks.FactionChat");
        mcmmoHook = getConfig().getBoolean("hooks.mcMMO");
        premiumVanishHook = getConfig().getBoolean("hooks.PremiumVanish");
    }
}