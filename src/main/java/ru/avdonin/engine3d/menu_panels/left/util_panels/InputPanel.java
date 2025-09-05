package ru.avdonin.engine3d.menu_panels.left.util_panels;

public interface InputPanel<T> {
    T getValue(String name);
    void clear();
}
