package com.libertas.boatlang.parser.nodes;

import com.libertas.boatlang.generics.Region;
import com.libertas.boatlang.parser.Context;
import com.libertas.boatlang.parser.NodeResult;
import com.libertas.boatlang.variables.BoatList;
import com.libertas.boatlang.variables.Variable;

import java.util.List;

public class ListLiteralNode extends StatementNode {
    public List<ArgumentNode> nodes;

    public ListLiteralNode(List<ArgumentNode> nodes, Region region) {
        super(region);
        this.nodes = nodes;
    }

    @Override
    public NodeResult get(Context context) {
        List<Variable> list = nodes.stream().map(argumentNode -> argumentNode.get(context).result).toList();

        return new NodeResult(new BoatList(list));
    }

    @Override
    public String toString() {
        return "[ListLiteralNode: " + nodes + "]";
    }
}
