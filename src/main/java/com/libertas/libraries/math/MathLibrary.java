package com.libertas.libraries.math;

import com.libertas.errors.BoatError;
import com.libertas.errors.ErrorLog;
import com.libertas.errors.ErrorType;
import com.libertas.functions.NativeFunction;
import com.libertas.libraries.Library;
import com.libertas.variables.Barrel;
import com.libertas.variables.None;
import org.apache.commons.math3.fraction.Fraction;

public class MathLibrary extends Library {
    public MathLibrary() {
        variables.put("MATH.PI", new Barrel(new Fraction(Math.PI)));
        variables.put("MATH.E", new Barrel(new Fraction(Math.E)));
        variables.put("MATH.TAU", new Barrel(new Fraction(Math.TAU)));

        functions.put("MATH.ABS", new NativeFunction("MATH.ABS", (context, arguments, region) -> {
            if (arguments.size() != 1) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidFunctionSignature", "Expected 1 parameter, got " + arguments.size() + ".", arguments.get(arguments.size() - 1).region()), true);
                return new None();
            }

            if (!(arguments.get(0).value().get(context) instanceof Barrel)) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "MismatchedTypes", "Expected BARREL, got " + arguments.get(0).value().get(context).displayName + ".", arguments.get(0).region()), true);
                return new None();
            }

            return new Barrel(new Fraction(Math.abs(((Fraction) arguments.get(0).value().get(context).value).doubleValue())));
        }));

        functions.put("MATH.NEGATIVE", new NativeFunction("MATH.ABS", (context, arguments, region) -> {
            if (arguments.size() != 1) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidFunctionSignature", "Expected 1 parameter, got " + arguments.size() + ".", arguments.get(arguments.size() - 1).region()), true);
                return new None();
            }

            if (!(arguments.get(0).value().get(context) instanceof Barrel)) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "MismatchedTypes", "Expected BARREL, got " + arguments.get(0).value().get(context).displayName + ".", arguments.get(0).region()), true);
                return new None();
            }

            return new Barrel(new Fraction(-Math.abs(((Fraction) arguments.get(0).value().get(context).value).doubleValue())));
        }));

        functions.put("MATH.INVERSE", new NativeFunction("MATH.ABS", (context, arguments, region) -> {
            if (arguments.size() != 1) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidFunctionSignature", "Expected 1 parameter, got " + arguments.size() + ".", arguments.get(arguments.size() - 1).region()), true);
                return new None();
            }

            if (!(arguments.get(0).value().get(context) instanceof Barrel)) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "MismatchedTypes", "Expected BARREL, got " + arguments.get(0).value().get(context).displayName + ".", arguments.get(0).region()), true);
                return new None();
            }

            return new Barrel(((Fraction) arguments.get(0).value().get(context).value).negate());
        }));

        functions.put("MATH.ATAN", new NativeFunction("MATH.ATAN", (context, arguments, region) -> {
            if (arguments.size() != 1) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidFunctionSignature", "Expected 1 parameter, got " + arguments.size() + ".", arguments.get(arguments.size() - 1).region()), true);
                return new None();
            }

            if (!(arguments.get(0).value().get(context) instanceof Barrel)) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "MismatchedTypes", "Expected BARREL, got " + arguments.get(0).value().get(context).displayName + ".", arguments.get(0).region()), true);
                return new None();
            }

            return new Barrel(new Fraction(Math.atan(((Fraction) arguments.get(0).value().get(context).value).doubleValue())));
        }));

        functions.put("MATH.ACOS", new NativeFunction("MATH.ACOS", (context, arguments, region) -> {
            if (arguments.size() != 1) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidFunctionSignature", "Expected 1 parameter, got " + arguments.size() + ".", arguments.get(arguments.size() - 1).region()), true);
                return new None();
            }

            if (!(arguments.get(0).value().get(context) instanceof Barrel)) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "MismatchedTypes", "Expected BARREL, got " + arguments.get(0).value().get(context).displayName + ".", arguments.get(0).region()), true);
                return new None();
            }

            return new Barrel(new Fraction(Math.acos(((Fraction) arguments.get(0).value().get(context).value).doubleValue())));
        }));

        functions.put("MATH.ASIN", new NativeFunction("MATH.ASIN", (context, arguments, region) -> {
            if (arguments.size() != 1) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidFunctionSignature", "Expected 1 parameter, got " + arguments.size() + ".", arguments.get(arguments.size() - 1).region()), true);
                return new None();
            }

            if (!(arguments.get(0).value().get(context) instanceof Barrel)) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "MismatchedTypes", "Expected BARREL, got " + arguments.get(0).value().get(context).displayName + ".", arguments.get(0).region()), true);
                return new None();
            }

            return new Barrel(new Fraction(Math.asin(((Fraction) arguments.get(0).value().get(context).value).doubleValue())));
        }));

        functions.put("MATH.COS", new NativeFunction("MATH.COS", (context, arguments, region) -> {
            if (arguments.size() != 1) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidFunctionSignature", "Expected 1 parameter, got " + arguments.size() + ".", arguments.get(arguments.size() - 1).region()), true);
                return new None();
            }

            if (!(arguments.get(0).value().get(context) instanceof Barrel)) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "MismatchedTypes", "Expected BARREL, got " + arguments.get(0).value().get(context).displayName + ".", arguments.get(0).region()), true);
                return new None();
            }

            return new Barrel(new Fraction(Math.cos(((Fraction) arguments.get(0).value().get(context).value).doubleValue())));
        }));

        functions.put("MATH.COSH", new NativeFunction("MATH.COSH", (context, arguments, region) -> {
            if (arguments.size() != 1) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidFunctionSignature", "Expected 1 parameter, got " + arguments.size() + ".", arguments.get(arguments.size() - 1).region()), true);
                return new None();
            }

            if (!(arguments.get(0).value().get(context) instanceof Barrel)) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "MismatchedTypes", "Expected BARREL, got " + arguments.get(0).value().get(context).displayName + ".", arguments.get(0).region()), true);
                return new None();
            }

            return new Barrel(new Fraction(Math.cosh(((Fraction) arguments.get(0).value().get(context).value).doubleValue())));
        }));

        functions.put("MATH.SIN", new NativeFunction("MATH.SIN", (context, arguments, region) -> {
            if (arguments.size() != 1) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidFunctionSignature", "Expected 1 parameter, got " + arguments.size() + ".", arguments.get(arguments.size() - 1).region()), true);
                return new None();
            }

            if (!(arguments.get(0).value().get(context) instanceof Barrel)) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "MismatchedTypes", "Expected BARREL, got " + arguments.get(0).value().get(context).displayName + ".", arguments.get(0).region()), true);
                return new None();
            }

            return new Barrel(new Fraction(Math.sin(((Fraction) arguments.get(0).value().get(context).value).doubleValue())));
        }));

        functions.put("MATH.SINH", new NativeFunction("MATH.SINH", (context, arguments, region) -> {
            if (arguments.size() != 1) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidFunctionSignature", "Expected 1 parameter, got " + arguments.size() + ".", arguments.get(arguments.size() - 1).region()), true);
                return new None();
            }

            if (!(arguments.get(0).value().get(context) instanceof Barrel)) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "MismatchedTypes", "Expected BARREL, got " + arguments.get(0).value().get(context).displayName + ".", arguments.get(0).region()), true);
                return new None();
            }

            return new Barrel(new Fraction(Math.sinh(((Fraction) arguments.get(0).value().get(context).value).doubleValue())));
        }));

        functions.put("MATH.TAN", new NativeFunction("MATH.TAN", (context, arguments, region) -> {
            if (arguments.size() != 1) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidFunctionSignature", "Expected 1 parameter, got " + arguments.size() + ".", arguments.get(arguments.size() - 1).region()), true);
                return new None();
            }

            if (!(arguments.get(0).value().get(context) instanceof Barrel)) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "MismatchedTypes", "Expected BARREL, got " + arguments.get(0).value().get(context).displayName + ".", arguments.get(0).region()), true);
                return new None();
            }

            return new Barrel(new Fraction(Math.tan(((Fraction) arguments.get(0).value().get(context).value).doubleValue())));
        }));

        functions.put("MATH.TANH", new NativeFunction("MATH.TANH", (context, arguments, region) -> {
            if (arguments.size() != 1) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidFunctionSignature", "Expected 1 parameter, got " + arguments.size() + ".", arguments.get(arguments.size() - 1).region()), true);
                return new None();
            }

            if (!(arguments.get(0).value().get(context) instanceof Barrel)) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "MismatchedTypes", "Expected BARREL, got " + arguments.get(0).value().get(context).displayName + ".", arguments.get(0).region()), true);
                return new None();
            }

            return new Barrel(new Fraction(Math.tanh(((Fraction) arguments.get(0).value().get(context).value).doubleValue())));
        }));

        functions.put("MATH.FLOOR", new NativeFunction("MATH.FLOOR", (context, arguments, region) -> {
            if (arguments.size() != 1) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidFunctionSignature", "Expected 1 parameter, got " + arguments.size() + ".", arguments.get(arguments.size() - 1).region()), true);
                return new None();
            }

            if (!(arguments.get(0).value().get(context) instanceof Barrel)) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "MismatchedTypes", "Expected BARREL, got " + arguments.get(0).value().get(context).displayName + ".", arguments.get(0).region()), true);
                return new None();
            }

            return new Barrel(new Fraction(Math.floor(((Fraction) arguments.get(0).value().get(context).value).doubleValue())));
        }));

        functions.put("MATH.CEIL", new NativeFunction("MATH.CEIL", (context, arguments, region) -> {
            if (arguments.size() != 1) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidFunctionSignature", "Expected 1 parameter, got " + arguments.size() + ".", arguments.get(arguments.size() - 1).region()), true);
                return new None();
            }

            if (!(arguments.get(0).value().get(context) instanceof Barrel)) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "MismatchedTypes", "Expected BARREL, got " + arguments.get(0).value().get(context).displayName + ".", arguments.get(0).region()), true);
                return new None();
            }

            return new Barrel(new Fraction(Math.ceil(((Fraction) arguments.get(0).value().get(context).value).doubleValue())));

        }));

        functions.put("MATH.ROUND", new NativeFunction("MATH.ROUND", (context, arguments, region) -> {
            if (arguments.size() != 1) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidFunctionSignature", "Expected 1 parameter, got " + arguments.size() + ".", arguments.get(arguments.size() - 1).region()), true);
                return new None();
            }

            if (!(arguments.get(0).value().get(context) instanceof Barrel)) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "MismatchedTypes", "Expected BARREL, got " + arguments.get(0).value().get(context).displayName + ".", arguments.get(0).region()), true);
                return new None();
            }

            return new Barrel(new Fraction(Math.round(((Fraction) arguments.get(0).value().get(context).value).doubleValue())));
        }));

        functions.put("MATH.RANDOM", new NativeFunction("MATH.RANDOM", (context, arguments, region) -> {
            if (arguments.size() != 0) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidFunctionSignature", "Expected no parameters, got " + arguments.size() + ".", arguments.get(arguments.size() - 1).region()), true);
                return new None();
            }

            return new Barrel(new Fraction(Math.random()));
        }));

        functions.put("MATH.SIGNUM", new NativeFunction("MATH.SIGNUM", (context, arguments, region) -> {
            if (arguments.size() != 1) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidFunctionSignature", "Expected 1 parameter, got " + arguments.size() + ".", arguments.get(arguments.size() - 1).region()), true);
                return new None();
            }

            if (!(arguments.get(0).value().get(context) instanceof Barrel)) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "MismatchedTypes", "Expected BARREL, got " + arguments.get(0).value().get(context).displayName + ".", arguments.get(0).region()), true);
                return new None();
            }

            return new Barrel(new Fraction(Math.signum(((Fraction) arguments.get(0).value().get(context).value).doubleValue())));
        }));

        functions.put("MATH.LOG", new NativeFunction("MATH.LOG", (context, arguments, region) -> {
            if (arguments.size() != 1) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidFunctionSignature", "Expected 1 parameter, got " + arguments.size() + ".", arguments.get(arguments.size() - 1).region()), true);
                return new None();
            }

            if (!(arguments.get(0).value().get(context) instanceof Barrel)) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "MismatchedTypes", "Expected BARREL, got " + arguments.get(0).value().get(context).displayName + ".", arguments.get(0).region()), true);
                return new None();
            }

            return new Barrel(new Fraction(Math.log(((Fraction) arguments.get(0).value().get(context).value).doubleValue())));
        }));

        functions.put("MATH.LOG10", new NativeFunction("MATH.LOG10", (context, arguments, region) -> {
            if (arguments.size() != 1) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidFunctionSignature", "Expected 1 parameter, got " + arguments.size() + ".", arguments.get(arguments.size() - 1).region()), true);
                return new None();
            }

            if (!(arguments.get(0).value().get(context) instanceof Barrel)) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "MismatchedTypes", "Expected BARREL, got " + arguments.get(0).value().get(context).displayName + ".", arguments.get(0).region()), true);
                return new None();
            }

            return new Barrel(new Fraction(Math.log10(((Fraction) arguments.get(0).value().get(context).value).doubleValue())));
        }));
    }
}
