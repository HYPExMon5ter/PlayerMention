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

class Mentioned {

    ActionbarAPI bar = new ActionbarAPI();

    private Misc misc;
    private PlayerMention PM;

    Mentioned(PlayerMention PM) {
        this.PM = PM;
        misc = new Misc(PM);
    }

    void regMention(Player sender, Player p) {
        if (misc.handleHooks(p, sender))
            return;

        OnMentionEvent event = new OnMentionEvent(sender, p);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled())
            return;

        if (PM.regMessageEnabled) {
            p.sendMessage(PM.convertPlaceholders(p.getPlayer(), PM.regMessage.replaceAll("%player%", p.getName()).replaceAll("%nick%", p.getDisplayName())));
        }

        try {
            if (EnumUtils.isValidEnum(Sound.class, PM.regSound)) {
                p.playSound(p.getLocation(), Sound.valueOf(PM.regSound), 100.0F, 1.0F);
            } else {
                p.playSound(p.getLocation(), Sounds.valueOf(PM.regSound).bukkitSound(), 100.0F, 1.0F);
            }
        } catch (Exception e) {
            System.err.println(PM.consolePrefix + ChatColor.RED + "That is not a valid sound (Error at 'Regular.sound')");
        }

        if (PM.regTitlesEnabled) {
            TitleAPI.sendTitle(p, 0, 5*20, 1*20, PM.regTitle.replaceAll("%player%", p.getName()).replaceAll("%nick%", p.getDisplayName()), PM.regSubtitle.replaceAll("%player%", p.getName()).replaceAll("%nick%", p.getDisplayName()));
        }

        if (PM.regActionbarEnabled) {
            bar.sendActionBar(p, PM.regActionbar.replaceAll("%player%", p.getName()).replaceAll("%nick%", p.getDisplayName()));
        }

        if (PM.regParticlesEnabled) {
            new BukkitRunnable() {
                double phi = 0;

                public void run() {
                    Location loc = p.getLocation();

                    phi += Math.PI / 10;
                    for (double theta = 0; theta <= 2 * Math.PI; theta += Math.PI / 60) {
                        double r = 1.5;
                        double x = r * Math.cos(theta) * Math.sin(phi);
                        double y = r * Math.cos(phi) + 1.5;
                        double z = r * Math.sin(theta) * Math.sin(phi);
                        loc.add(x, y, z);
                        p.spawnParticle(Particle.valueOf(PM.regParticle), loc, 0, 0, 0, 0, 1);
                        loc.subtract(x, y, z);
                    }

                    if (phi > 3 * Math.PI) {
                        this.cancel();
                    }

                }
            }.runTaskTimer(PM, 0, 1);
        }

        if (!(p.hasPermission("pm.bypass") || p.hasPermission("pm.admin"))) {
            misc.addToCooldown(sender);
        }
    }

    public void mentionEveryone(Player sender, Player p) {
        if (misc.handleHooks(p, sender))
            return;

        OnMentionEvent event = new OnMentionEvent(sender, p);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled())
            return;

        if (PM.everyoneMessageEnabled) {
            p.sendMessage(PM.convertPlaceholders(p.getPlayer(), PM.everyoneMessage.replaceAll("%player%", p.getName()).replaceAll("%nick%", p.getDisplayName())));
        }

        try {
            if (EnumUtils.isValidEnum(Sound.class, PM.everyoneSound)) {
                p.playSound(p.getLocation(), Sound.valueOf(PM.everyoneSound), 100.0F, 1.0F);
            } else {
                p.playSound(p.getLocation(), Sounds.valueOf(PM.everyoneSound).bukkitSound(), 100.0F, 1.0F);
            }
        } catch (Exception e) {
            System.err.println(PM.consolePrefix + ChatColor.RED + "That is not a valid sound (Error at 'Everyone.sound')");
        }

        if (PM.everyoneTitlesEnabled) {
            TitleAPI.sendTitle(p, 0, 5*20, 1*20, PM.everyoneTitle.replaceAll("%player%", p.getName()).replaceAll("%nick%", p.getDisplayName()), PM.everyoneSubtitle.replaceAll("%player%", p.getName()).replaceAll("%nick%", p.getDisplayName()));
        }

        if (PM.everyoneActionbarEnabled) {
            bar.sendActionBar(p, PM.everyoneActionbar.replaceAll("%player%", p.getName()).replaceAll("%nick%", p.getDisplayName()));
        }

        if (PM.everyoneParticlesEnabled) {
            new BukkitRunnable() {
                double phi = 0;

                public void run() {
                    Location loc = p.getLocation();

                    phi += Math.PI / 10;
                    for (double theta = 0; theta <= 2 * Math.PI; theta += Math.PI / 60) {
                        double r = 1.5;
                        double x = r * Math.cos(theta) * Math.sin(phi);
                        double y = r * Math.cos(phi) + 1.5;
                        double z = r * Math.sin(theta) * Math.sin(phi);
                        loc.add(x, y, z);
                        p.spawnParticle(Particle.valueOf(PM.everyoneParticle), loc, 0, 0, 0, 0, 1);
                        loc.subtract(x, y, z);
                    }

                    if (phi > 3 * Math.PI) {
                        this.cancel();
                    }

                }
            }.runTaskTimer(PM, 0, 1);
        }

        if (!(p.hasPermission("pm.bypass") || p.hasPermission("pm.admin"))) {
            misc.addToCooldown(sender);
        }
    }
}