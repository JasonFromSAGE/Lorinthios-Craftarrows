package me.lorinth.craftarrows.Util;

import org.bukkit.Bukkit;

import java.util.HashMap;

public class NmsHelper {

    private static HashMap<String, Class<?>> nmsReflectionCache = new HashMap<>();
    private static HashMap<String, Class<?>> obReflectionCache = new HashMap<>();
    private static String version;

    public static Class<?> getNMSClass(String name) {
        if(nmsReflectionCache.containsKey(name))
            return nmsReflectionCache.get(name);

        try {
            Class<?> requested = Class.forName("net.minecraft.server." + getVersion() + "." + name);
            nmsReflectionCache.put(name, requested);
            return requested;
        } catch (ClassNotFoundException var3) {
            var3.printStackTrace();
            return null;
        }
    }

    public static Class<?> getOBClass(String name) {
        if(obReflectionCache.containsKey(name))
            return obReflectionCache.get(name);

        try {
            Class<?> requested =  Class.forName("org.bukkit.craftbukkit." + getVersion() + "." + name);
            obReflectionCache.put(name, requested);
            return requested;
        } catch (ClassNotFoundException var3) {
            var3.printStackTrace();
            return null;
        }
    }

    public static String getVersion(){
        if(version == null)
            version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

        return version;
    }

    public static int getSimpleVersion(){
        return Integer.valueOf(getVersion().split("_")[1]);
    }

}
