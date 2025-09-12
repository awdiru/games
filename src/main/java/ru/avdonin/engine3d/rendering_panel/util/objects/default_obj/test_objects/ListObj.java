package ru.avdonin.engine3d.rendering_panel.util.objects.default_obj.test_objects;

import lombok.Getter;
import ru.avdonin.engine3d.rendering_panel.util.Creatable;
import ru.avdonin.engine3d.rendering_panel.util.objects.*;
import ru.avdonin.engine3d.rendering_panel.util.objects.default_obj.test_objects.obj.Cube;
import ru.avdonin.engine3d.rendering_panel.util.objects.default_obj.test_objects.obj.House;
import ru.avdonin.engine3d.rendering_panel.util.objects.default_obj.test_objects.obj.Plane;

@Getter
public enum ListObj {
    POINT("Point", Point3D.class),
    EDGE("Edge", Edge3D.class),
    VECTOR("Vector", Vector3D.class),
    POLYGON("Polygon", Polygon3D.class),
    LIGHT("Light", Light3D.class),
    PLANE("Plane", Plane.class),
    CUBE("Cube", Cube.class),
    HOUSE("House", House.class);

    private final String name;
    private final Class<? extends Creatable> aClass;

    ListObj(String name, Class<? extends Creatable> aClass) {
        this.name = name;
        this.aClass = aClass;
    }

    public Creatable newInstance() {
        return Creatable.newInstance(aClass.getName());
    }

}
