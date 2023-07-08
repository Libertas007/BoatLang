package com.libertas.boatlang.parser.nodes;

import com.libertas.boatlang.generics.Region;

public class EmptyStatementNode extends StatementNode {
    public EmptyStatementNode(Region region) {
        super(region);
    }

    @Override
    public String toString() {
        return "[EmptyStatementNode]";
    }
}
