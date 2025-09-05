package ru.avdonin.engine3d.menu_panels.left.util_panels;

public interface InputPanel<T> {
    /**
     * Вернуть значение из строки
     * @param name название строки
     * @return значение строки
     */
    T getValue(String name);

    /**
     * Очистить панель ввода
     */
    void clear();
}
