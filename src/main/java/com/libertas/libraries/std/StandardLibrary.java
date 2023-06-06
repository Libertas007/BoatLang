package com.libertas.libraries.std;

import com.libertas.errors.BoatError;
import com.libertas.errors.ErrorLog;
import com.libertas.errors.ErrorType;
import com.libertas.functions.NativeFunction;
import com.libertas.generics.Region;
import com.libertas.generics.RunConfiguration;
import com.libertas.generics.RunMode;
import com.libertas.libraries.Library;
import com.libertas.parser.Context;
import com.libertas.variables.*;
import com.libertas.variables.Package;
import org.apache.commons.math3.fraction.Fraction;

import java.util.ArrayList;
import java.util.Scanner;


public class StandardLibrary extends Library {
    public StandardLibrary() {
        variables.put("YES", new Switch(true));
        variables.put("NO", new Switch(false));

        functions.put("ADD", new NativeFunction("ADD", (context, arguments, region) -> {
            if (arguments.size() != 3) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidFunctionSignature", "Expected 3 parameters, got " + arguments.size() + ".", arguments.get(arguments.size() - 1).region()), true);
                return new None();
            }

            if (!arguments.get(1).value().get(context).value.equals("TO")) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidFunctionSignature", "The second argument must be 'TO'.", arguments.get(arguments.size() - 1).region()), true);
                return new None();
            }

            return arguments.get(2).value().implementAdd(arguments.get(0), arguments.get(2), context, region);
        }));

        functions.put("SUBTRACT", new NativeFunction("SUBTRACT", (context, arguments, region) -> {
            if (arguments.size() != 3) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidFunctionSignature", "Expected 3 parameters, got " + arguments.size() + ".", arguments.get(arguments.size() - 1).region()), true);
                return new None();
            }

            if (!arguments.get(1).value().get(context).value.equals("FROM")) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidFunctionSignature", "The second argument must be 'FROM'.", arguments.get(arguments.size() - 1).region()), true);
                return new None();
            }

            return arguments.get(2).value().implementSubtract(arguments.get(0), arguments.get(2), context, region);
        }));

        functions.put("MULTIPLY", new NativeFunction("MULTIPLY", (context, arguments, region) -> {
            if (arguments.size() != 3) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidFunctionSignature", "Expected 3 parameters, got " + arguments.size() + ".", arguments.get(arguments.size() - 1).region()), true);
                return new None();
            }

            if (!arguments.get(1).value().get(context).value.equals("BY")) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidFunctionSignature", "The second argument must be 'BY'.", arguments.get(arguments.size() - 1).region()), true);
                return new None();
            }

            return arguments.get(0).value().implementMultiply(arguments.get(0), arguments.get(2), context, region);
        }));

        functions.put("DIVIDE", new NativeFunction("DIVIDE", (context, arguments, region) -> {
            if (arguments.size() != 3) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidFunctionSignature", "Expected 3 parameters, got " + arguments.size() + ".", arguments.get(arguments.size() - 1).region()), true);
                return new None();
            }

            if (!arguments.get(1).value().get(context).value.equals("BY")) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidFunctionSignature", "The second argument must be 'BY'.", arguments.get(arguments.size() - 1).region()), true);
                return new None();
            }

            return arguments.get(0).value().implementDivide(arguments.get(0), arguments.get(2), context, region);
        }));

        functions.put("REPACK", new NativeFunction("REPACK", (context, arguments, region) -> {
            if (arguments.size() != 3) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidFunctionSignature", "Expected 3 parameters, got " + arguments.size() + ".", arguments.get(arguments.size() - 1).region()), true);
                return new None();
            }

            if (!arguments.get(1).value().get(context).value.equals("TO")) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidFunctionSignature", "The second argument must be 'TO'.", arguments.get(arguments.size() - 1).region()), true);
                return new None();
            }

            return arguments.get(0).value().implementRepack(arguments.get(0), arguments.get(2), context, region);
        }));

        functions.put("SET", new NativeFunction("SET", (context, arguments, region) -> {
            if (arguments.size() != 3) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidFunctionSignature", "Expected 3 parameters, got " + arguments.size() + ".", arguments.get(arguments.size() - 1).region()), true);
                return new None();
            }

            if (!arguments.get(1).value().get(context).value.equals("TO")) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidFunctionSignature", "The second argument must be 'TO'.", arguments.get(arguments.size() - 1).region()), true);
                return new None();
            }

            if (!(arguments.get(0).value() instanceof VariableReference)) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidFunctionSignature", "The first argument must be an identifier.", arguments.get(arguments.size() - 1).region()), true);
                return new None();
            }

            context.setVariable(((VariableReference) arguments.get(0).value()).name, arguments.get(2).value().get(context));
            return context.getVariable(((VariableReference) arguments.get(0).value()).name, arguments.get(0).region());
        }));

        functions.put("REQUEST", new NativeFunction("REQUEST", (context, arguments, region) -> {
            if (arguments.size() != 2) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidFunctionSignature", "Expected 2 parameters, got " + arguments.size() + ".", arguments.get(arguments.size() - 1).region()), true);
                return new None();
            }

            if (!(arguments.get(0).value() instanceof TypeName)) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidFunctionSignature", "The first argument must be a type name.", arguments.get(arguments.size() - 1).region()), true);
                return new None();
            }

            if (!(arguments.get(1).value() instanceof VariableReference)) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidFunctionSignature", "The second argument must be an identifier.", arguments.get(arguments.size() - 1).region()), true);
                return new None();
            }

            context.setVariable(((VariableReference) arguments.get(1).value()).name, defaultValue(arguments.get(0).value().value.toString(), context, region));

            return context.getVariable(((VariableReference) arguments.get(1).value()).name, arguments.get(1).region());
        }));

        functions.put("BROADCAST", new NativeFunction("BROADCAST", (context, arguments, region) -> {
            if (arguments.size() != 1) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidFunctionSignature", "Expected 1 parameter, got " + arguments.size() + ".", arguments.get(arguments.size() - 1).region()), true);
                return new None();
            }

            if (RunConfiguration.getInstance().mode == RunMode.ANALYZE) {
                return arguments.get(0).value();
            }

            System.out.println(arguments.get(0).value().get(context).represent());

            return arguments.get(0).value();
        }));

        functions.put("LISTEN", new NativeFunction("LISTEN", (context, arguments, region) -> {
            if (arguments.size() != 2) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidFunctionSignature", "Expected 2 parameters, got " + arguments.size() + ".", arguments.get(arguments.size() - 1).region()), true);
                return new None();
            }

            if (!arguments.get(0).value().get(context).value.equals("TO")) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidFunctionSignature", "The second argument must be 'TO'.", arguments.get(arguments.size() - 1).region()), true);
                return new None();
            }

            if (!(arguments.get(1).value() instanceof VariableReference)) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidFunctionSignature", "The second argument must be an identifier.", arguments.get(arguments.size() - 1).region()), true);
                return new None();
            }

            if (RunConfiguration.getInstance().mode == RunMode.ANALYZE) {
                Package result = new Package("");
                result.originatesFromInput = true;
                context.setVariable(((VariableReference) arguments.get(1).value()).name, result);
                return result;
            }

            Scanner scanner = new Scanner(System.in);

            String data = scanner.nextLine();

            context.setVariable(((VariableReference) arguments.get(1).value()).name, new Package(data));

            return context.getVariable(((VariableReference) arguments.get(1).value()).name, arguments.get(1).region());
        }));

        functions.put("ARRIVE", new NativeFunction("ARRIVE", (context, arguments, region) -> {
            if (arguments.size() != 2) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidFunctionSignature", "Expected 2 parameters, got " + arguments.size() + ".", arguments.get(arguments.size() - 1).region()), true);
                return new None();
            }

            if (!arguments.get(0).value().get(context).value.equals("AT")) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidFunctionSignature", "The second argument must be 'AT'.", arguments.get(arguments.size() - 1).region()), true);
                return new None();
            }

            if (RunConfiguration.getInstance().mode == RunMode.ANALYZE) {
                return new None();
            }

            System.exit(0);
            return new None();
        }));

        functions.put("CRASH", new NativeFunction("CRASH", (context, arguments, region) -> {
            if (arguments.size() != 2) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidFunctionSignature", "Expected 2 parameters, got " + arguments.size() + ".", arguments.get(arguments.size() - 1).region()), true);
                return new None();
            }

            if (!arguments.get(0).value().get(context).value.equals("INTO")) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidFunctionSignature", "The second argument must be 'INTO'.", arguments.get(arguments.size() - 1).region()), true);
                return new None();
            }

            if (RunConfiguration.getInstance().mode == RunMode.ANALYZE) {
                return new None();
            }

            System.exit(1);
            return new None();
        }));

        functions.put("SINK", new NativeFunction("SINK", (context, arguments, region) -> {
            if (arguments.size() != 0) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidFunctionSignature", "Expected 0 parameters, got " + arguments.size() + ".", arguments.get(arguments.size() - 1).region()), true);
                return new None();
            }

            if (RunConfiguration.getInstance().mode == RunMode.ANALYZE) {
                return new None();
            }

            System.exit(1);
            return new None();
        }));

        functions.put("WAIT", new NativeFunction("WAIT", (context, arguments, region) -> {
            if (arguments.size() != 1) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidFunctionSignature", "Expected 1 parameter, got " + arguments.size() + ".", arguments.get(arguments.size() - 1).region()), true);
                return new None();
            }

            if (RunConfiguration.getInstance().mode == RunMode.ANALYZE) {
                return new None();
            }

            if (arguments.get(0).value().get(context) instanceof Barrel) {
                try {
                    Thread.sleep(((Double) arguments.get(0).value().get(context).value).longValue());
                } catch (InterruptedException e) {
                    ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "ThreadInterrupted", "The current thread has been interrupted.", region), true);

                }
                return arguments.get(0).value().get(context);
            }

            ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "MismatchedTypes", "Expected a BARREL, got " + arguments.get(0).value().get(context).displayName + ".", arguments.get(0).region()), true);

            return new None();
        }));
    }

    public Variable defaultValue(String type, Context context, Region region) {
        switch (type) {
            case "BARREL" -> {
                return new Barrel(Fraction.ZERO).implementRequest(context, region);
            }
            case "PACKAGE" -> {
                return new Package("").implementRequest(context, region);
            }
            case "SWITCH" -> {
                return new Switch(false).implementRequest(context, region);
            }
            case "LIST" -> {
                return new BoatList(new ArrayList<>()).implementRequest(context, region);
            }
            default -> {
                return new None().implementRequest(context, region);
            }
        }
    }
}

/*
* functions:
*
* REPACK ✅
* REQUEST ✅
* RETURN ✅
* DROP ✅
* SET ✅
*
* ADD ✅
* SUBTRACT ✅
* DIVIDE ✅
* MULTIPLY ✅
*
* BROADCAST ✅
* LISTEN ✅
*
* ARRIVE ✅
* CRASH ✅
* SINK ✅
*
* */