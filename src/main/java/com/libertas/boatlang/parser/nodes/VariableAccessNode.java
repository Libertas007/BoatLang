package com.libertas.boatlang.parser.nodes;

import com.libertas.boatlang.generics.Region;
import com.libertas.boatlang.parser.Context;
import com.libertas.boatlang.parser.NodeResult;

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
