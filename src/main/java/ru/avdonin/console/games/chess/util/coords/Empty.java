package ru.avdonin.console.games.chess.util.coords;

import ru.avdonin.console.games.chess.util.Constants;

import javax.swing.*;
import java.awt.*;

public class Empty extends JPanel {
    public Empty() {
        setPreferredSize(new Dimension(Constants.CELL_SIZE / 3, Constants.CELL_SIZE / 3));
    }
}
