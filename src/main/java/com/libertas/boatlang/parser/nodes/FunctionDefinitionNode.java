package com.libertas.boatlang.parser.nodes;

import com.libertas.boatlang.functions.BoatFunction;
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
        context.setFunction(function.name, function);

        return new NodeResult(new None());
    }

    @Override
    public String toString() {
        return "[FunctionDefinitionNode: " + function.name + "(" + function.arguments + ") -> " + function.body + "]";
    }
}
