package com.libertas.parser.nodes;

import com.libertas.generics.Region;
import com.libertas.parser.Context;
import com.libertas.parser.NodeResult;
import com.libertas.variables.Variable;

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
