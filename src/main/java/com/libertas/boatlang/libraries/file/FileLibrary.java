package com.libertas.boatlang.libraries.file;

import com.libertas.boatlang.errors.BoatError;
import com.libertas.boatlang.errors.ErrorLog;
import com.libertas.boatlang.errors.ErrorType;
import com.libertas.boatlang.functions.Method;
import com.libertas.boatlang.libraries.Library;
import com.libertas.boatlang.variables.None;
import com.libertas.boatlang.variables.Package;

import java.io.File;

public class FileLibrary extends Library {
    public FileLibrary() {
        types.put("FILE", new BoatFile(""));

        setMethod("NEW", new Method("NEW", (context, self, arguments, region) -> {
            if (arguments.size() != 1) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidFunctionSignature", "Expected 1 parameter, got " + arguments.size() + ".", arguments.get(arguments.size() - 1).region()), true);
                return new None();
            }

            if (!(arguments.get(0).value().get(context) instanceof Package)) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "MismatchedTypes", "Expected PACKAGE, got " + arguments.get(0).value().get(context).displayName + ".", arguments.get(0).region()), true);
                return new None();
            }

            return new BoatFile(arguments.get(0).value().get(context).value.toString());
        }));

        setProperty("SEPARATOR", new Package(File.pathSeparator));
    }
}
