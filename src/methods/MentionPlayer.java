package methods;

import events.OnMentionEvent;
import net.hypexmon5ter.pm.PlayerMention;
import org.apache.commons.lang3.EnumUtils;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import utils.ActionbarAPI;
import utils.Sounds;
import utils.TitleAPI;

public class MentionPlayer {

    ActionbarAPI bar = new ActionbarAPI();

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
                OnMentionEvent event = new OnMentionEvent(mentioner, target);
                Bukkit.getPluginManager().callEvent(event);
                if (!(event.isCancelled())) {
                    if (PM.regMessageEnabled) {
                        target.sendMessage(PM.convertPlaceholders(target.getPlayer(), PM.regMessage.replaceAll("%player%", target.getName()).replaceAll("%nick%", target.getDisplayName())));
                    }

                    try {
                        if (EnumUtils.isValidEnum(Sound.class, PM.regSound)) {
                            target.playSound(target.getLocation(), Sound.valueOf(PM.regSound), 100.0F, 1.0F);
                        } else {
                            target.playSound(target.getLocation(), Sounds.valueOf(PM.regSound).bukkitSound(), 100.0F, 1.0F);
                        }
                    } catch (Exception e) {
                        System.err.println(PM.consolePrefix + ChatColor.RED + "That is not a valid sound (Error at 'Regular.sound')");
                    }

                    if (PM.regTitlesEnabled) {
                        TitleAPI.sendTitle(target, 0, 5 * 20, 1 * 20, PM.regTitle.replaceAll("%player%", target.getName()).replaceAll("%nick%", target.getDisplayName()), PM.regSubtitle.replaceAll("%player%", target.getName()).replaceAll("%nick%", target.getDisplayName()));
                    }

                    if (PM.regActionbarEnabled) {
                        ActionbarAPI.sendActionBar(target, PM.regActionbar.replaceAll("%player%", target.getName()).replaceAll("%nick%", target.getDisplayName()));
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

                    if (!(target.hasPermission("pm.bypass") || target.hasPermission("pm.admin"))) {
                        MU.addToCooldown(target);
                    }
                }
            }
        }
    }
}

