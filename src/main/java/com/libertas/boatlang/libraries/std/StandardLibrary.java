package com.libertas.boatlang.libraries.std;

import com.libertas.boatlang.errors.BoatError;
import com.libertas.boatlang.errors.ErrorLog;
import com.libertas.boatlang.errors.ErrorType;
import com.libertas.boatlang.functions.NativeFunction;
import com.libertas.boatlang.generics.Region;
import com.libertas.boatlang.generics.RunConfiguration;
import com.libertas.boatlang.generics.RunMode;
import com.libertas.boatlang.libraries.Library;
import com.libertas.boatlang.parser.Context;
import com.libertas.boatlang.variables.Package;
import com.libertas.boatlang.variables.*;
import org.apache.commons.math3.fraction.Fraction;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;


public class StandardLibrary extends Library {
    public StandardLibrary() {
        types.put("BARREL", new Barrel(Fraction.ZERO));
        types.put("PACKAGE", new Package(""));
        types.put("SWITCH", new Switch(false));
        types.put("LIST", new BoatList(new ArrayList<>()));
        types.put("NONE", new None());

        setProperty("YES", new Switch(true));
        setProperty("NO", new Switch(false));

        setFunction("ADD", new NativeFunction("ADD", (context, arguments, region) -> {
            if (arguments.size() != 3) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidFunctionSignature", "Expected 3 parameters, got " + arguments.size() + ".", region), true);
                return new None();
            }

            if (!arguments.get(1).value().get(context).value.equals("TO")) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidFunctionSignature", "The second argument must be 'TO'.", arguments.get(arguments.size() - 1).region()), true);
                return new None();
            }

            return arguments.get(2).value().implementAdd(arguments.get(0), arguments.get(2), context, region);
        }));

        setFunction("SUBTRACT", new NativeFunction("SUBTRACT", (context, arguments, region) -> {
            if (arguments.size() != 3) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidFunctionSignature", "Expected 3 parameters, got " + arguments.size() + ".", region), true);
                return new None();
            }

            if (!arguments.get(1).value().get(context).value.equals("FROM")) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidFunctionSignature", "The second argument must be 'FROM'.", arguments.get(arguments.size() - 1).region()), true);
                return new None();
            }

            return arguments.get(2).value().implementSubtract(arguments.get(0), arguments.get(2), context, region);
        }));

        setFunction("MULTIPLY", new NativeFunction("MULTIPLY", (context, arguments, region) -> {
            if (arguments.size() != 3) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidFunctionSignature", "Expected 3 parameters, got " + arguments.size() + ".", region), true);
                return new None();
            }

            if (!arguments.get(1).value().get(context).value.equals("BY")) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidFunctionSignature", "The second argument must be 'BY'.", arguments.get(arguments.size() - 1).region()), true);
                return new None();
            }

            return arguments.get(0).value().implementMultiply(arguments.get(0), arguments.get(2), context, region);
        }));

        setFunction("DIVIDE", new NativeFunction("DIVIDE", (context, arguments, region) -> {
            if (arguments.size() != 3) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidFunctionSignature", "Expected 3 parameters, got " + arguments.size() + ".", region), true);
                return new None();
            }

            if (!arguments.get(1).value().get(context).value.equals("BY")) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidFunctionSignature", "The second argument must be 'BY'.", arguments.get(arguments.size() - 1).region()), true);
                return new None();
            }

            return arguments.get(0).value().implementDivide(arguments.get(0), arguments.get(2), context, region);
        }));

        setFunction("REPACK", new NativeFunction("REPACK", (context, arguments, region) -> {
            if (arguments.size() != 3) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidFunctionSignature", "Expected 3 parameters, got " + arguments.size() + ".", region), true);
                return new None();
            }

            if (!arguments.get(1).value().get(context).value.equals("TO")) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidFunctionSignature", "The second argument must be 'TO'.", arguments.get(arguments.size() - 1).region()), true);
                return new None();
            }

            return arguments.get(0).value().implementRepack(arguments.get(0), arguments.get(2), context, region);
        }));

        setFunction("SET", new NativeFunction("SET", (context, arguments, region) -> {
            if (arguments.size() != 3) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidFunctionSignature", "Expected 3 parameters, got " + arguments.size() + ".", region), true);
                return new None();
            }

            if (!arguments.get(1).value().get(context).value.equals("TO")) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidFunctionSignature", "The second argument must be 'TO'.", arguments.get(1).region()), true);
                return new None();
            }

            if (!(arguments.get(0).value() instanceof VariableReference)) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidFunctionSignature", "The first argument must be an identifier.", arguments.get(0).region()), true);
                return new None();
            }

            if (!Objects.equals(arguments.get(0).value().get(context).displayName, arguments.get(2).value().get(context).displayName)) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "MismatchedTypes", "Expected " + arguments.get(0).value().get(context).displayName + ", got " + arguments.get(2).value().get(context).displayName + ".", arguments.get(2).region()), true);
                return new None();
            }


            context.setVariable(((VariableReference) arguments.get(0).value()).name, arguments.get(2).value().get(context));
            return context.getVariable(((VariableReference) arguments.get(0).value()).name, arguments.get(2).region());
        }));

        setFunction("REQUEST", new NativeFunction("REQUEST", (context, arguments, region) -> {
            if (arguments.size() != 2) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidFunctionSignature", "Expected 2 parameters, got " + arguments.size() + ".", region), true);
                return new None();
            }


            if (!(arguments.get(0).value() instanceof VariableReference typeName)) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidFunctionSignature", "The first argument must be an identifier.", arguments.get(0).region()), true);
                return new None();
            }

            if (!context.existsType(typeName.name)) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidFunctionSignature", "The first argument must be a type name.", arguments.get(0).region()), true);
                return new None();
            }

            if (!(arguments.get(1).value() instanceof VariableReference)) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidFunctionSignature", "The second argument must be an identifier.", arguments.get(0).region()), true);
                return new None();
            }

            context.setVariable(((VariableReference) arguments.get(1).value()).name, context.getDefaultValue(typeName.name, region));

            return context.getVariable(((VariableReference) arguments.get(1).value()).name, arguments.get(1).region());
        }));

        setFunction("BROADCAST", new NativeFunction("BROADCAST", (context, arguments, region) -> {
            if (arguments.size() != 1) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidFunctionSignature", "Expected 1 parameter, got " + arguments.size() + ".", region), true);
                return new None();
            }

            if (RunConfiguration.getInstance().mode == RunMode.ANALYZE || RunConfiguration.getInstance().mode == RunMode.IMPORT) {
                return arguments.get(0).value();
            }

            System.out.println(arguments.get(0).value().get(context).represent());

            return arguments.get(0).value();
        }));

        setFunction("LISTEN", new NativeFunction("LISTEN", (context, arguments, region) -> {
            if (arguments.size() != 2) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidFunctionSignature", "Expected 2 parameters, got " + arguments.size() + ".", region), true);
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

            if (RunConfiguration.getInstance().mode == RunMode.ANALYZE || RunConfiguration.getInstance().mode == RunMode.IMPORT) {
                com.libertas.boatlang.variables.Package result = new com.libertas.boatlang.variables.Package("");
                result.originatesFromInput = true;
                context.setVariable(((VariableReference) arguments.get(1).value()).name, result);
                return result;
            }

            Scanner scanner = new Scanner(System.in);

            String data = scanner.nextLine();

            context.setVariable(((VariableReference) arguments.get(1).value()).name, new com.libertas.boatlang.variables.Package(data));

            return context.getVariable(((VariableReference) arguments.get(1).value()).name, arguments.get(1).region());
        }));

        setFunction("ARRIVE", new NativeFunction("ARRIVE", (context, arguments, region) -> {
            if (arguments.size() != 2) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidFunctionSignature", "Expected 2 parameters, got " + arguments.size() + ".", region), true);
                return new None();
            }

            if (!arguments.get(0).value().get(context).value.equals("AT")) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidFunctionSignature", "The second argument must be 'AT'.", arguments.get(arguments.size() - 1).region()), true);
                return new None();
            }

            if (RunConfiguration.getInstance().mode == RunMode.ANALYZE || RunConfiguration.getInstance().mode == RunMode.IMPORT) {
                return new None();
            }

            System.exit(0);
            return new None();
        }));

        setFunction("CRASH", new NativeFunction("CRASH", (context, arguments, region) -> {
            if (arguments.size() != 2) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidFunctionSignature", "Expected 2 parameters, got " + arguments.size() + ".", region), true);
                return new None();
            }

            if (!arguments.get(0).value().get(context).value.equals("INTO")) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidFunctionSignature", "The second argument must be 'INTO'.", arguments.get(arguments.size() - 1).region()), true);
                return new None();
            }

            if (RunConfiguration.getInstance().mode == RunMode.ANALYZE || RunConfiguration.getInstance().mode == RunMode.IMPORT) {
                return new None();
            }

            System.exit(1);
            return new None();
        }));

        setFunction("SINK", new NativeFunction("SINK", (context, arguments, region) -> {
            if (arguments.size() != 0) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidFunctionSignature", "Expected 0 parameters, got " + arguments.size() + ".", region), true);
                return new None();
            }

            if (RunConfiguration.getInstance().mode == RunMode.ANALYZE || RunConfiguration.getInstance().mode == RunMode.IMPORT) {
                return new None();
            }

            System.exit(1);
            return new None();
        }));

        setFunction("WAIT", new NativeFunction("WAIT", (context, arguments, region) -> {
            if (arguments.size() != 1) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidFunctionSignature", "Expected 1 parameter, got " + arguments.size() + ".", region), true);
                return new None();
            }

            if (RunConfiguration.getInstance().mode == RunMode.ANALYZE || RunConfiguration.getInstance().mode == RunMode.IMPORT) {
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