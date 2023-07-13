package com.libertas.boatlang.variables;

import com.libertas.boatlang.errors.BoatError;
import com.libertas.boatlang.errors.ErrorLog;
import com.libertas.boatlang.errors.ErrorType;
import com.libertas.boatlang.functions.BoatFunctionArgumentValue;
import com.libertas.boatlang.functions.Method;
import com.libertas.boatlang.generics.Region;
import com.libertas.boatlang.generics.RunConfiguration;
import com.libertas.boatlang.generics.RunMode;
import com.libertas.boatlang.libraries.std.StandardLibrary;
import com.libertas.boatlang.parser.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class Variable {
    public String displayName;
    public Object value;
    public boolean originatesFromInput = false;
    protected List<Implementation> implementations;
    protected HashMap<String, Method> methods;
    protected HashMap<String, Variable> properties;

    public Variable(Object value, String displayName) {
        this.value = value;
        this.displayName = displayName;
        implementations = new ArrayList<>();
        methods = new HashMap<>();
        properties = new HashMap<>();
    }

    public Variable get(Context context) {
        return this;
    }

    public String represent() {
        return "";
    }

    public void setMethod(String name, Method function) {
        methods.put(name, function);
    }

    public Variable runMethod(String name, List<BoatFunctionArgumentValue> arguments, Region region, Context context) {
        if (name.equals("")) return this;

        if (!methods.containsKey(name)) {
            ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "UnknownMethod", "Method '" + name + "' does not exist on type " + displayName + ".", region), true);
        }

        return methods.get(name).run(context, this, arguments, region).result;
    }

    public void setProperty(String name, Variable value) {
        properties.put(name, value);
    }

    public Variable getProperty(String name) {
        return properties.get(name);
    }

    public void addImplementation(Implementation implementation) {
        implementations.add(implementation);
    }

    public Variable implementAdd(BoatFunctionArgumentValue first, BoatFunctionArgumentValue second, Context context, Region region) {
        String firstDisplayName = first.value().get(context).displayName;
        String secondDisplayName = second.value().get(context).displayName;

        List<Implementation> matching = second.value().implementations.stream().filter(implementation -> implementation.matches("ADD", List.of(firstDisplayName, secondDisplayName))).toList();

        if (!matching.isEmpty()) {
            return matching.get(0).run(context, List.of(first, second), region);
        }

        ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "MethodNotImplemented", "The method ADD is not implemented for " + firstDisplayName + " " + secondDisplayName + ".", region), true);
        return new None();
    }

    public Variable implementDrop(BoatFunctionArgumentValue first, Context context, Region region) {
        String firstDisplayName = first.value().get(context).displayName;

        List<Implementation> matching = first.value().implementations.stream().filter(implementation -> implementation.matches("DROP", List.of(firstDisplayName))).toList();

        if (!matching.isEmpty()) {
            return matching.get(0).run(context, List.of(first), region);
        }

        // ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "MethodNotImplemented", "The method DROP is not implemented!", new Region(0, 0, 0, 0)), true);
        if (!(first.value() instanceof VariableReference)) {
            ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidFunctionSignature", "The first argument must be an identifier.", first.region()), true);
            return new None();
        }

        context.dropVariable(((VariableReference) first.value()).name);
        return new None();
    }

    public Variable implementDivide(BoatFunctionArgumentValue first, BoatFunctionArgumentValue second, Context context, Region region) {
        String firstDisplayName = first.value().get(context).displayName;
        String secondDisplayName = second.value().get(context).displayName;

        List<Implementation> matching = first.value().implementations.stream().filter(implementation -> implementation.matches("DIVIDE", List.of(firstDisplayName, secondDisplayName))).toList();

        if (!matching.isEmpty()) {
            return matching.get(0).run(context, List.of(first, second), region);
        }

        ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "MethodNotImplemented", "The method DIVIDE is not implemented for " + firstDisplayName + " " + secondDisplayName + ".", region), true);
        return new None();
    }

    public Variable implementMultiply(BoatFunctionArgumentValue first, BoatFunctionArgumentValue second, Context context, Region region) {
        String firstDisplayName = first.value().get(context).displayName;
        String secondDisplayName = second.value().get(context).displayName;

        List<Implementation> matching = first.value().implementations.stream().filter(implementation -> implementation.matches("MULTIPLY", List.of(firstDisplayName, secondDisplayName))).toList();

        if (!matching.isEmpty()) {
            return matching.get(0).run(context, List.of(first, second), region);
        }

        ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "MethodNotImplemented", "The method MULTIPLY is not implemented for " + firstDisplayName + " " + secondDisplayName + ".", region), true);
        return new None();
    }

    public Variable implementRepack(BoatFunctionArgumentValue first, BoatFunctionArgumentValue target, Context context, Region region) {
        String firstDisplayName = first.value().get(context).displayName;

        if (!(target.value() instanceof VariableReference)) {
            ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidFunctionSignature", "The first argument must be a type name.", target.region()), true);
            return new None();
        }

        String secondDisplayName = ((VariableReference) target.value()).name;

        List<Implementation> matching = first.value().implementations.stream().filter(implementation -> implementation.matches("REPACK", List.of(firstDisplayName, secondDisplayName))).toList();

        if (!matching.isEmpty()) {
            if ((RunConfiguration.getInstance().mode == RunMode.ANALYZE || RunConfiguration.getInstance().mode == RunMode.IMPORT) && first.value().get(context).originatesFromInput) {
                if (RunConfiguration.getInstance().mode == RunMode.ANALYZE) {
                    ErrorLog.getInstance().registerError(new BoatError(ErrorType.WARNING, "PossibleError", "The variable originates from input, repacking may not be possible.", first.region()), true);
                }

                StandardLibrary std = new StandardLibrary();
                Variable newValue = std.defaultValue(secondDisplayName, context, region);
                newValue.originatesFromInput = true;
                context.setVariable(((VariableReference) first.value()).name, newValue);
                return newValue;
            }

            return matching.get(0).run(context, List.of(first, target), region);
        }

        ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "MethodNotImplemented", "The method REPACK is not implemented for " + firstDisplayName + " " + secondDisplayName + ".", region), true);
        return new None();
    }

    public Variable implementRequest(Context context, Region region) {
        List<Implementation> matching = implementations.stream().filter(implementation -> implementation.matches("REQUEST", new ArrayList<>())).toList();

        if (!matching.isEmpty()) {
            return matching.get(0).run(context, new ArrayList<>(), region);
        }

        ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "MethodNotImplemented", "The method REQUEST is not implemented!", region), true);
        return new None();
    }

    public Variable implementReturn(BoatFunctionArgumentValue first, Context context, Region region) {
        String firstDisplayName = first.value().get(context).displayName;

        List<Implementation> matching = first.value().implementations.stream().filter(implementation -> implementation.matches("RETURN", List.of(firstDisplayName))).toList();

        if (!matching.isEmpty()) {
            return matching.get(0).run(context, List.of(first), region);
        }

        // ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "MethodNotImplemented", "The method RETURN is not implemented!", new Region(0, 0, 0, 0)), true);
        return implementDrop(first, context, region);
    }

    public Variable implementSubtract(BoatFunctionArgumentValue first, BoatFunctionArgumentValue second, Context context, Region region) {
        String firstDisplayName = first.value().get(context).displayName;
        String secondDisplayName = second.value().get(context).displayName;

        List<Implementation> matching = second.value().implementations.stream().filter(implementation -> implementation.matches("SUBTRACT", List.of(firstDisplayName, secondDisplayName))).toList();

        if (!matching.isEmpty()) {
            return matching.get(0).run(context, List.of(first, second), region);
        }

        ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "MethodNotImplemented", "The method SUBTRACT is not implemented for " + firstDisplayName + " " + secondDisplayName + ".", region), true);
        return new None();
    }

    @Override
    public String toString() {
        return displayName + "{" + value + "}";
    }
}
