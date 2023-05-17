package com.libertas.parser.nodes;

import com.libertas.generics.Region;
import com.libertas.parser.Context;
import com.libertas.parser.NodeResult;
import com.libertas.variables.None;
import com.libertas.variables.Variable;

public abstract class Node {
    public Region region;

    public Node(Region region) {
        this.region = region;
    }
    public NodeResult get(Context context) {
        return new NodeResult(new None());
    }


}
