package com.libertas.variables;

import com.libertas.errors.BoatError;
import com.libertas.errors.ErrorLog;
import com.libertas.errors.ErrorType;
import com.libertas.generics.Region;
import com.libertas.parser.Context;

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
