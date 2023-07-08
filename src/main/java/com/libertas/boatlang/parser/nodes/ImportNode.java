package com.libertas.boatlang.parser.nodes;

import com.libertas.boatlang.generics.Region;
import com.libertas.boatlang.libraries.math.MathLibrary;
import com.libertas.boatlang.parser.Context;
import com.libertas.boatlang.parser.NodeResult;
import com.libertas.boatlang.variables.Switch;
import com.libertas.boatlang.variables.Variable;

public class ImportNode extends DefinitionNode {
    public Variable toImport;

    public ImportNode(Variable toImport, Region region) {
        super(region);
        this.toImport = toImport;
    }

    @Override
    public NodeResult get(Context context) {

        if (toImport.get(context).value.equals("MATH")) {
            context.setVariable("MATH", new MathLibrary());
        }

        return new NodeResult(new Switch(true));
    }

    @Override
    public String toString() {
        return "[ImportNode: " + toImport + "]";
    }
}
