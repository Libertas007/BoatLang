package com.libertas.boatlang.parser.nodes;

import com.libertas.boatlang.generics.Region;
import com.libertas.boatlang.libraries.math.MathSet;
import com.libertas.boatlang.parser.Context;
import com.libertas.boatlang.parser.NodeResult;
import com.libertas.boatlang.variables.Variable;

import java.util.HashSet;
import java.util.stream.Collectors;

public class SetLiteralNode extends StatementNode {
    public HashSet<ArgumentNode> nodes;

    public SetLiteralNode(HashSet<ArgumentNode> nodes, Region region) {
        super(region);
        this.nodes = nodes;
    }

    @Override
    public NodeResult get(Context context) {
        HashSet<Variable> set = nodes.stream().map(node -> node.get(context).result).collect(Collectors.toCollection(HashSet::new));

        return new NodeResult(new MathSet(set));
    }

    @Override
    public String toString() {
        return "[SetLiteralNode: " + nodes + "]";
    }
}
