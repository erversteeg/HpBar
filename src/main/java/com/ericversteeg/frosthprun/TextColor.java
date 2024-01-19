package com.ericversteeg.frosthprun;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.awt.*;

@Getter
@RequiredArgsConstructor
public enum TextColor {
    WHITE("White", Color.WHITE),
    YELLOW("Yellow", Color.YELLOW),
    RED("Red", Color.RED),
    GREEN("Green", Color.GREEN),
    BLUE("Blue", Color.BLUE),
    CYAN("Cyan", Color.CYAN),
    MAGENTA("Magenta", Color.MAGENTA),
    PINK("Pink", Color.PINK);

    private final String name;
    private final Color color;
};


