package com.libertas.boatlang.functions;

import com.libertas.boatlang.generics.Region;
import com.libertas.boatlang.parser.Context;
import com.libertas.boatlang.parser.NodeResult;

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
