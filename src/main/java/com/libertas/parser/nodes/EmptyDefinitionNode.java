package com.libertas.parser.nodes;

import com.libertas.generics.Region;

public class EmptyDefinitionNode extends DefinitionNode {

    public EmptyDefinitionNode(Region region) {
        super(region);
    }

    @Override
    public String toString() {
        return "[EmptyDefinitionNode]";
    }
}
