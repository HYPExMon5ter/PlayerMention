package net.hypexmon5ter.pm.methods;

import com.earth2me.essentials.Essentials;
import net.hypexmon5ter.pm.events.OnMentionEvent;
import net.hypexmon5ter.pm.PlayerMention;
import org.apache.commons.lang3.EnumUtils;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import net.hypexmon5ter.pm.utils.ActionbarAPI;
import net.hypexmon5ter.pm.utils.Sounds;
import net.hypexmon5ter.pm.utils.TitleAPI;

public class MentionPlayer {

    private String ver = ActionbarAPI.getServerVersion();

    private MentionChecks MC;
    private MentionUtilities MU;
    private PlayerMention PM;

    public MentionPlayer(PlayerMention PM) {
        this.PM = PM;
        MC = new MentionChecks(PM);
        MU = new MentionUtilities(PM);
    }

    public void mention(Player mentioner, Player target) {
        if (MC.canMention(mentioner)) {
            if (MC.canBeMentioned(target)) {
                if (PM.needsPermission) {
                    if (!(mentioner.hasPermission("pm.use"))) {
                        return;
                    }
                }
                /*if (PM.essentialsHook) {
                    if (Bukkit.getPluginManager().isPluginEnabled("Essentials")) {
                        Essentials ess = (Essentials) Bukkit.getServer().getPluginManager().getPlugin("Essentials");
                        if (ess.getUser(mentioner).isIgnoredPlayer(ess.getUser(target)) || ess.getUser(target).isIgnoredPlayer(ess.getUser(mentioner))) {
                            return;
                        }
                    }
                }*/
                OnMentionEvent event = new OnMentionEvent(mentioner, target);
                Bukkit.getPluginManager().callEvent(event);
                if (!(event.isCancelled())) {
                    if (PM.regMessageEnabled) {
                        target.sendMessage(PM.convertPlaceholders(mentioner, PM.regMessage.replaceAll("%player%", mentioner.getName()).replaceAll("%nick%", mentioner.getDisplayName())));
                    }

                    try {
                        if (EnumUtils.isValidEnum(Sound.class, PM.regSound)) {
                            target.playSound(target.getLocation(), Sound.valueOf(PM.regSound), 100.0F, 1.0F);
                        } else {
                            target.playSound(target.getLocation(), Sounds.valueOf(PM.regSound).bukkitSound(), 100.0F, 1.0F);
                        }
                    } catch (Exception e) {
                        System.err.println(PM.prefix + ChatColor.RED + "That is not a valid sound (Error at 'Regular.sound')");
                    }

                    if (PM.regTitlesEnabled) {
                        TitleAPI.sendTitle(target, 0, 5 * 20, 20, PM.convertPlaceholders(mentioner, PM.regTitle.replaceAll("%player%", mentioner.getName()).replaceAll("%nick%", mentioner.getDisplayName())), PM.convertPlaceholders(mentioner, PM.regSubtitle.replaceAll("%player%", mentioner.getName()).replaceAll("%nick%", mentioner.getDisplayName())));
                    }

                    if (!(ver.startsWith("v1_8_"))) {
                        if (PM.regActionbarEnabled) {
                            ActionbarAPI.sendActionBar(target, PM.convertPlaceholders(mentioner, PM.regActionbar.replaceAll("%player%", mentioner.getName()).replaceAll("%nick%", mentioner.getDisplayName())));
                        }

                        if (PM.regParticlesEnabled) {
                            new BukkitRunnable() {
                                double phi = 0;

                                public void run() {
                                    Location loc = target.getLocation();

                                    phi += Math.PI / 10;
                                    for (double theta = 0; theta <= 2 * Math.PI; theta += Math.PI / 60) {
                                        double r = 1.5;
                                        double x = r * Math.cos(theta) * Math.sin(phi);
                                        double y = r * Math.cos(phi) + 1.5;
                                        double z = r * Math.sin(theta) * Math.sin(phi);
                                        loc.add(x, y, z);
                                        target.spawnParticle(Particle.valueOf(PM.regParticle), loc, 0, 0, 0, 0, 1);
                                        loc.subtract(x, y, z);
                                    }

                                    if (phi > 3 * Math.PI) {
                                        this.cancel();
                                    }

                                }
                            }.runTaskTimer(PM, 0, 1);
                        }
                    }

                    if (!(mentioner.hasPermission("pm.bypass") || mentioner.hasPermission("pm.admin"))) {
                        MU.addToCooldown(mentioner);
                    }
                }
            }
        }
    }
}