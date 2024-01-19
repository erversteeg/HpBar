package com.ericversteeg.frosthprun;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FontWeight {
    BOLD("Bold"),
    BLACK("Black");

    private final String name;
};


