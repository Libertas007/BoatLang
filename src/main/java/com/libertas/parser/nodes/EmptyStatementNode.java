package com.libertas.parser.nodes;

import com.libertas.generics.Region;

public class EmptyStatementNode extends StatementNode {
    public EmptyStatementNode(Region region) {
        super(region);
    }

    @Override
    public String toString() {
        return "[EmptyStatementNode]";
    }
}
