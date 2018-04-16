package net.hypexmon5ter.pm.methods;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import net.hypexmon5ter.pm.PlayerMention;
import org.bukkit.Bukkit;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.function.Consumer;

public class UpdateChecker {

    public static void check(Consumer<String> success, Consumer<Throwable> fail) {
        Bukkit.getServer().getScheduler().runTaskAsynchronously(PlayerMention.getInstance(), () -> {
            try {
                HttpURLConnection con = (HttpURLConnection) new URL("https://api.spiget.org/v2/resources/8963/versions/latest").openConnection();
                con.setRequestMethod("GET");
                con.connect();
                InputStream is = con.getInputStream();
                byte[] buffer = new byte[is.available()];
                is.read(buffer);
                String content = new String(buffer);
                JsonObject jsonObject = new JsonParser().parse(content).getAsJsonObject();
                if (!(PlayerMention.getInstance().getDescription().getVersion().equals(jsonObject.get("name").getAsString()))) {
                    success.accept(jsonObject.get("name").getAsString());
                    PlayerMention.getInstance().needsUpdate = true;
                } else {
                    PlayerMention.getInstance().needsUpdate = false;
                }
            } catch (IOException | JsonSyntaxException e) {
                fail.accept(e);
                PlayerMention.getInstance().needsUpdate = false;
            }
        });
    }
}