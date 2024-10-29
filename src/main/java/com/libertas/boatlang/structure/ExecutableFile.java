package com.libertas.boatlang.structure;

import com.libertas.boatlang.errors.BoatError;
import com.libertas.boatlang.errors.ErrorLog;
import com.libertas.boatlang.errors.ErrorType;
import com.libertas.boatlang.generics.Region;
import com.libertas.boatlang.generics.RunConfiguration;
import com.libertas.boatlang.generics.RunMode;
import com.libertas.boatlang.lexer.Lexer;
import com.libertas.boatlang.lexer.Token;
import com.libertas.boatlang.parser.Context;
import com.libertas.boatlang.parser.NodeResult;
import com.libertas.boatlang.parser.Parser;
import com.libertas.boatlang.parser.nodes.ProgramNode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class ExecutableFile {
    public String path;
    public ProgramNode program;

    public Context context;
    private String contents;

    public ExecutableFile(String path, Context context) {
        this.path = path;
        this.context = new Context(context);
        contents = read();
        ErrorLog.getInstance().registerInput(contents);
        process();
    }

    private String read() {
        try {
            Path path1 = Paths.get(path);

            return Files.readString(path1);
        } catch (IOException e) {
            ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "IOException", e.getMessage() + "\n" + e.getCause(), new Region()), true);
            return "";
        }
    }

    public void process() {
        Lexer lexer = new Lexer(contents);
        ArrayList<Token> tokens = lexer.tokenize();

//        StringJoiner joiner = new StringJoiner("\n");
//
//        for (Token token : tokens) {
//            joiner.add(token.toString());
//        }
//
//        System.out.println(joiner);

        Parser parser = new Parser(tokens);
        program = parser.parse();

        if (ErrorLog.getInstance().hasCriticalErrors) {
            ErrorLog.getInstance().process();
            System.exit(1);
        }

        //System.out.println(program);
    }

    public NodeResult run() {
        return program.get(context);
    }

    public Context getExportedContext() {
        RunConfiguration configuration = RunConfiguration.getInstance();

        RunMode previous = RunMode.values()[configuration.mode.ordinal()];

        configuration.mode = RunMode.IMPORT;

        program.get(context);

        configuration.mode = previous;

        return context;
    }
}
