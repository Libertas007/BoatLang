package com.libertas.variables;

public class None extends Variable {

    public None() {
        super(null, "NONE");
    }

    @Override
    public String represent() {
        return "NONE";
    }
}
