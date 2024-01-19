package com.ericversteeg.frosthprun;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TextAlignment {
    LEFT("Left"),
    CENTER("Center"),
    RIGHT("Right");

    private final String name;
};


