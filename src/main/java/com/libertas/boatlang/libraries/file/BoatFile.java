package com.libertas.boatlang.libraries.file;

import com.libertas.boatlang.errors.BoatError;
import com.libertas.boatlang.errors.ErrorLog;
import com.libertas.boatlang.errors.ErrorType;
import com.libertas.boatlang.functions.Method;
import com.libertas.boatlang.generics.Region;
import com.libertas.boatlang.generics.RunConfiguration;
import com.libertas.boatlang.generics.RunMode;
import com.libertas.boatlang.parser.Context;
import com.libertas.boatlang.variables.None;
import com.libertas.boatlang.variables.Package;
import com.libertas.boatlang.variables.Variable;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class BoatFile extends Variable {
    File value;

    public BoatFile(String path) {
        super(new File(path), "FILE");
        value = new File(path);

        methods();
        properties();
    }

    private void properties() {
        setProperty("PATH", new Package(value.getPath()));
        setProperty("ABSOLUTE", new Package(value.getAbsolutePath()));
    }

    private void methods() {
        setMethod("READ", new Method("READ", ((context, self, arguments, region) -> {
            BoatFile me = (BoatFile) self;

            if (!me.value.exists()) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "FileNotFound", "The file '" + me.value.getPath() + "' cannot be found.", region), true);
                return new None();
            }

            if (!me.value.canRead()) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "MissingPermissions", "The file '" + me.value.getPath() + "' cannot be read.", region), true);
                return new None();
            }

            if (RunConfiguration.getInstance().mode == RunMode.ANALYZE) return new None();

            try {
                Path path = Paths.get(me.value.getPath());
                String contents = Files.readString(path);

                return new Package(contents);
            } catch (IOException e) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "IOException", e.getMessage(), new Region()), true);
                return new None();
            }
        })));

        setMethod("WRITE", new Method("WRITE", ((context, self, arguments, region) -> {
            BoatFile me = (BoatFile) self;

            if (arguments.size() != 1) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidFunctionSignature", "Expected 1 parameter, got " + arguments.size() + ".", region), true);
                return new None();
            }

            if (!me.value.canWrite()) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "MissingPermissions", "The file '" + me.value.getPath() + "' cannot be written to.", region), true);
                return new None();
            }

            if (RunConfiguration.getInstance().mode == RunMode.ANALYZE) return new None();

            try {
                FileWriter writer = new FileWriter(me.value);

                String toWrite = arguments.get(0).value().get(context).value.toString();

                writer.write(toWrite);

                writer.flush();
                writer.close();

                return new None();
            } catch (IOException e) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "IOException", e.getMessage(), new Region()), true);
                return new None();
            }
        })));

        setMethod("CLEAR", new Method("CLEAR", ((context, self, arguments, region) -> {
            BoatFile me = (BoatFile) self;

            if (!me.value.exists()) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "FileNotFound", "The file '" + me.value.getPath() + "' cannot be found.", region), true);
                return new None();
            }

            if (!me.value.canWrite()) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "MissingPermissions", "The file '" + me.value.getPath() + "' cannot be written to.", region), true);
                return new None();
            }

            if (RunConfiguration.getInstance().mode == RunMode.ANALYZE) return new None();

            try {
                FileWriter writer = new FileWriter(me.value);

                writer.write("");

                return new None();
            } catch (IOException e) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "IOException", e.getMessage(), new Region()), true);
                return new None();
            }
        })));
    }

    @Override
    public Variable implementRequest(Context context, Region region) {
        return new BoatFile("");
    }
}
