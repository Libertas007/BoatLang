package com.libertas.boatlang.errors;

import com.libertas.boatlang.generics.Region;

public class BoatError {
    public String message;
    public String name;

    public Region region;
    public ErrorType type;

    public BoatError(ErrorType type, String name, String message, Region region) {
        this.message = message;
        this.name = name;
        this.region = region;
        this.type = type;
    }
}

