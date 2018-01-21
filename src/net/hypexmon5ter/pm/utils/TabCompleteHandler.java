package net.hypexmon5ter.pm.utils;

import net.hypexmon5ter.pm.PlayerMention;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TabCompleteHandler implements TabCompleter {

    private PlayerMention PM;

    public TabCompleteHandler(PlayerMention PM) {
        this.PM = PM;
    }

    private String[] validFirstArgs = {"config", "messages", "reload", "everyone", "toggle"};

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("playermention")) {
            if (args.length == 1) {
                ArrayList<String> firstArgs = new ArrayList<>();

                if (!(args[0].equals(""))) {
                    for (String s : Arrays.asList(validFirstArgs)) {
                        if (s.toLowerCase().startsWith(args[0].toLowerCase())) {
                            firstArgs.add(s);
                        }
                    }
                } else {
                    Collections.sort(Arrays.asList(validFirstArgs));
                    return Arrays.asList(validFirstArgs);
                }
                Collections.sort(firstArgs);
                return firstArgs;
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("config")) {
                    String configStrings = PM.getConfig().getKeys(true).toString().replaceAll("\\[", "").replaceAll("]", "");
                    String[] splitConfigStrings = configStrings.split(", ");
                    ArrayList<String> configPaths = new ArrayList<>();


                    if (!(args[1].equals(""))) {
                        for (String s : Arrays.asList(splitConfigStrings)) {
                            if (s.toLowerCase().startsWith(args[1].toLowerCase())) {
                                configPaths.add(s);
                            }
                        }
                    } else {
                        Collections.sort(Arrays.asList(splitConfigStrings));
                        return Arrays.asList(splitConfigStrings);
                    }
                    Collections.sort(configPaths);
                    return configPaths;
                } else if (args[0].equalsIgnoreCase("messages")) {
                    String messageStrings = PM.msgs.getConfig().getKeys(true).toString().replaceAll("\\[", "").replaceAll("]", "");
                    String[] splitMessageStrings = messageStrings.split(", ");
                    ArrayList<String> messagesPaths = new ArrayList<>();


                    if (!(args[1].equals(""))) {
                        for (String s : Arrays.asList(splitMessageStrings)) {
                            if (s.toLowerCase().startsWith(args[1].toLowerCase())) {
                                messagesPaths.add(s);
                            }
                        }
                    } else {
                        Collections.sort(Arrays.asList(splitMessageStrings));
                        return Arrays.asList(splitMessageStrings);
                    }
                    Collections.sort(messagesPaths);
                    return messagesPaths;
                }
            }
        }
        return null;
    }
}
