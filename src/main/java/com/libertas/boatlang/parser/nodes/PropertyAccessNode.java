package com.libertas.boatlang.parser.nodes;

import com.libertas.boatlang.generics.Region;
import com.libertas.boatlang.parser.Context;
import com.libertas.boatlang.parser.NodeResult;
import com.libertas.boatlang.variables.Variable;

public class PropertyAccessNode extends StatementNode {
    public ArgumentNode root;
    public String property;


    public PropertyAccessNode(ArgumentNode root, String property, Region region) {
        super(region);
        this.root = root;
        this.property = property;
    }

    @Override
    public NodeResult get(Context context) {
        Variable variable = root.get(context).result.get(context).getProperty(property);

        return new NodeResult(variable);
    }

    @Override
    public String toString() {
        return "[PropertyAccessNode: " + root + "." + property + "]";
    }
}
