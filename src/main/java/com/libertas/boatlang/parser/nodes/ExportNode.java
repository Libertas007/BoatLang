package com.libertas.boatlang.parser.nodes;

import com.libertas.boatlang.generics.Region;
import com.libertas.boatlang.parser.Context;
import com.libertas.boatlang.parser.NodeResult;
import com.libertas.boatlang.variables.None;

public class ExportNode extends DefinitionNode {
    public String toExport;

    public ExportNode(String toExport, Region region) {
        super(region);
        this.toExport = toExport;
    }

    @Override
    public NodeResult get(Context context) {
        context.makeExportable(toExport, region);

        return new NodeResult(new None());
    }

    @Override
    public String toString() {
        return "[ExportNode: " + toExport + "]";
    }
}
