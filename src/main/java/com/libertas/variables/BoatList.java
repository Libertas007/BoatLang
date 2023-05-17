package com.libertas.variables;

import com.libertas.errors.BoatError;
import com.libertas.errors.ErrorLog;
import com.libertas.errors.ErrorType;
import com.libertas.functions.BoatFunction;
import com.libertas.functions.Method;
import com.libertas.functions.NativeFunction;
import com.libertas.generics.Region;
import com.libertas.parser.Context;
import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.FractionFormat;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class BoatList extends Variable {
    public List<Variable> value;
    public BoatList(List<Variable> value) {
        super(value, "LIST");
        this.value = value;

        implement();
    }

    @Override
    public String represent() {
        StringJoiner joiner = new StringJoiner(", ");

        for (Variable var: value) {
            joiner.add(var.represent());
        }

        return "[" + joiner + "]";
    }

    @Override
    public Variable implementRequest(Context context, Region region) {
        return new BoatList(new ArrayList<>());
    }

    private void implement() {
        setMethod("LENGTH", new Method("LENGTH", ((context, self, arguments, region) ->
                new Barrel(new Fraction(((BoatList) self.get(context)).value.size()))
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

            for (Variable element: ((BoatList) self).value) {
                joiner.add(element.represent());
            }

            return new Package(joiner.toString());
        })));

        setMethod("REMOVE", new Method("REMOVE", ((context, self, arguments, region) -> {
            if (arguments.size() != 1) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidFunctionSignature", "Expected 1 parameter, got " + arguments.size() + ".", arguments.get(arguments.size() - 1).region()), true);
                return new None();
            }

            if (!(arguments.get(0).value().get(context) instanceof Barrel)) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidFunctionSignature", "The first argument must be a BARREL.", arguments.get(0).region()), true);
                return new None();
            }

            int toRemove = ((Fraction) arguments.get(0).value().get(context).value).intValue();

            if (((BoatList) self).value.size() <= toRemove) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "NotInRange", "Element with index " + toRemove + " does not exist in a list with the size of " + ((BoatList) self).value.size() + ".", arguments.get(0).region()), true);
                return new None();
            }

            return ((BoatList) self).value.remove(toRemove);
        })));

        addImplementation(new Implementation("REQUEST", new ArrayList<>(), new NativeFunction("", (context, arguments, region) -> new BoatList(new ArrayList<>()))));
        addImplementation(new Implementation("ADD", List.of("ANY", "LIST"), new NativeFunction("", (context, arguments, region) -> {
            if (arguments.get(1).value() instanceof VariableReference) {

                List<Variable> newlist = ((BoatList) arguments.get(1).value().get(context)).value;

                newlist.add(arguments.get(0).value().get(context));

                context.setVariable(((VariableReference) arguments.get(1).value()).name, new BoatList(newlist));
                return context.getVariable(((VariableReference) arguments.get(1).value()).name, arguments.get(1).region());
            }

            List<Variable> newlist = ((BoatList) arguments.get(1).value()).value;

            newlist.add(arguments.get(0).value().get(context));

            return new BoatList(newlist);
        })));
    }
}
