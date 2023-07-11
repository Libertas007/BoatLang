package com.libertas.boatlang.parser.nodes;

import com.libertas.boatlang.errors.BoatError;
import com.libertas.boatlang.errors.ErrorLog;
import com.libertas.boatlang.errors.ErrorType;
import com.libertas.boatlang.generics.Region;
import com.libertas.boatlang.libraries.math.MathLibrary;
import com.libertas.boatlang.libraries.std.StandardLibrary;
import com.libertas.boatlang.parser.Context;
import com.libertas.boatlang.parser.NodeResult;
import com.libertas.boatlang.structure.ExecutableFile;
import com.libertas.boatlang.variables.Switch;
import com.libertas.boatlang.variables.Variable;

import java.io.File;

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

        String path = toImport.get(context).value.toString();
        File file = new File(path);

        if (!file.exists()) {
            ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "FileNotFound", "The file '" + path + "' cannot be found", region), true);
        }

        Context childContext = new Context();

        StandardLibrary stdLibrary = new StandardLibrary();
        childContext.loadContext(stdLibrary.asContext());

        ExecutableFile executableFile = new ExecutableFile(path, childContext);

        context.loadContext(executableFile.getExportedContext());

        return new NodeResult(new Switch(true));
    }

    @Override
    public String toString() {
        return "[ImportNode: " + toImport + "]";
    }
}
