package com.libertas.variables;

import com.libertas.errors.BoatError;
import com.libertas.errors.ErrorLog;
import com.libertas.errors.ErrorType;
import com.libertas.functions.Method;
import com.libertas.functions.NativeFunction;
import com.libertas.generics.Region;
import com.libertas.generics.RunConfiguration;
import com.libertas.generics.RunMode;
import com.libertas.parser.Context;
import org.apache.commons.math3.fraction.Fraction;

import java.util.ArrayList;
import java.util.List;

public class Barrel extends Variable {
    public Fraction value;

    public Barrel(Fraction value) {
        super(value, "BARREL");
        this.value = value;

        implement();
    }

    @Override
    public String represent() {
        return value.toString();
    }

    private void implement() {
        setMethod("DECIMAL", new Method("DECIMAL", ((context, self, arguments, region) ->
                new Package(Double.valueOf(((Fraction) self.get(context).value).doubleValue()).toString())
        )));

        addImplementation(new Implementation("REQUEST", new ArrayList<>(), new NativeFunction("", (context, arguments, region) -> new Barrel(Fraction.ZERO))));
        addImplementation(new Implementation("ADD", List.of("BARREL", "BARREL"), new NativeFunction("", (context, arguments, region) -> {
            if (arguments.get(1).value() instanceof VariableReference) {
                context.setVariable(((VariableReference) arguments.get(1).value()).name, new Barrel(((Fraction) arguments.get(0).value().get(context).value).add((Fraction) arguments.get(1).value().get(context).value)));
                return context.getVariable(((VariableReference) arguments.get(1).value()).name, arguments.get(1).region());
            }

            return new Barrel(((Fraction) arguments.get(0).value().get(context).value).add((Fraction) arguments.get(1).value().get(context).value));
        })));
        addImplementation(new Implementation("DIVIDE", List.of("BARREL", "BARREL"), new NativeFunction("", (context, arguments, region) -> {
            if (arguments.get(1).value().get(context).value.equals(Fraction.ZERO)) {

                if (arguments.get(1).value().get(context).originatesFromInput && RunConfiguration.getInstance().mode == RunMode.ANALYZE) {
                    ErrorLog.getInstance().registerError(new BoatError(ErrorType.WARNING, "PossibleError", "Division by zero may occur, the variable originates from input.", arguments.get(1).region()));
                    return new Barrel(Fraction.ONE);
                }

                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "DivisionByZero", "Cannot divide by zero", arguments.get(1).region()));
                return new None();
            }

            if (arguments.get(0).value() instanceof VariableReference) {
                context.setVariable(((VariableReference) arguments.get(0).value()).name, new Barrel(((Fraction) arguments.get(0).value().get(context).value).divide((Fraction) arguments.get(1).value().get(context).value)));
                return context.getVariable(((VariableReference) arguments.get(0).value()).name, arguments.get(0).region());
            }

            return new Barrel(((Fraction) arguments.get(0).value().get(context).value).divide((Fraction) arguments.get(1).value().get(context).value));
        })));
        addImplementation(new Implementation("MULTIPLY", List.of("BARREL", "BARREL"), new NativeFunction("", (context, arguments, region) -> {
            if (arguments.get(0).value() instanceof VariableReference) {
                context.setVariable(((VariableReference) arguments.get(0).value()).name, new Barrel( ((Fraction) arguments.get(0).value().get(context).value).multiply((Fraction) arguments.get(1).value().get(context).value)));
                return context.getVariable(((VariableReference) arguments.get(0).value()).name, arguments.get(0).region());
            }

            return new Barrel(((Fraction) arguments.get(0).value().get(context).value).multiply((Fraction) arguments.get(1).value().get(context).value));
        })));
        addImplementation(new Implementation("SUBTRACT", List.of("BARREL", "BARREL"), new NativeFunction("", (context, arguments, region) -> {
            if (arguments.get(1).value() instanceof VariableReference) {
                context.setVariable(((VariableReference) arguments.get(1).value()).name, new Barrel(((Fraction) arguments.get(1).value().get(context).value).subtract((Fraction) arguments.get(0).value().get(context).value)));
                return context.getVariable(((VariableReference) arguments.get(1).value()).name, arguments.get(1).region());
            }

            return new Barrel(((Fraction) arguments.get(1).value().get(context).value).subtract((Fraction) arguments.get(0).value().get(context).value));
        })));
        addImplementation(new Implementation("REPACK", List.of("BARREL", "PACKAGE"), new NativeFunction("", (context, arguments, region) -> {
            if (!(arguments.get(1).value() instanceof TypeName)) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidFunctionSignature", "The last argument must be a type name.", arguments.get(arguments.size() - 1).region()), true);
                return new None();
            }

            if (arguments.get(0).value() instanceof VariableReference) {
                context.setVariable(((VariableReference) arguments.get(0).value()).name, new Package((arguments.get(0).value()).get(context).value.toString()));
                return context.getVariable(((VariableReference) arguments.get(0).value()).name, arguments.get(0).region());
            }

            context.setVariable(((VariableReference) arguments.get(0).value()).name, new Package((String) arguments.get(0).value().get(context).value));
            return context.getVariable(((VariableReference) arguments.get(0).value()).name, arguments.get(0).region());
        })));
    }

    @Override
    public Variable implementRequest(Context context, Region region) {
        return new Barrel(Fraction.ZERO);
    }
}
