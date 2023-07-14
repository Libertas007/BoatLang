package com.libertas.boatlang.parser;

import com.libertas.boatlang.errors.BoatError;
import com.libertas.boatlang.errors.ErrorLog;
import com.libertas.boatlang.errors.ErrorType;
import com.libertas.boatlang.functions.BoatFunction;
import com.libertas.boatlang.functions.BoatFunctionArgumentValue;
import com.libertas.boatlang.generics.Region;
import com.libertas.boatlang.variables.None;
import com.libertas.boatlang.variables.Variable;

import java.util.*;

public class Context {
    private HashMap<String, Variable> variables;
    private HashMap<String, BoatFunction> functions;
    private HashMap<String, Variable> types;
    private List<String> exports;

    public Context() {
        variables = new HashMap<>();
        functions = new HashMap<>();
        types = new HashMap<>();
        exports = new ArrayList<>();
    }

    public Context(Context parent) {
        variables = parent.variables;
        functions = parent.functions;
        types = parent.types;
        exports = parent.exports;
    }

    public Context(HashMap<String, Variable> variables, HashMap<String, BoatFunction> functions, HashMap<String, Variable> types) {
        this.variables = variables;
        this.functions = functions;
        this.types = types;
        exports = new ArrayList<>();
    }

    public HashMap<String, BoatFunction> getFunctions() {
        return functions;
    }

    public HashMap<String, Variable> getVariables() {
        return variables;
    }

    public HashMap<String, Variable> getTypes() {
        return types;
    }

    public void loadTypes(HashMap<String, Variable> types) {
        this.types.putAll(types);
    }

    public void loadContext(Context context) {
        variables.putAll(context.exportVariables());
        functions.putAll(context.exportFunctions());
        types.putAll(context.types);
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

    public Variable getDefaultValue(String type, Region region) {
        if (!types.containsKey(type)) {
            ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "UnknownType", "Type '" + type + "' has not been defined in current context.", region), true);
            return new None();
        }
        return types.get(type).implementRequest(this, region);
    }

    public boolean existsType(String name) {
        return types.containsKey(name);
    }

    public void registerType(String name, Variable value) {
        types.put(name, value);
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
