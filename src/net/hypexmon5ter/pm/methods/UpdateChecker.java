package net.hypexmon5ter.pm.methods;

import com.google.gson.*;
import net.hypexmon5ter.pm.PlayerMention;
import org.bukkit.Bukkit;
import org.json.simple.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

public class UpdateChecker {

    private PlayerMention PM;

    public UpdateChecker(PlayerMention PM) {
        this.PM = PM;
    }



    public boolean needsUpdate() {
        String pluginVer = PM.getDescription().getVersion();
        return !version("8963").equals(pluginVer);
    }

    public String version(String resource) {
        try {
            HttpURLConnection con = (HttpURLConnection)new URL("https://api.spiget.org/v2/resources/8963/versions/latest").openConnection();
            con.setRequestMethod("GET");
            con.connect();
            InputStream is = con.getInputStream();
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            String content = new String(buffer);
            JsonObject jsonObject = new JsonParser().parse(content).getAsJsonObject();
            return jsonObject.get("name").getAsString();
        } catch (Exception ex) {
            Bukkit.getLogger().severe(PM.prefix + PM.parseColors("&cFailed to check for an update.."));
            return PM.getDescription().getVersion();
        }
    }
}