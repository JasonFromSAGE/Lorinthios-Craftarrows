package me.lorinth.craftarrows.Objects;

import org.bukkit.configuration.file.FileConfiguration;

public class ConfigValue<T> {

    private String path;
    private T value;

    public ConfigValue(String path, T defaultValue){
        this.path = path;
        this.value = defaultValue;
    }

    public T getValue(FileConfiguration config){
        try{
            T value = (T) config.get(path);
            return value;
        }
        catch(Exception exception){
            return this.value;
        }
    }

    public String getPath(){
        return path;
    }

    public void setPath(String path){
        this.path = path;
    }

    public void setDefault(FileConfiguration config){
        config.set(path, value);
    }

}
