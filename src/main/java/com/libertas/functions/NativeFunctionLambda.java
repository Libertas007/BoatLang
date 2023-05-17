package com.libertas.functions;

import com.libertas.generics.Region;
import com.libertas.parser.Context;
import com.libertas.variables.Variable;

import java.util.List;

public interface NativeFunctionLambda {
    Variable run(Context context, List<BoatFunctionArgumentValue> arguments, Region region);
}
