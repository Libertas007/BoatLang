package com.libertas.parser.nodes;

import com.libertas.generics.Region;
import com.libertas.functions.BoatFunction;
import com.libertas.parser.Context;
import com.libertas.parser.NodeResult;
import com.libertas.variables.None;

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
