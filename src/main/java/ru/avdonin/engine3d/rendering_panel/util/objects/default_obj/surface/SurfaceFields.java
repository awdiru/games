package ru.avdonin.engine3d.rendering_panel.util.objects.default_obj.surface;

import lombok.Getter;
import ru.avdonin.engine3d.menu_panels.util_panels.CreatePanel;
import ru.avdonin.engine3d.menu_panels.util_panels.input_panels.PointPane;
import ru.avdonin.engine3d.menu_panels.util_panels.input_panels.SizeField;

@Getter
public enum SurfaceFields {
    POINT(new PointPane(), "point"),
    SIZE(new DoubleField(), "size"),
    DX(new DoubleField(), "dx"),
    DY(new DoubleField(), "dy"),
    DZ(new DoubleField(), "dz"),
    R(new DoubleField(), "R"),
    r(new DoubleField(), "r");

    private final CreatePanel<?> createPanel;
    private final  String name;

    SurfaceFields(CreatePanel<?> createPanel, String name) {
        this.createPanel = createPanel;
        this.name = name;
    }

    private static class DoubleField extends SizeField<Double> {

        @Override
        public Double getValue() {
            String value = getValueText();
            try {
                return Double.parseDouble(value);
            } catch (Exception e) {
                return 0.0;
            }
        }

        @Override
        public Double getInstance() {
            return getValue();
        }
    }
}
