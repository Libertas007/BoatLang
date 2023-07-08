package com.libertas.boatlang.parser.nodes;

import com.libertas.boatlang.generics.Region;
import com.libertas.boatlang.parser.Context;
import com.libertas.boatlang.parser.NodeResult;
import com.libertas.boatlang.variables.None;

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
