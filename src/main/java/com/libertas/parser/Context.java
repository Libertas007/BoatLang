package com.libertas.parser;

import com.libertas.errors.BoatError;
import com.libertas.errors.ErrorLog;
import com.libertas.errors.ErrorType;
import com.libertas.functions.BoatFunction;
import com.libertas.functions.BoatFunctionArgumentValue;
import com.libertas.generics.Region;
import com.libertas.variables.None;
import com.libertas.variables.Variable;

import java.util.*;

public class Context {
    private HashMap<String, Variable> variables;
    private HashMap<String, BoatFunction> functions;
    private List<String> exports;

    public Context() {
        variables = new HashMap<>();
        functions = new HashMap<>();
        exports = new ArrayList<>();
    }

    public Context(Context parent) {
        variables = parent.variables;
        functions = parent.functions;
        exports = parent.exports;
    }

    public Context(HashMap<String, Variable> variables, HashMap<String, BoatFunction> functions) {
        this.variables = variables;
        this.functions = functions;
        exports = new ArrayList<>();
    }

    public HashMap<String, BoatFunction> getFunctions() {
        return functions;
    }

    public HashMap<String, Variable> getVariables() {
        return variables;
    }

    public void loadContext(Context context) {
        variables.putAll(context.exportVariables());
        functions.putAll(context.exportFunctions());
    }

    public void makeExportable(String name, Region region) {
        if (!variables.containsKey(name) && !functions.containsKey(name)) {
            ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "UnknownIdentifier", "Cannot export variable or function '" + name + "', because it has not been defined in current context.", region), true);
        }
        exports.add(name);
    }

    public HashMap<String, BoatFunction> exportFunctions() {
        HashMap<String, BoatFunction> toExport = new HashMap<>();

        functions.forEach((name, function) -> {
            if (exports.contains(name)) {
                toExport.put(name, function);
            }
        });

        return toExport;
    }

    public HashMap<String, Variable> exportVariables() {
        HashMap<String, Variable> toExport = new HashMap<>();

        variables.forEach((name, variable) -> {
            if (exports.contains(name)) {
                toExport.put(name, variable);
            }
        });

        return toExport;
    }

    public Variable getVariable(String name, Region region) {
        if (!variables.containsKey(name)) {
            ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "UnknownVariable", "Variable '" + name + "' has not been defined in current context.", region), true);
            return new None();
        }
        return variables.get(name);
    }

    public void setVariable(String name, Variable value) {
        variables.put(name, value);
    }

    public void dropVariable(String name) {
        variables.remove(name);
    }

    public BoatFunction getFunction(String name, Region region) {
        if (!variables.containsKey(name)) {
            ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "UnknownFunction", "Function '" + name + "' has not been defined in current context.", region), true);
        }
        return functions.get(name);
    }

    public void setFunction(String name, BoatFunction function) {
        functions.put(name, function);
    }

    public Variable runFunction(String name, List<BoatFunctionArgumentValue> arguments, Region region) {
        if (!functions.containsKey(name)) {
            List<String> parts = new ArrayList<>(Arrays.stream(name.split("\\.")).toList());

            if (!parts.isEmpty()) {
                String first = parts.get(0);
                parts.remove(0);
                StringJoiner joiner = new StringJoiner(".");

                for (String part : parts) {
                    joiner.add(part);
                }

                return getVariable(first, region).runMethod(joiner.toString(), arguments, region, this);
            }

            ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "UnknownFunction", "Function '" + name + "' has not been defined in current context.", region), true);
        }

        return functions.get(name).run(this, arguments, region).result;
    }
}
