package ru.avdonin.engine3d.menu_panels;

import ru.avdonin.engine3d.Constants;
import ru.avdonin.engine3d.Context;
import ru.avdonin.engine3d.rendering_panel.util.Creatable;
import ru.avdonin.engine3d.rendering_panel.util.objects.default_obj.surface.Surface;
import ru.avdonin.engine3d.rendering_panel.util.objects.default_obj.test_objects.ListObj;
import ru.avdonin.engine3d.saver.Saver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;

public class UpPanel extends JPanel {
    public UpPanel() {
        setLayout(new FlowLayout());
        init();
    }

    private void init() {
        add(createSaveButton());
        add(createObjectButton());
        add(createSurfaceButton());
        add(createFlagsMenuButton());
        add(createClearContextButton());
    }

    private JButton createSaveButton() {
        JButton button = new JButton("save");
        button.addActionListener(e -> Saver.saveScene("save", "scene"));
        return button;
    }

    private JButton createObjectButton() {
        JButton button = new JButton("create object");
        button.addActionListener(e -> showObjContextMenu(button));
        return button;
    }

    private JButton createSurfaceButton() {
        JButton button = new JButton("create surface");
        button.addActionListener(e -> showSurfaceContextMenu(button));
        return button;
    }

    private JButton createFlagsMenuButton() {
        JButton button = new JButton("settings");
        button.addActionListener(e -> showFlagsContextMenu(button));
        return button;
    }

    private JButton createClearContextButton() {
        JButton button = new JButton("clear");
        button.addActionListener(e -> Context.clear());
        return button;
    }

    private void showObjContextMenu(JButton button) {
        JPopupMenu menu = new JPopupMenu();

        for (ListObj obj : ListObj.values()) {
            JMenuItem item = new JMenuItem(obj.getName());
            item.addActionListener(e -> {
                Creatable o = obj.newInstance();
                o.openCreateFrame();
            });
            menu.add(item);
        }
        menu.show(button, 0, button.getHeight());
    }

    private void showSurfaceContextMenu(JButton button) {
        JPopupMenu menu = new JPopupMenu();

        for (Surface obj : Surface.values()) {
            JMenuItem item = new JMenuItem(obj.getName());
            item.addActionListener(e -> {
                Creatable o = obj.newInstance();
                o.openCreateFrame();
            });
            menu.add(item);
        }
        menu.show(button, 0, button.getHeight());
    }

    private void showFlagsContextMenu(JButton button) {
        JPopupMenu menu = new JPopupMenu();
        menu.add(setFlagCheckBox("noise filter", Constants.NOISE_FILTER));
        menu.add(setFlagCheckBox("render lights", Constants.RENDERING_LIGHTS_OBJ));
        menu.add(setFlagCheckBox("draw the height of the light", Constants.DRAW_HEIGHT_LIGHT));
        menu.show(button, 0, button.getHeight());
    }

    private JCheckBox setFlagCheckBox(String name, String contextKey) {
        Boolean isSelected = Context.get(contextKey);
        JCheckBox checkBox = new JCheckBox(name, isSelected);
        checkBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                Context.put(contextKey, Boolean.TRUE);
            } else {
                Context.put(contextKey, Boolean.FALSE);
            }
        });
        return checkBox;
    }


}
