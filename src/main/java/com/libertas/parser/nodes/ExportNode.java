package com.libertas.parser.nodes;

import com.libertas.generics.Region;
import com.libertas.parser.Context;
import com.libertas.parser.NodeResult;
import com.libertas.variables.None;

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
