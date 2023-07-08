package com.libertas.boatlang.parser.nodes;

import com.libertas.boatlang.generics.Region;

public abstract class StatementNode extends Node {
    public StatementNode(Region region) {
        super(region);
    }
}
