package com.libertas.boatlang.variables;

public class None extends Variable {

    public None() {
        super(null, "NONE");
    }

    @Override
    public String represent() {
        return "NONE";
    }
}
