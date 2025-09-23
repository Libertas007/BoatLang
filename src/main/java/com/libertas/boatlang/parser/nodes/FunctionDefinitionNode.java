package com.libertas.boatlang.parser.nodes;

import com.libertas.boatlang.errors.BoatError;
import com.libertas.boatlang.errors.ErrorLog;
import com.libertas.boatlang.errors.ErrorType;
import com.libertas.boatlang.functions.BoatFunction;
import com.libertas.boatlang.functions.BoatFunctionArgument;
import com.libertas.boatlang.generics.Region;
import com.libertas.boatlang.parser.Context;
import com.libertas.boatlang.parser.NodeResult;
import com.libertas.boatlang.variables.None;

public class FunctionDefinitionNode extends DefinitionNode {
    BoatFunction function;

    public FunctionDefinitionNode(BoatFunction function, Region region) {
        super(region);
        this.function = function;
    }

    @Override
    public NodeResult get(Context context) {
        for (BoatFunctionArgument argument : function.arguments) {
            if (!argument.type().equals("ANY") && !context.existsType(argument.type())) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "UnknownType", "The type '" + argument.type() + "' does not exist.", argument.region()));
            }
        }

        context.setFunction(function.name, function);

        return new NodeResult(new None());
    }

    @Override
    public String toString() {
        return "[FunctionDefinitionNode: " + function.name + "(" + function.arguments + ") -> " + function.body + "]";
    }
}
