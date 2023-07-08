package com.libertas.boatlang.parser.nodes;

import com.libertas.boatlang.generics.Region;
import com.libertas.boatlang.parser.Context;
import com.libertas.boatlang.parser.NodeResult;
import com.libertas.boatlang.variables.Variable;

public class ArgumentNode extends Node {
    private Variable value;
    private StatementNode node;

    public ArgumentNode(Variable value, Region region) {
        super(region);
        this.value = value;
    }

    public ArgumentNode(StatementNode node) {
        super(node.region);
        this.node = node;
    }

    @Override
    public NodeResult get(Context context) {
        if (value != null) return new NodeResult(value);
        return node.get(context);
    }

    @Override
    public String toString() {
        if (value != null) return "[ArgumentNode: " + value + "]";
        return "[ArgumentNode: " + node + "]";
    }
}
