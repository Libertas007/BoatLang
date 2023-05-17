package com.libertas.parser.nodes;

import com.libertas.generics.Region;
import com.libertas.libraries.math.MathLibrary;
import com.libertas.parser.Context;
import com.libertas.parser.NodeResult;
import com.libertas.variables.Switch;
import com.libertas.variables.Variable;

public class ImportNode extends DefinitionNode {
    public Variable toImport;

    public ImportNode(Variable toImport, Region region) {
        super(region);
        this.toImport = toImport;
    }

    @Override
    public NodeResult get(Context context) {

        if (toImport.get(context).value.equals("MATH")) {
            context.loadContext(new MathLibrary().asContext());
        }

        return new NodeResult(new Switch(true));
    }

    @Override
    public String toString() {
        return "[ImportNode: " + toImport + "]";
    }
}
