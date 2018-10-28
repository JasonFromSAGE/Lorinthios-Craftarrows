package me.lorinth.craftarrows.Objects;

public class Setting<T> implements ISetting<T> {

    private T value;
    private String name;
    private String identifier;

    public Setting(String name, String identifier, T value){
        this.name = name;
        this.identifier = identifier;
        this.value = value;
    }

    public String getIdentifier(){
        return identifier;
    }

    public String getName(){
        return name;
    }

    public T getValue(){
        return value;
    }

    public T setValue(T newValue){
        T oldValue = value;
        value = newValue;
        return oldValue;
    }

}
