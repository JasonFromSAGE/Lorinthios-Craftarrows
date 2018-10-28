package me.lorinth.craftarrows.Objects;

public interface ISetting<T> {

    String getName();
    String getIdentifier();

    T getValue();
    T setValue(T value);

}
