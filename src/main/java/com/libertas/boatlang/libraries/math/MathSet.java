package com.libertas.boatlang.libraries.math;

import com.libertas.boatlang.errors.BoatError;
import com.libertas.boatlang.errors.ErrorLog;
import com.libertas.boatlang.errors.ErrorType;
import com.libertas.boatlang.functions.Method;
import com.libertas.boatlang.functions.NativeFunction;
import com.libertas.boatlang.generics.Region;
import com.libertas.boatlang.parser.Context;
import com.libertas.boatlang.variables.Package;
import com.libertas.boatlang.variables.*;
import org.apache.commons.math3.fraction.Fraction;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.StringJoiner;

public class MathSet extends Variable {
    HashSet<Variable> value;

    public MathSet(HashSet<Variable> value) {
        super(value, "MATHSET");

        this.value = value;

        implement();
        methods();
    }

    @Override
    public String represent() {
        StringJoiner joiner = new StringJoiner(", ");

        for (Variable var : value) {
            joiner.add(var.represent());
        }

        return "{" + joiner + "}";
    }

    @Override
    public Variable implementRequest(Context context, Region region) {
        return new MathSet(new HashSet<>());
    }

    private void methods() {
        setMethod("SIZE", new Method("SIZE", ((context, self, arguments, region) ->
                new Barrel(new Fraction(((MathSet) self.get(context)).value.size()))
        )));

        setMethod("JOIN", new Method("JOIN", ((context, self, arguments, region) -> {
            if (arguments.size() != 1) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidFunctionSignature", "Expected 1 parameter, got " + arguments.size() + ".", arguments.get(arguments.size() - 1).region()), true);
                return new None();
            }

            if (!(arguments.get(0).value().get(context) instanceof Package)) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidFunctionSignature", "The first argument must be a PACKAGE.", arguments.get(0).region()), true);
                return new None();
            }

            StringJoiner joiner = new StringJoiner(arguments.get(0).value().get(context).value.toString());

            for (Variable element : ((MathSet) self).value) {
                joiner.add(element.represent());
            }

            return new Package(joiner.toString());
        })));

        setMethod("REMOVE", new Method("REMOVE", ((context, self, arguments, region) -> {
            if (arguments.size() != 1) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidFunctionSignature", "Expected 1 argument, got " + arguments.size() + ".", arguments.get(arguments.size() - 1).region()), true);
                return new None();
            }

            Variable toRemove = arguments.get(0).value().get(context);

            ((MathSet) self).value.removeIf(var -> var.value.equals(toRemove.value) && var.displayName.equals(toRemove.displayName));
            return toRemove;
        })));

        setMethod("CONTAINS", new Method("CONTAINS", ((context, self, arguments, region) -> {
            if (arguments.size() != 1) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidFunctionSignature", "Expected 1 argument, got " + arguments.size() + ".", arguments.get(arguments.size() - 1).region()), true);
                return new None();
            }

            Variable shouldContain = arguments.get(0).value().get(context);

            for (Variable predicate : ((MathSet) self).value) {
                if (!predicate.value.equals(shouldContain.value)) continue;
                if (!predicate.displayName.equals(shouldContain.displayName)) continue;

                return new Switch(true);
            }

            return new Switch(false);
        })));

        setMethod("TOLIST", new Method("TOLIST", ((context, self, arguments, region) -> {
            if (arguments.size() != 0) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidFunctionSignature", "Expected no arguments, got " + arguments.size() + ".", arguments.get(arguments.size() - 1).region()), true);
                return new None();
            }

            return new BoatList(((MathSet) self).value.stream().toList());
        })));
    }

    private void implement() {
        addImplementation(new Implementation("REQUEST", new ArrayList<>(), new NativeFunction("", (context, arguments, region) -> new BoatList(new ArrayList<>()))));
        addImplementation(new Implementation("ADD", List.of("ANY", "MATHSET"), new NativeFunction("", (context, arguments, region) -> {
            if (arguments.get(1).value() instanceof VariableReference) {

                HashSet<Variable> newSet = ((MathSet) arguments.get(1).value().get(context)).value;

                newSet.add(arguments.get(0).value().get(context));

                context.setVariable(((VariableReference) arguments.get(1).value()).name, new MathSet(newSet));
                return context.getVariable(((VariableReference) arguments.get(1).value()).name, arguments.get(1).region());
            }

            HashSet<Variable> newSet = ((MathSet) arguments.get(1).value()).value;

            newSet.add(arguments.get(0).value().get(context));

            return new MathSet(newSet);
        })));
    }

}
