package utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ActionbarAPI {
    public static boolean works = true;
    public static String nmsver;
    private static boolean useOldMethods = false;

    public ActionbarAPI() {
        nmsver = Bukkit.getServer().getClass().getPackage().getName();
        nmsver = nmsver.substring(nmsver.lastIndexOf(".") + 1);

        if ((nmsver.equalsIgnoreCase("v1_8_R1") && nmsver.startsWith("v1_8_") || nmsver.startsWith("v1_9_")
                || nmsver.startsWith("v1_10_") || nmsver.startsWith("v1_11_"))) {
            useOldMethods = true;
        }
    }

    public void sendActionBar(Player player, String message) {
        if (!player.isOnline()) {
            return;
        }
        if (nmsver.startsWith("v1_12_"))
            sendActionBarPost112(player, message);
        else
            sendActionBarPre112(player, message);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private static void sendActionBarPost112(Player player, String message) {
        if (!player.isOnline()) {
            return;
        }
        try {
            Class craftPlayerClass = Class.forName("org.bukkit.craftbukkit." + nmsver + ".entity.CraftPlayer");
            Object craftPlayer = craftPlayerClass.cast(player);

            Class c4 = Class.forName("net.minecraft.server." + nmsver + ".PacketPlayOutChat");
            Class c5 = Class.forName("net.minecraft.server." + nmsver + ".Packet");
            Class c2 = Class.forName("net.minecraft.server." + nmsver + ".ChatComponentText");
            Class c3 = Class.forName("net.minecraft.server." + nmsver + ".IChatBaseComponent");
            Class chatMessageTypeClass = Class.forName("net.minecraft.server." + nmsver + ".ChatMessageType");
            Object[] chatMessageTypes = chatMessageTypeClass.getEnumConstants();
            Object chatMessageType = null;
            for (Object obj : chatMessageTypes) {
                if (obj.toString().equals("GAME_INFO")) {
                    chatMessageType = obj;
                }
            }
            Object o = c2.getConstructor(new Class[] { String.class }).newInstance(new Object[] { message });
            Object ppoc = c4.getConstructor(new Class[] { c3, chatMessageTypeClass })
                    .newInstance(new Object[] { o, chatMessageType });
            Method m1 = craftPlayerClass.getDeclaredMethod("getHandle", new Class[0]);
            Object h = m1.invoke(craftPlayer, new Object[0]);
            Field f1 = h.getClass().getDeclaredField("playerConnection");
            Object pc = f1.get(h);
            Method m5 = pc.getClass().getDeclaredMethod("sendPacket", new Class[] { c5 });
            m5.invoke(pc, new Object[] { ppoc });
        } catch (Exception ex) {
            ex.printStackTrace();
            works = false;
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private static void sendActionBarPre112(Player player, String message) {
        if (!player.isOnline()) {
            return;
        }
        try {
            Class craftPlayerClass = Class.forName("org.bukkit.craftbukkit." + nmsver + ".entity.CraftPlayer");
            Object craftPlayer = craftPlayerClass.cast(player);

            Class c4 = Class.forName("net.minecraft.server." + nmsver + ".PacketPlayOutChat");
            Class c5 = Class.forName("net.minecraft.server." + nmsver + ".Packet");
            Object ppoc;
            if (useOldMethods) {
                Class c2 = Class.forName("net.minecraft.server." + nmsver + ".ChatSerializer");
                Class c3 = Class.forName("net.minecraft.server." + nmsver + ".IChatBaseComponent");
                Method m3 = c2.getDeclaredMethod("a", new Class[] { String.class });
                Object cbc = c3.cast(m3.invoke(c2, new Object[] { "{\"text\": \"" + message + "\"}" }));
                ppoc = c4.getConstructor(new Class[] { c3, Byte.TYPE })
                        .newInstance(new Object[] { cbc, Byte.valueOf((byte) 2) });
            } else {
                Class c2 = Class.forName("net.minecraft.server." + nmsver + ".ChatComponentText");
                Class c3 = Class.forName("net.minecraft.server." + nmsver + ".IChatBaseComponent");
                Object o = c2.getConstructor(new Class[] { String.class }).newInstance(new Object[] { message });
                ppoc = c4.getConstructor(new Class[] { c3, Byte.TYPE })
                        .newInstance(new Object[] { o, Byte.valueOf((byte) 2) });
            }
            Method m1 = craftPlayerClass.getDeclaredMethod("getHandle", new Class[0]);
            Object h = m1.invoke(craftPlayer, new Object[0]);
            Field f1 = h.getClass().getDeclaredField("playerConnection");
            Object pc = f1.get(h);
            Method m5 = pc.getClass().getDeclaredMethod("sendPacket", new Class[] { c5 });
            m5.invoke(pc, new Object[] { ppoc });
        } catch (Exception ex) {
            ex.printStackTrace();
            works = false;
        }
    }
}