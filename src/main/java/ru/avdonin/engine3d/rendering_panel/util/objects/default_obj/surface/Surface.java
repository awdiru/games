package ru.avdonin.engine3d.rendering_panel.util.objects.default_obj.surface;

import lombok.Getter;
import ru.avdonin.engine3d.rendering_panel.util.Creatable;
import ru.avdonin.engine3d.rendering_panel.util.objects.default_obj.surface.obj.Ellipsoid;

import java.util.List;

@Getter
public enum Surface {
    ELLIPSOID ("Ellipsoid", Ellipsoid.class,
            List.of(SurfaceFields.POINT, SurfaceFields.SIZE, SurfaceFields.DX, SurfaceFields.DY, SurfaceFields.DZ));

    private final String name;
    private final Class<? extends Creatable> aClass;
    private final List<SurfaceFields> fields;

    Surface(String name, Class<? extends Creatable> aClass, List<SurfaceFields> fields) {
        this.name = name;
        this.aClass = aClass;
        this.fields = fields;
    }

    public Creatable newInstance() {
        return Creatable.newInstance(aClass.getName());
    }
}
