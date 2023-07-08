package com.libertas.boatlang.structure;

import com.libertas.boatlang.errors.BoatError;
import com.libertas.boatlang.errors.ErrorLog;
import com.libertas.boatlang.errors.ErrorType;
import com.libertas.boatlang.generics.Region;
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
import java.util.StringJoiner;

public class ExecutableFile {
    public String path;
    public ProgramNode program;

    public Context context;
    private String contents;

    public ExecutableFile(String path, Context context) {
        this.path = path;
        this.context = new Context(context);
        contents = read();
        process();
    }

    private String read() {
        try {
            Path path1 = Paths.get(path);

            return Files.readString(path1);
        } catch (IOException e) {
            ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "IOException", e.getMessage(), new Region()), true);
            return "";
        }
    }

    public void process() {
        Lexer lexer = new Lexer(contents);
        ArrayList<Token> tokens = lexer.tokenize();

        StringJoiner joiner = new StringJoiner("\n");

        for (Token token : tokens) {
            joiner.add(token.toString());
        }

        // System.out.println(joiner);

        Parser parser = new Parser(tokens);
        program = parser.parse();
    }

    public NodeResult run() {
        return program.get(context);
    }
}
