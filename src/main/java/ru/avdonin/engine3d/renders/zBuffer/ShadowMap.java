package ru.avdonin.engine3d.renders.zBuffer;

import lombok.Getter;

@Getter
public class ShadowMap extends ZBuffer {
    private final int resolution;

    public ShadowMap(int resolution) {
        this.resolution = resolution;
        this.maxHeight = resolution;
        this.maxWidth = resolution;
        initZBuffer();
    }
}