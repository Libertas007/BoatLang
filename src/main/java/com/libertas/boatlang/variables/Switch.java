package com.libertas.boatlang.variables;

import com.libertas.boatlang.generics.Region;
import com.libertas.boatlang.parser.Context;

public class Switch extends Variable {
    public Boolean value;

    public Switch(Boolean value) {
        super(value, "SWITCH");
        this.value = value;
    }

    @Override
    public Variable implementRequest(Context context, Region region) {
        return new Switch(false);
    }

    @Override
    public String represent() {
        return value ? "YES" : "NO";
    }
}
