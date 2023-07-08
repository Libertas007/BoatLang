package com.libertas.boatlang.functions;

import com.libertas.boatlang.generics.Region;
import com.libertas.boatlang.parser.Context;
import com.libertas.boatlang.variables.Variable;

import java.util.List;

public interface NativeFunctionLambda {
    Variable run(Context context, List<BoatFunctionArgumentValue> arguments, Region region);
}
