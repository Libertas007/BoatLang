package com.libertas.boatlang.libraries;

import com.libertas.boatlang.errors.BoatError;
import com.libertas.boatlang.errors.ErrorLog;
import com.libertas.boatlang.errors.ErrorType;
import com.libertas.boatlang.functions.BoatFunction;
import com.libertas.boatlang.functions.BoatFunctionArgumentValue;
import com.libertas.boatlang.generics.Region;
import com.libertas.boatlang.parser.Context;
import com.libertas.boatlang.variables.None;
import com.libertas.boatlang.variables.Variable;

import java.util.HashMap;
import java.util.List;

public class Library extends Variable {
    protected HashMap<String, Variable> types;
    protected HashMap<String, BoatFunction> functions;


    public Library() {
        super(new None(), "LIBRARY");
        types = new HashMap<>();
        functions = new HashMap<>();
    }

    public Context asContext() {
        Context context = new Context(properties, functions, types);

        properties.forEach((s, variable) -> context.makeExportable(s, new Region()));
        functions.forEach((s, function) -> context.makeExportable(s, new Region()));
        
        return context;
    }

    public void setFunction(String name, BoatFunction function) {
        functions.put(name, function);
    }

    public Variable runFunction(String name, List<BoatFunctionArgumentValue> arguments, Region region, Context context) {
        if (name.isEmpty()) return this;

        if (!functions.containsKey(name)) {
            ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "UnknownFunction", "Function '" + name + "' does not exist on type " + displayName + ".", region), true);
            return new None();
        }

        return functions.get(name).run(context, arguments, region).result;
    }
}
