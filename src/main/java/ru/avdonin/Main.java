package ru.avdonin;

import ru.avdonin.console.GameConsole;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(GameConsole::new);
    }
}