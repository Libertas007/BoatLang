package com.libertas.structure;

import com.libertas.errors.BoatError;
import com.libertas.errors.ErrorLog;
import com.libertas.errors.ErrorType;
import com.libertas.generics.Region;
import com.libertas.lexer.Lexer;
import com.libertas.lexer.Token;
import com.libertas.parser.Context;
import com.libertas.parser.NodeResult;
import com.libertas.parser.Parser;
import com.libertas.parser.nodes.Node;
import com.libertas.parser.nodes.ProgramNode;
import com.libertas.variables.Variable;

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

        System.out.println(joiner);

        Parser parser = new Parser(tokens);
        program = parser.parse();
    }

    public NodeResult run() {
        return program.get(context);
    }
}
