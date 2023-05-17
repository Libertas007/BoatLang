package com.libertas.parser.nodes;

import com.libertas.generics.Region;
import com.libertas.parser.Context;
import com.libertas.parser.NodeResult;
import com.libertas.variables.Switch;
import com.libertas.variables.Variable;

import java.util.List;

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
