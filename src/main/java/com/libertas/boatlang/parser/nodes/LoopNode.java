package com.libertas.boatlang.parser.nodes;

import com.libertas.boatlang.generics.Region;
import com.libertas.boatlang.parser.Context;
import com.libertas.boatlang.parser.NodeResult;
import com.libertas.boatlang.variables.None;

import java.util.List;

public abstract class LoopNode extends StatementNode {
    public List<StatementNode> body;
    protected int iterations;

    public LoopNode(List<StatementNode> body, Region region) {
        super(region);
        this.body = body;
    }

    public NodeResult iter(int i, Context context) {
        iterations++;
        return new NodeResult(new None());
    }

    @Override
    public NodeResult get(Context context) {
        return super.get(context);
    }

    @Override
    public String toString() {
        return "[LoopNode: " + body + "]";
    }
}
