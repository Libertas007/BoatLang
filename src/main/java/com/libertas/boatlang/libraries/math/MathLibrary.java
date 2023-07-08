package com.libertas.boatlang.libraries.math;

import com.libertas.boatlang.errors.BoatError;
import com.libertas.boatlang.errors.ErrorLog;
import com.libertas.boatlang.errors.ErrorType;
import com.libertas.boatlang.functions.Method;
import com.libertas.boatlang.libraries.Library;
import com.libertas.boatlang.variables.Barrel;
import com.libertas.boatlang.variables.None;
import org.apache.commons.math3.fraction.Fraction;

public class MathLibrary extends Library {
    public MathLibrary() {
        setProperty("PI", new Barrel(new Fraction(Math.PI)));
        setProperty("E", new Barrel(new Fraction(Math.E)));
        setProperty("TAU", new Barrel(new Fraction(Math.TAU)));

        setMethod("ABS", new Method("ABS", (context, self, arguments, region) -> {
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

        setMethod("NEGATIVE", new Method("ABS", (context, self, arguments, region) -> {
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

        setMethod("INVERSE", new Method("ABS", (context, self, arguments, region) -> {
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

        setMethod("ATAN", new Method("ATAN", (context, self, arguments, region) -> {
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

        setMethod("ACOS", new Method("ACOS", (context, self, arguments, region) -> {
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

        setMethod("ASIN", new Method("ASIN", (context, self, arguments, region) -> {
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

        setMethod("COS", new Method("COS", (context, self, arguments, region) -> {
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

        setMethod("COSH", new Method("COSH", (context, self, arguments, region) -> {
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

        setMethod("SIN", new Method("SIN", (context, self, arguments, region) -> {
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

        setMethod("SINH", new Method("SINH", (context, self, arguments, region) -> {
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

        setMethod("TAN", new Method("TAN", (context, self, arguments, region) -> {
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

        setMethod("TANH", new Method("TANH", (context, self, arguments, region) -> {
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

        setMethod("FLOOR", new Method("FLOOR", (context, self, arguments, region) -> {
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

        setMethod("CEIL", new Method("CEIL", (context, self, arguments, region) -> {
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

        setMethod("ROUND", new Method("ROUND", (context, self, arguments, region) -> {
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

        setMethod("RANDOM", new Method("RANDOM", (context, self, arguments, region) -> {
            if (arguments.size() != 0) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidFunctionSignature", "Expected no parameters, got " + arguments.size() + ".", arguments.get(arguments.size() - 1).region()), true);
                return new None();
            }

            return new Barrel(new Fraction(Math.random()));
        }));

        setMethod("SIGNUM", new Method("SIGNUM", (context, self, arguments, region) -> {
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

        setMethod("LOG", new Method("LOG", (context, self, arguments, region) -> {
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

        setMethod("LOG10", new Method("LOG10", (context, self, arguments, region) -> {
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
