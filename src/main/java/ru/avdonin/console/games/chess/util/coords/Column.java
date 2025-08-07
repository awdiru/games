package ru.avdonin.console.games.chess.util.coords;

import ru.avdonin.console.games.chess.util.Constants;

import javax.swing.*;
import java.awt.*;

public class Column extends JPanel {
    public Column(char name) {
        setPreferredSize(new Dimension(Constants.CELL_SIZE, Constants.CELL_SIZE / 3));
        add (new JLabel(name + ""));
    }
}
