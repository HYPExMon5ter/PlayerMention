package net.hypexmon5ter.pm.methods;

import net.hypexmon5ter.pm.PlayerMention;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class UpdateChecker {

    private PlayerMention PM;

    public UpdateChecker(PlayerMention PM) {
        this.PM = PM;
    }

    public boolean needsUpdate() {
        String resourceID = "8963";
        String pluginVer = PM.getDescription().getVersion();
        if (version(resourceID).equals(pluginVer)) {
            return false;
        } else {
            return true;
        }
        //return version(resourceID).equals(pluginVer);
    }

    public String version(String resource) {
        try {
            HttpURLConnection con = (HttpURLConnection)new URL("http://www.spigotmc.org/api/general.php").openConnection();
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.getOutputStream().write(("key=98BE0FE67F88AB82B4C197FAF1DC3B69206EFDCC4D3B80FC83A00037510B99B4&resource=" + resource).getBytes("UTF-8"));
            String version = new BufferedReader(new InputStreamReader(con.getInputStream())).readLine();
            if (version.length() <= 7)
                return version;
        } catch (Exception ex) {
            return PM.prefix + PM.parseColors("&cFailed to check for an update..");
        }
        return PM.prefix + PM.parseColors("&cError.. nothing happened when checking for an update.");
    }
}