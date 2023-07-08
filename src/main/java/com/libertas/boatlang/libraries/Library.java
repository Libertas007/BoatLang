package com.libertas.boatlang.libraries;

import com.libertas.boatlang.functions.BoatFunction;
import com.libertas.boatlang.generics.Region;
import com.libertas.boatlang.parser.Context;
import com.libertas.boatlang.variables.None;
import com.libertas.boatlang.variables.Variable;

import java.util.HashMap;

public class Library extends Variable {

    public Library() {
        super(new None(), "LIBRARY");
    }

    public Context asContext() {

        HashMap<String, BoatFunction> functions = new HashMap<>(methods);

        Context context = new Context(properties, functions);

        properties.forEach((s, variable) -> context.makeExportable(s, new Region()));
        methods.forEach((s, function) -> context.makeExportable(s, new Region()));

        return context;
    }
}
