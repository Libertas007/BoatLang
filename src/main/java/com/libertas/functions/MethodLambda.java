package com.libertas.functions;

import com.libertas.generics.Region;
import com.libertas.parser.Context;
import com.libertas.variables.Variable;

import java.util.List;

public interface MethodLambda {
    Variable run(Context context, Variable self, List<BoatFunctionArgumentValue> arguments, Region region);
}
