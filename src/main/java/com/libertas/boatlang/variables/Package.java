package com.libertas.boatlang.variables;

import com.libertas.boatlang.errors.BoatError;
import com.libertas.boatlang.errors.ErrorLog;
import com.libertas.boatlang.errors.ErrorType;
import com.libertas.boatlang.functions.Method;
import com.libertas.boatlang.functions.NativeFunction;
import com.libertas.boatlang.generics.Region;
import com.libertas.boatlang.parser.Context;
import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.FractionFormat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Package extends Variable {
    public String value;

    public Package(String value) {
        super(value, "PACKAGE");
        this.value = value;

        implement();
    }

    @Override
    public String represent() {
        return value;
    }

    @Override
    public Variable implementRequest(Context context, Region region) {
        return new Package("");
    }

    private void implement() {
        setMethod("UPPERCASE", new Method("UPPERCASE", ((context, self, arguments, region) ->
                new Package(self.get(context).value.toString().toUpperCase())
        )));

        setMethod("LOWERCASE", new Method("LOWERCASE", ((context, self, arguments, region) ->
                new Package(self.get(context).value.toString().toLowerCase())
        )));

        setMethod("SPLIT", new Method("SPLIT", ((context, self, arguments, region) -> {
            if (arguments.size() != 1) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidFunctionSignature", "Expected 1 parameter, got " + arguments.size() + ".", region), true);
                return new None();
            }

            if (!(arguments.get(0).value().get(context) instanceof Package)) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidFunctionSignature", "The first argument must be a PACKAGE.", arguments.get(0).region()), true);
                return new None();
            }

            String selfValue = ((Package) self.get(context)).value;
            String delimiter = ((Package) arguments.get(0).value().get(context)).value;

            return new BoatList(Arrays.stream(selfValue.split(delimiter)).map(e -> (Variable) new Package(e)).toList());
        })));

        addImplementation(new Implementation("REQUEST", new ArrayList<>(), new NativeFunction("", (context, arguments, region) -> new Package(""))));
        addImplementation(new Implementation("ADD", List.of("PACKAGE", "PACKAGE"), new NativeFunction("", (context, arguments, region) -> {
            if (arguments.get(1).value() instanceof VariableReference) {
                context.setVariable(((VariableReference) arguments.get(1).value()).name, new Package(arguments.get(0).value().get(context).value.toString() + (arguments.get(1).value()).get(context).value.toString()));
                return context.getVariable(((VariableReference) arguments.get(1).value()).name, arguments.get(1).region());
            }

            return new Package(arguments.get(0).value().get(context).value + (String) arguments.get(1).value().get(context).value);
        })));
        addImplementation(new Implementation("MULTIPLY", List.of("PACKAGE", "BARREL"), new NativeFunction("", (context, arguments, region) -> {
            if (arguments.get(0).value() instanceof VariableReference) {
                Fraction repeat = (Fraction) arguments.get(1).value().get(context).value;
                String value = (String) arguments.get(0).value().get(context).value;
                StringBuilder builder = new StringBuilder();

                for (int i = 0; i < repeat.doubleValue(); i++) {
                    builder.append(value);
                }

                context.setVariable(((VariableReference) arguments.get(0).value()).name, new Package(builder.toString()));
                return context.getVariable(((VariableReference) arguments.get(0).value()).name, arguments.get(0).region());
            }

            Fraction repeat = (Fraction) arguments.get(1).value().get(context).value;
            String value = (String) arguments.get(0).value().get(context).value;
            StringBuilder builder = new StringBuilder();

            for (int i = 0; i < repeat.doubleValue(); i++) {
                builder.append(value);
            }

            return new Package(builder.toString());
        })));
        addImplementation(new Implementation("REPACK", List.of("PACKAGE", "BARREL"), new NativeFunction("", (context, arguments, region) -> {
            if (!(arguments.get(1).value() instanceof VariableReference typeName)) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidFunctionSignature", "The last argument must be a type name.", arguments.get(arguments.size() - 1).region()), true);
                return new None();
            }

            if (!context.existsType(typeName.name)) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidFunctionSignature", "The last argument must be a type name.", arguments.get(arguments.size() - 1).region()), true);
                return new None();
            }

            if (arguments.get(0).value() instanceof VariableReference reference) {
                try {
                    String value = reference.get(context).value.toString();

                    FractionFormat format = new FractionFormat();

                    Fraction parsed = format.parse(value);

                    context.setVariable(reference.name, new Barrel(parsed));
                    return context.getVariable(reference.name, arguments.get(0).region());
                } catch (NumberFormatException e) {
                    ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidBarrelFormat", "The PACKAGE '" + value + "' cannot be parsed into a BARREL.", arguments.get(0).region()), true);
                    return new None();
                }
            }

            try {
                String value = arguments.get(0).value().get(context).value.toString();

                FractionFormat format = new FractionFormat();

                Fraction parsed = format.parse(value);

                return new Barrel(parsed);
            } catch (NumberFormatException e) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidBarrelFormat", "The PACKAGE '" + value + "' cannot be parsed into a BARREL.", arguments.get(0).region()), true);
                return new None();
            }
        })));
    }
}
