package net.hypexmon5ter.pm;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.util.SimpleEvent;
import com.earth2me.essentials.Essentials;
import events.OnMentionEvent;
import events.PlayerMentionCheck;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import skript.EventVals;
import skript.ExprPlayerMentioned;
import utils.ConfigManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PlayerMention extends JavaPlugin {

    public Set<UUID> excluded = new HashSet<>();
    public ArrayList<Player> cooldown = new ArrayList<>();

    public boolean useOldWay = getConfig().getBoolean("useOldWay");
    public boolean needsPrefix = getConfig().getBoolean("needsPrefix");
    public boolean needsPermission = getConfig().getBoolean("needPermissionToMention");

    public boolean isEssentialsEnabled;
    public boolean essentialsHook;
    public boolean isFactionChatEnabled;
    public boolean factionChatHook;
    public boolean isMcmmoEnabled;
    public boolean mcmmoHook;
    public boolean isPremiumVanishEnabled;
    public boolean premiumVanishHook;

    public boolean isPAPIEnabled;
    public boolean isMVdWEnabled;
    public boolean isSkriptEnabled;

    public String consolePrefix = "[PlayerMention] ";

    public String regPrefix;
    public boolean regTitlesEnabled;
    public String regTitle;
    public String regSubtitle;
    public boolean regActionbarEnabled;
    public String regActionbar;
    public boolean regParticlesEnabled;
    public String regParticle;
    public boolean regSoundsEnabled;
    public String regSound;
    public boolean regCooldownEnabled;
    public int regCooldown;
    public boolean regReplacementEnabled;
    public String regReplacement;
    public String regMessage;

    public String everyonePrefix;
    public boolean everyoneTitlesEnabled;
    public String everyoneTitle;
    public String everyoneSubtitle;
    public boolean everyoneActionbarEnabled;
    public String everyoneActionbar;
    public boolean everyoneParticlesEnabled;
    public String everyoneParticle;
    public boolean everyoneSoundsEnabled;
    public String everyoneSound;
    public boolean everyoneCooldownEnabled;
    public int everyoneCooldown;
    public boolean everyoneReplacementEnabled;
    public String everyoneReplacement;
    public String everyoneMessage;

    ConsoleCommandSender console;

    public Essentials ess;
    public ConfigManager msgs;

    public void onEnable() {
        checkHooks();

        msgs = new ConfigManager(this.getDataFolder().getPath(), "messages.yml", this);
        msgs.create(true);
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

        if (isSkriptEnabled) {
            Skript.registerAddon(this);
            Skript.registerExpression(ExprPlayerMentioned.class, Player.class, ExpressionType.SIMPLE, "[the] mentioned player");
            Skript.registerEvent("Player Mention", SimpleEvent.class, OnMentionEvent.class, "mention [of player]");

            new EventVals();
        }
    }

    public void onDisable() {

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
        essentialsHook = getConfig().getBoolean("hooks.Essentials");
        factionChatHook = getConfig().getBoolean("hooks.FactionChat");
        mcmmoHook = getConfig().getBoolean("hooks.mcMMO");
        premiumVanishHook = getConfig().getBoolean("hooks.PremiumVanish");
    }

    public void checkConfig() {

        regPrefix = getConfig().getString("Regular.prefix");

        regTitlesEnabled = getConfig().getBoolean("Regular.titles.enabled");
        regTitle = getConfig().getString("Regular.titles.title");
        regSubtitle = getConfig().getString("Regular.titles.subtitle");

        regActionbarEnabled = getConfig().getBoolean("Regular.actionbar.enabled");
        regActionbar = getConfig().getString("Regular.actionbar.text");

        regParticlesEnabled = getConfig().getBoolean("Regular.particles.enabled");
        regParticle = getConfig().getString("Regular.particles.particle-type");

        regSoundsEnabled = getConfig().getBoolean("Regular.sounds.enabled");
        regSound = getConfig().getString("Regular.sounds.sound");

        regCooldownEnabled = getConfig().getBoolean("Regular.cooldown.enabled");
        regCooldown = getConfig().getInt("Regular.cooldown.time");

        regReplacementEnabled = getConfig().getBoolean("Regular.replacement.enabled");
        regReplacement = getConfig().getString("Regular.replacement.text");

        regMessage = getConfig().getString("Regular.message");



        everyonePrefix = getConfig().getString("Everyone.prefix");

        everyoneTitlesEnabled = getConfig().getBoolean("Everyone.titles.enabled");
        everyoneTitle = getConfig().getString("Everyone.titles.title");
        everyoneSubtitle = getConfig().getString("Everyone.titles.subtitle");

        everyoneActionbarEnabled = getConfig().getBoolean("Everyone.actionbar.enabled");
        everyoneActionbar = getConfig().getString("Everyone.actionbar.text");

        everyoneParticlesEnabled = getConfig().getBoolean("Everyone.particles.enabled");
        everyoneParticle = getConfig().getString("Everyone.particles.particle-type");

        everyoneSoundsEnabled = getConfig().getBoolean("Everyone.sounds.enabled");
        everyoneSound = getConfig().getString("Everyone.sounds.sound");

        everyoneCooldownEnabled = getConfig().getBoolean("Everyone.cooldown.enabled");
        everyoneCooldown = getConfig().getInt("Everyone.cooldown.time");

        everyoneReplacementEnabled = getConfig().getBoolean("Everyone.replacement.enabled");
        everyoneReplacement = getConfig().getString("Everyone.replacement.text");

        everyoneMessage = getConfig().getString("Everyone.message");

    }
}