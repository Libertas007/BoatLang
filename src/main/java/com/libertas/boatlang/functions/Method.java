package com.libertas.boatlang.functions;

import com.libertas.boatlang.generics.Region;
import com.libertas.boatlang.parser.Context;
import com.libertas.boatlang.parser.NodeResult;
import com.libertas.boatlang.variables.Variable;

import java.util.ArrayList;
import java.util.List;

public class Method extends BoatFunction {

    MethodLambda function;

    public Method(String name, MethodLambda function) {
        super(name, new ArrayList<>(), new ArrayList<>());
        this.function = function;
    }

    @Override
    public NodeResult run(Context context, List<BoatFunctionArgumentValue> values, Region region) {
        Variable self = values.get(0).value();

        return new NodeResult(function.run(context, self, values, region));
    }

    public NodeResult run(Context context, Variable self, List<BoatFunctionArgumentValue> values, Region region) {
        return new NodeResult(function.run(context, self, values, region));
    }
}
