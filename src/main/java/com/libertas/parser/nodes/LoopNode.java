package com.libertas.parser.nodes;

import com.libertas.generics.Region;
import com.libertas.parser.Context;
import com.libertas.parser.NodeResult;
import com.libertas.variables.None;
import com.libertas.variables.Variable;

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
