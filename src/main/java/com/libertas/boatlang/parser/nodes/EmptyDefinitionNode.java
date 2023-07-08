package com.libertas.boatlang.parser.nodes;

import com.libertas.boatlang.generics.Region;

public class EmptyDefinitionNode extends DefinitionNode {

    public EmptyDefinitionNode(Region region) {
        super(region);
    }

    @Override
    public String toString() {
        return "[EmptyDefinitionNode]";
    }
}
