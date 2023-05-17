package com.libertas.functions;

import com.libertas.generics.Region;
import com.libertas.parser.Context;
import com.libertas.parser.NodeResult;
import com.libertas.parser.nodes.StatementNode;
import com.libertas.variables.Variable;

import java.util.ArrayList;
import java.util.List;

public class NativeFunction extends BoatFunction {

    NativeFunctionLambda function;

    public NativeFunction(String name, NativeFunctionLambda function) {
        super(name, new ArrayList<>(), new ArrayList<>());
        this.function = function;
    }

    @Override
    public NodeResult run(Context context, List<BoatFunctionArgumentValue> values, Region region) {
        return new NodeResult(function.run(context, values, region));
    }
}
