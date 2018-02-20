package me.lorinth.craftarrows.Util;


import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Arrays;

public class ConfigHelper {

    public static boolean ConfigContainsPath(FileConfiguration config, String path){
        if(path == null || config == null) {
            return false;
        }

        String[] pathParts = path.split("\\.");
        if(pathParts.length == 0){
            return false;
        }

        String keyToSearchFor = pathParts[pathParts.length-1];
        String configSection = makeConfigSection(Arrays.copyOfRange(pathParts, 0, pathParts.length-1));
        if(!configSection.equalsIgnoreCase("")){
            ConfigurationSection section = config.getConfigurationSection(configSection);
            if(section == null)
                return false;

            return section.getKeys(false).contains(keyToSearchFor);
        }
        else
            return config.getKeys(false).contains(keyToSearchFor);
    }

    private static String makeConfigSection(String[] pieces){
        String result = "";

        for(String piece : pieces){
            result += piece + ".";
        }

        if(!result.equalsIgnoreCase(""))
            result = result.substring(0, result.length() - 1);
        return result;
    }

}
