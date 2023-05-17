package com.libertas.variables;

import com.libertas.functions.BoatFunction;
import com.libertas.functions.BoatFunctionArgumentValue;
import com.libertas.generics.Region;
import com.libertas.parser.Context;

import java.util.Arrays;
import java.util.List;

public class Implementation {
    public List<String> types;
    public BoatFunction function;
    public String forMethod;

    public Implementation(String forMethod, List<String> types, BoatFunction function) {
        this.forMethod = forMethod;
        this.types = types;
        this.function = function;
    }

    public boolean matches(String method, List<String> types) {
        if (!method.equals(forMethod)) return false;

        if (types.size() != this.types.size()) return false;

        for (int i = 0; i < types.size(); i++) {
            String type = types.get(i);
            if (this.types.get(i).equals("ANY")) continue;

            if (!type.equals(this.types.get(i))) return false;
        }

        return true;
    }

    public Variable run(Context context, List<BoatFunctionArgumentValue> values, Region region) {
        return function.run(context, values, region).result;
    }
}
