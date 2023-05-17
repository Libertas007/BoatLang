package com.libertas.libraries;

import com.libertas.functions.BoatFunction;
import com.libertas.generics.Region;
import com.libertas.parser.Context;
import com.libertas.variables.Variable;

import java.util.HashMap;

public class Library {
    public HashMap<String, Variable> variables;
    public HashMap<String, BoatFunction> functions;

    public Library() {
        variables = new HashMap<>();
        functions = new HashMap<>();
    }

    public Context asContext() {
        Context context = new Context(variables, functions);

        variables.forEach((s, variable) -> context.makeExportable(s, new Region()));
        functions.forEach((s, function) -> context.makeExportable(s, new Region()));

        return context;
    }
}
