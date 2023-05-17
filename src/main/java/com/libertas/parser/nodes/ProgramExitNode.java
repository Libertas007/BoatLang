package com.libertas.parser.nodes;

import com.libertas.generics.Region;
import com.libertas.parser.Context;
import com.libertas.parser.NodeResult;
import com.libertas.variables.None;
import com.libertas.variables.Variable;

public class ProgramExitNode extends StatementNode {
    public int status;

    public ProgramExitNode(int status, Region region) {
        super(region);
        this.status = status;
    }

    @Override
    public NodeResult get(Context context) {
        System.exit(status);
        return new NodeResult(new None());
    }
}
