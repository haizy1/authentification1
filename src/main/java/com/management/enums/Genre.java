package com.management.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Genre {
    HOMME,
    FEMME;

    @JsonCreator
    public static Genre fromString(String value) {
        return value == null ? null : Genre.valueOf(value.toUpperCase());
    }

    @JsonValue
    public String toJson() {
        return name().toLowerCase(); // Optional: Control how it's serialized
    }
}
