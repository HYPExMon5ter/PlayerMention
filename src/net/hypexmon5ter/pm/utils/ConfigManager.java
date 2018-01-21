package net.hypexmon5ter.pm.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

public class ConfigManager {

	private File file;
	private FileConfiguration config;
	
	private String path;
	private String fileName;
	
	private Plugin main;
	
	public ConfigManager(String path, String fileName, Plugin JavaPluginExtender) {
		main = JavaPluginExtender;

		this.path = path;
		this.fileName = fileName;

	}

	public void create(Boolean dflt) {
		file = new File(path, fileName);
        if (!(file.exists())) {
            if (dflt) {
                main.saveResource(fileName, false);
                config = YamlConfiguration.loadConfiguration(file);
            } else {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    System.err.println("Couldn't create the " + fileName);
                }
            }
        }
        config = YamlConfiguration.loadConfiguration(file);
	}

	public void saveConfig() {
		try {
			config.save(file);
		} catch(IOException e) {
			System.err.println("Couldn't save the " + fileName);
		}
	}

	public void reloadConfig() {
		try {
			config = YamlConfiguration.loadConfiguration(file);
		} catch(Exception e) {
			System.err.println("Couldn't reload " + fileName);
		}
	}

	public FileConfiguration getConfig() {
		return config;
	}
}