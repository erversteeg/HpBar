package com.ericversteeg.frosthprun;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FontWeight {
    EXTRA_LIGHT("Extra Light"),
    LIGHT("Light"),
    THIN("Thin"),
    REGULAR("Regular"),
    MEDIUM("Medium"),
    SEMI_BOLD("Semi Bold"),
    BOLD("Bold"),
    EXTRA_BOLD("Extra Bold"),
    BLACK("Black");

    private final String name;
};


