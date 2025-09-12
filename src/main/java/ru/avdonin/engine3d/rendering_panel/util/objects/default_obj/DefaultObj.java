package ru.avdonin.engine3d.rendering_panel.util.objects.default_obj;

import ru.avdonin.engine3d.rendering_panel.util.AbstractObject3D;

public interface DefaultObj <T extends AbstractObject3D<?>> {
    T createObject();
}
