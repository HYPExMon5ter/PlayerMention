package net.hypexmon5ter.pm;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.util.SimpleEvent;
import com.earth2me.essentials.Essentials;
import me.clip.placeholderapi.PlaceholderAPI;
import net.hypexmon5ter.pm.commands.MainCommand;
import net.hypexmon5ter.pm.events.MentionListener;
import net.hypexmon5ter.pm.events.OnMentionEvent;
import net.hypexmon5ter.pm.events.onJoin;
import net.hypexmon5ter.pm.methods.UpdateChecker;
import net.hypexmon5ter.pm.skript.EventVals;
import net.hypexmon5ter.pm.skript.ExprPlayerMentioned;
import net.hypexmon5ter.pm.utils.ConfigManager;
import net.hypexmon5ter.pm.utils.Metrics;
import net.hypexmon5ter.pm.utils.TabCompleteHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PlayerMention extends JavaPlugin {

    private static PlayerMention instance;

    public Set<UUID> excluded = new HashSet<>();
    public ArrayList<Player> cooldown = new ArrayList<>();

    public boolean needsUpdate;

    public boolean useOldWay;
    public boolean needsPrefix;
    public boolean needsPermission;
    public boolean updateNotifications;

    //public boolean essentialsHook;
    public boolean factionChatHook;
    public boolean mcmmoHook;
    public boolean superVanishHook;
    public boolean premiumVanishHook;
    public boolean marriageHook;

    private boolean isPAPIEnabled;
    private boolean isMVdWEnabled;
    private boolean isSkriptEnabled;

    public String regPrefix;
    public boolean regTitlesEnabled;
    public String regTitle;
    public String regSubtitle;
    public boolean regActionbarEnabled;
    public String regActionbar;
    public boolean regParticlesEnabled;
    public String regParticle;
    public String regSound;
    public boolean regCooldownEnabled;
    public int regCooldown;
    public boolean regReplacementEnabled;
    public String regReplacement;
    public boolean regMessageEnabled;
    public String regMessage;

    public String everyonePrefix;
    public boolean everyoneTitlesEnabled;
    public String everyoneTitle;
    public String everyoneSubtitle;
    public boolean everyoneActionbarEnabled;
    public String everyoneActionbar;
    public boolean everyoneParticlesEnabled;
    public String everyoneParticle;
    public String everyoneSound;
    public boolean everyoneCooldownEnabled;
    public int everyoneCooldown;
    public boolean everyoneReplacementEnabled;
    public String everyoneReplacement;
    public boolean everyoneMessageEnabled;
    public String everyoneMessage;

    public String prefix;
    public String noPermission;
    public String success;
    public String notAValidPath;
    public String currentValue;
    public String availablePaths;
    public String canOnlyBeBool;
    public String canOnlyBeNumber;
    public String canOnlyBeSound;
    public String canOnlyBeParticle;
    public String reloadSuccess;
    public String everyoneSuccess;
    public String toggleOn;
    public String toggleOff;

    public ConfigManager msgs;
    private ConsoleCommandSender console;

    public void onEnable() {
        instance = this;

        checkHooks();

        msgs = new ConfigManager(this.getDataFolder().getPath(), "messages.yml", this);
        msgs.create(true);
        getConfig().options().copyDefaults(true);
        saveConfig();

        cacheConfigs();

        PluginDescriptionFile pdfFile = getDescription();

        console = Bukkit.getConsoleSender();
        console.sendMessage(prefix + "§ahas successfully loaded, enjoy!");
        console.sendMessage("§8-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
        console.sendMessage("Info:");
        console.sendMessage("Running version: " + pdfFile.getVersion());
        if (updateNotifications) {
            UpdateChecker.check(
                    version -> console.sendMessage(parseColors("&aA new update is out for PlayerMention! Download it here: https://www.spigotmc.org/resources/playermention.8963/")),
                    exception -> console.sendMessage(parseColors("&cAn error occured when checking for an update for PlayerMention."))
            );
        }
        if (isPAPIEnabled) {
            console.sendMessage("Hooked into PlaceholderAPI");
        }
        if (isMVdWEnabled) {
            console.sendMessage("Hooked into MVdW's Placeholder API");
        }
        /*if (essentialsHook) {
            console.sendMessage("Hooked into Essentials");
        }*/
        if (factionChatHook) {
            console.sendMessage("Hooked into FactionChat");
        }
        if (mcmmoHook) {
            console.sendMessage("Hooked into mcMMO");
        }
        if (superVanishHook) {
            console.sendMessage("Hooked into Super Vanish");
        }
        if (premiumVanishHook) {
            console.sendMessage("Hooked into Premium Vanish");
        }
        if (marriageHook) {
            console.sendMessage("Hooked into Marriage");
        }
        if (isSkriptEnabled) {
            console.sendMessage("Hooked into Skript");
        }
        console.sendMessage("§8-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");

        getServer().getPluginManager().registerEvents(new onJoin(this), this);
        getServer().getPluginManager().registerEvents(new MentionListener(this), this);

        getCommand("playermention").setExecutor(new MainCommand(this));
        getCommand("playermention").setTabCompleter(new TabCompleteHandler(this));

        if (isSkriptEnabled) {
            Skript.registerAddon(this);
            Skript.registerExpression(ExprPlayerMentioned.class, Player.class, ExpressionType.SIMPLE, "[the] mentioned player");
            Skript.registerEvent("Player Mention", SimpleEvent.class, OnMentionEvent.class, "mention [of player]");

            new EventVals();
        }

        Metrics metrics = new Metrics(this);
        metrics.addCustomChart(new Metrics.SimplePie("update_notifications", () -> String.valueOf(updateNotifications)));
        metrics.addCustomChart(new Metrics.SimplePie("usingOldWay", () -> String.valueOf(useOldWay)));
        metrics.addCustomChart(new Metrics.SimplePie("needsPrefix", () -> String.valueOf(needsPrefix)));
        metrics.addCustomChart(new Metrics.SimplePie("needsPermToMention", () -> String.valueOf(needsPermission)));

    }

    public void onDisable() {

    }

    public String parseColors(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public String convertPlaceholders(Player p, String msg) {
        if (isPAPIEnabled) {
            return parseColors(PlaceholderAPI.setPlaceholders(p.getPlayer(), msg));
        } else if (isMVdWEnabled) {
            return parseColors(be.maximvdw.placeholderapi.PlaceholderAPI.replacePlaceholders(p.getPlayer(), msg));
        } else {
            return parseColors(msg);
        }
    }

    public static PlayerMention getInstance() {
        if (instance == null) {
            instance = new PlayerMention();
        }
        return instance;
    }

    private void checkHooks() {
        /*if (!(Bukkit.getPluginManager().isPluginEnabled("Essentials"))) {
            getConfig().set("hooks.Essentials", false);
        }*/
        if (!(Bukkit.getPluginManager().isPluginEnabled("FactionChat"))) {
            getConfig().set("hooks.FactionChat", false);
        }
        if (!(Bukkit.getPluginManager().isPluginEnabled("mcMMO"))) {
            getConfig().set("hooks.mcMMO", false);
        }
        if (!(Bukkit.getPluginManager().isPluginEnabled("SuperVanish"))) {
            getConfig().set("hooks.SuperVanish", false);
        }
        if (!(Bukkit.getPluginManager().isPluginEnabled("PremiumVanish"))) {
            getConfig().set("hooks.PremiumVanish", false);
        }
        if (!(Bukkit.getPluginManager().isPluginEnabled("Marriage"))) {
            getConfig().set("hooks.Marriage", false);
        }
        saveConfig();
        isPAPIEnabled = Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI");
        isMVdWEnabled = Bukkit.getPluginManager().isPluginEnabled("MVdWPlaceholderAPI");
        isSkriptEnabled = Bukkit.getPluginManager().isPluginEnabled("Skript");
    }

    public void cacheConfigs() {
        //config
        useOldWay = getConfig().getBoolean("useOldWay");
        needsPrefix = getConfig().getBoolean("needsPrefix");
        needsPermission = getConfig().getBoolean("needPermissionToMention");
        updateNotifications = getConfig().getBoolean("updateNotifications");

        //essentialsHook = getConfig().getBoolean("hooks.Essentials");
        factionChatHook = getConfig().getBoolean("hooks.FactionChat");
        mcmmoHook = getConfig().getBoolean("hooks.mcMMO");
        superVanishHook = getConfig().getBoolean("hooks.SuperVanish");
        premiumVanishHook = getConfig().getBoolean("hooks.PremiumVanish");
        marriageHook = getConfig().getBoolean("hooks.Marriage");

        regPrefix = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Regular.prefix"));
        regTitlesEnabled = getConfig().getBoolean("Regular.titles.enabled");
        regTitle = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Regular.titles.title"));
        regSubtitle = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Regular.titles.subtitle"));
        regActionbarEnabled = getConfig().getBoolean("Regular.actionbar.enabled");
        regActionbar = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Regular.actionbar.text"));
        regParticlesEnabled = getConfig().getBoolean("Regular.particles.enabled");
        regParticle = getConfig().getString("Regular.particles.particle-type");
        regSound = getConfig().getString("Regular.sound");
        regCooldownEnabled = getConfig().getBoolean("Regular.cooldown.enabled");
        regCooldown = getConfig().getInt("Regular.cooldown.time");
        regReplacementEnabled = getConfig().getBoolean("Regular.replacement.enabled");
        regReplacement = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Regular.replacement.text"));
        regMessageEnabled = getConfig().getBoolean("Regular.message.enabled");
        regMessage = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Regular.message.text"));

        everyonePrefix = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Everyone.prefix"));
        everyoneTitlesEnabled = getConfig().getBoolean("Everyone.titles.enabled");
        everyoneTitle = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Everyone.titles.title"));
        everyoneSubtitle = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Everyone.titles.subtitle"));
        everyoneActionbarEnabled = getConfig().getBoolean("Everyone.actionbar.enabled");
        everyoneActionbar = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Everyone.actionbar.text"));
        everyoneParticlesEnabled = getConfig().getBoolean("Everyone.particles.enabled");
        everyoneParticle = getConfig().getString("Everyone.particles.particle-type");
        everyoneSound = getConfig().getString("Everyone.sound");
        everyoneCooldownEnabled = getConfig().getBoolean("Everyone.cooldown.enabled");
        everyoneCooldown = getConfig().getInt("Everyone.cooldown.time");
        everyoneReplacementEnabled = getConfig().getBoolean("Everyone.replacement.enabled");
        everyoneReplacement = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Everyone.replacement.text"));
        everyoneMessageEnabled = getConfig().getBoolean("Everyone.message.enabled");
        everyoneMessage = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Everyone.message.text"));


        //messages
        prefix = ChatColor.translateAlternateColorCodes('&', msgs.getConfig().getString("prefix"));
        noPermission = prefix + ChatColor.translateAlternateColorCodes('&', msgs.getConfig().getString("no-permission"));
        success = prefix + ChatColor.translateAlternateColorCodes('&', msgs.getConfig().getString("commands.success"));
        notAValidPath = prefix + ChatColor.translateAlternateColorCodes('&', msgs.getConfig().getString("commands.not-a-valid-path"));
        currentValue = prefix + ChatColor.translateAlternateColorCodes('&', msgs.getConfig().getString("commands.current-value"));
        availablePaths = prefix + ChatColor.translateAlternateColorCodes('&', msgs.getConfig().getString("commands.available-paths"));
        canOnlyBeBool = prefix + ChatColor.translateAlternateColorCodes('&', msgs.getConfig().getString("commands.config.can-only-be-boolean"));
        canOnlyBeNumber = prefix + ChatColor.translateAlternateColorCodes('&', msgs.getConfig().getString("commands.config.can-only-be-number"));
        canOnlyBeSound = prefix + ChatColor.translateAlternateColorCodes('&', msgs.getConfig().getString("commands.config.can-only-be-sound"));
        canOnlyBeParticle = prefix + ChatColor.translateAlternateColorCodes('&', msgs.getConfig().getString("commands.config.can-only-be-particle"));
        reloadSuccess = prefix + ChatColor.translateAlternateColorCodes('&', msgs.getConfig().getString("commands.reload.success"));
        everyoneSuccess = prefix + ChatColor.translateAlternateColorCodes('&', msgs.getConfig().getString("commands.everyone.success"));
        toggleOn = prefix + ChatColor.translateAlternateColorCodes('&', msgs.getConfig().getString("commands.toggle.enabled"));
        toggleOff = prefix + ChatColor.translateAlternateColorCodes('&', msgs.getConfig().getString("commands.toggle.disabled"));

    }
}