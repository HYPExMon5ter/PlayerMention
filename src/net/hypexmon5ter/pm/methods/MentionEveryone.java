package net.hypexmon5ter.pm.methods;

import net.hypexmon5ter.pm.events.OnMentionEvent;
import net.hypexmon5ter.pm.PlayerMention;
import org.apache.commons.lang3.EnumUtils;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import net.hypexmon5ter.pm.utils.ActionbarAPI;
import net.hypexmon5ter.pm.utils.Sounds;
import net.hypexmon5ter.pm.utils.TitleAPI;

public class MentionEveryone {

    private String ver = ActionbarAPI.getServerVersion();

    private MentionUtilities MU;
    private PlayerMention PM;

    public MentionEveryone(PlayerMention PM) {
        this.PM = PM;
        MU = new MentionUtilities(PM);
    }

    public void mentionEveryone(Player mentioner) {
        if (mentioner.hasPermission("pm.everyone")) {
            for (Player target : Bukkit.getOnlinePlayers()) {
                if (mentioner != target) {
                    OnMentionEvent event = new OnMentionEvent(mentioner, target);
                    Bukkit.getPluginManager().callEvent(event);
                    if (!(event.isCancelled())) {
                        if (PM.everyoneMessageEnabled) {
                            target.sendMessage(PM.convertPlaceholders(mentioner, PM.everyoneMessage.replaceAll("%player%", mentioner.getName()).replaceAll("%nick%", mentioner.getDisplayName())));
                        }

                        try {
                            if (EnumUtils.isValidEnum(Sound.class, PM.everyoneSound)) {
                                target.playSound(target.getLocation(), Sound.valueOf(PM.everyoneSound), 100.0F, 1.0F);
                            } else {
                                target.playSound(target.getLocation(), Sounds.valueOf(PM.everyoneSound).bukkitSound(), 100.0F, 1.0F);
                            }
                        } catch (Exception e) {
                            System.err.println(PM.prefix + ChatColor.RED + "That is not a valid sound (Error at 'Everyone.sound')");
                        }

                        if (PM.everyoneTitlesEnabled) {
                            TitleAPI.sendTitle(target, 0, 5 * 20, 20, PM.convertPlaceholders(mentioner, PM.everyoneTitle.replaceAll("%player%", mentioner.getName()).replaceAll("%nick%", mentioner.getDisplayName())), PM.convertPlaceholders(mentioner, PM.everyoneSubtitle.replaceAll("%player%", mentioner.getName()).replaceAll("%nick%", mentioner.getDisplayName())));
                        }

                        if (!(ver.startsWith("v1_8_"))) {
                            if (PM.everyoneActionbarEnabled) {
                                ActionbarAPI.sendActionBar(target, PM.convertPlaceholders(mentioner, PM.everyoneActionbar.replaceAll("%player%", mentioner.getName()).replaceAll("%nick%", mentioner.getDisplayName())));
                            }

                            if (PM.everyoneParticlesEnabled) {
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
                                            target.spawnParticle(Particle.valueOf(PM.everyoneParticle), loc, 0, 0, 0, 0, 1);
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
}
