package com.libertas.parser.nodes;

import com.libertas.generics.Region;
import com.libertas.parser.Context;
import com.libertas.parser.NodeResult;
import com.libertas.variables.Variable;

public class VariableAccessNode extends StatementNode {
    private String name;

    public VariableAccessNode(String name, Region region) {
        super(region);
        this.name = name;
    }

    @Override
    public NodeResult get(Context context) {
        return new NodeResult(context.getVariable(name, region));
    }

    @Override
    public String toString() {
        return "[VariableAccessNode: " + name + "]";
    }
}
