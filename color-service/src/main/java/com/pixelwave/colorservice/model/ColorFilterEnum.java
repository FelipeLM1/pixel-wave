package com.pixelwave.colorservice.model;

import net.coobird.thumbnailator.filters.Colorize;
import net.coobird.thumbnailator.filters.ImageFilter;

import java.awt.*;


public enum ColorFilterEnum {
    BLUE("Blue No Opacity", new Colorize(new Color(0, 0, 255,127))),
    RED("Red No Opacity", new Colorize(new Color(255, 0, 0,127))),
    GREEN("Green No Opacity", new Colorize(new Color(0, 255, 0,127))),
    GRAYSCALE("Grayscale", new Colorize(new Color(0, 0, 0), 127));

    private final String name;
    private final ImageFilter filter;

    ColorFilterEnum(String name, ImageFilter filter) {
        this.name = name;
        this.filter = filter;
    }

    public String getName() {
        return name;
    }

    public ImageFilter getFilter() {
        return filter;
    }

    public static ColorFilterEnum fromString(String filterName) {
        for (ColorFilterEnum filterEnum : ColorFilterEnum.values()) {
            if (filterEnum.name.equalsIgnoreCase(filterName)) {
                return filterEnum;
            }
        }
        throw new IllegalArgumentException("Nenhum filtro encontrado para o nome: " + filterName);
    }

}
