package net.hypexmon5ter.pm.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;

public class ActionbarAPI {

    public static Class<?> getNmsClass(String nmsClassName)
            throws ClassNotFoundException {
        return Class.forName("net.minecraft.server." + Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3] + "." + nmsClassName);
    }

    public static String getServerVersion() {
        return Bukkit.getServer().getClass().getPackage().getName().substring(23);
    }

    public static void sendActionBar(Player p, String msg) {
        try {
            if ((getServerVersion().contains("v1_12_"))) {
                Object icbc = getNmsClass("ChatComponentText").getConstructor(new Class[]{String.class}).newInstance(new Object[]{ChatColor.translateAlternateColorCodes('&', msg)});
                Object cmt = getNmsClass("ChatMessageType").getField("GAME_INFO").get(null);

                Object ppoc = getNmsClass("PacketPlayOutChat").getConstructor(new Class[]{getNmsClass("IChatBaseComponent"), getNmsClass("ChatMessageType")}).newInstance(new Object[]{icbc, cmt});

                Object nmsp = p.getClass().getMethod("getHandle", new Class[0]).invoke(p, new Object[0]);

                Object pcon = nmsp.getClass().getField("playerConnection").get(nmsp);

                pcon.getClass().getMethod("sendPacket", new Class[]{getNmsClass("Packet")}).invoke(pcon, new Object[]{ppoc});
            } else if (getServerVersion().contains("v1_9_") || getServerVersion().contains("v1_10_") || getServerVersion().contains("v1_11_")) {
                Object icbc = getNmsClass("ChatComponentText").getConstructor(new Class[]{String.class}).newInstance(new Object[]{ChatColor.translateAlternateColorCodes('&', msg)});

                Object ppoc = getNmsClass("PacketPlayOutChat").getConstructor(new Class[]{getNmsClass("IChatBaseComponent"), Byte.TYPE}).newInstance(new Object[]{icbc, 2});

                Object nmsp = p.getClass().getMethod("getHandle", new Class[0]).invoke(p, new Object[0]);

                Object pcon = nmsp.getClass().getField("playerConnection").get(nmsp);

                pcon.getClass().getMethod("sendPacket", new Class[]{getNmsClass("Packet")}).invoke(pcon, new Object[]{ppoc});
            }
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | ClassNotFoundException | InstantiationException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
}