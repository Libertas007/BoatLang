package com.libertas.boatlang.parser.nodes;

import com.libertas.boatlang.parser.Context;
import com.libertas.boatlang.parser.NodeResult;

public class FunctionReturnNode extends StatementNode {
    public StatementNode toReturn;

    public FunctionReturnNode(StatementNode toReturn) {
        super(toReturn.region);
        this.toReturn = toReturn;
    }

    @Override
    public NodeResult get(Context context) {
        return new NodeResult(toReturn.get(context).result, true);
    }

    @Override
    public String toString() {
        return "[FunctionReturnNode: " + toReturn + "]";
    }
}
