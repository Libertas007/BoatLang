package com.libertas.boatlang;

import com.libertas.boatlang.errors.BoatError;
import com.libertas.boatlang.errors.ErrorLog;
import com.libertas.boatlang.errors.ErrorType;
import com.libertas.boatlang.generics.Region;
import com.libertas.boatlang.generics.RunConfiguration;
import com.libertas.boatlang.generics.RunMode;
import com.libertas.boatlang.lexer.Lexer;
import com.libertas.boatlang.lexer.Token;
import com.libertas.boatlang.libraries.std.StandardLibrary;
import com.libertas.boatlang.parser.Context;
import com.libertas.boatlang.parser.Parser;
import com.libertas.boatlang.parser.nodes.ProgramNode;
import com.libertas.boatlang.structure.ExecutableFile;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringJoiner;

public class Main {
    public static String input = "";
    public static final String TEMPLATE = """
            SAIL ON YACHT
            
            BROADCAST "Hello world!"
            """;
    
    public static void main(String[] args) {
        if (args.length == 0) {
            terminal();
            return;
        }

        if (args[0].equals("run")) {

            RunConfiguration.getInstance().mode = RunMode.FILE;

            Context globalContext = new Context();

            StandardLibrary stdLibrary = new StandardLibrary();
            globalContext.loadContext(stdLibrary.asContext());

            ExecutableFile file = new ExecutableFile(args[1], globalContext);

            file.run();
            return;
        }

        if (args[0].equals("update")) {
            BoatUpdater.update();
            return;
        }

        if (args[0].equals("new") && args.length == 2) {
            try {
                create(args[1]);
            } catch (Exception e) {
                System.out.println("❌ We were interrupted! Don't do that to us!");
            }
            return;
        }

        if (args[0].equals("analyze") && args.length == 2) {
            analyze(args[1]);
            return;
        }

        printHelp();
    }

    public static void printHelp() {
        System.out.println("⛵ Boat " + BoatUpdater.getCurrentVersion());
        System.out.println("\nHow to use?\n");
        System.out.println("\uD83D\uDEF6 run a file: boat run <filename>");
        System.out.println("\uD83D\uDD0E analyze file: boat analyze <filename>");
        System.out.println("\uD83C\uDD95 create new file: boat new <filename>");
        System.out.println("\uD83E\uDD75 update Boat: boat update");
    }

    public static void analyze(String filename) {
        RunConfiguration.getInstance().mode = RunMode.ANALYZE;

        String contents = "";

        try {
            Path path = Paths.get(filename);
            contents = Files.readString(path);
        } catch (IOException e) {
            ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "IOException", e.getMessage(), new Region()), true);
        }

        input = contents;

        Lexer lexer = new Lexer(contents);
        ArrayList<Token> tokens = lexer.tokenize();

        StringJoiner joiner = new StringJoiner("\n");

        for (Token token : tokens) {
            joiner.add(token.analysisRepresentation());
        }

        System.out.println(joiner);

        if (ErrorLog.getInstance().hasCriticalErrors) {
            System.out.println(ErrorLog.getInstance().getAnalysisReport());
            System.exit(0);
        }

        Parser parser = new Parser(tokens);
        ProgramNode programNode = parser.parse();

        Context globalContext = new Context();

        StandardLibrary stdLibrary = new StandardLibrary();
        globalContext.loadContext(stdLibrary.asContext());

        programNode.get(globalContext);

        System.out.println(ErrorLog.getInstance().getAnalysisReport());
    }

    public static void create(String filename) throws InterruptedException, IOException {
        RunConfiguration.getInstance().mode = RunMode.CREATE;

        System.out.println("✨ Creating a brand new Boat file for you...");
        Thread.sleep(500);
        System.out.println("🏷️ It will be '" + filename + "'!");
        Thread.sleep(500);
        System.out.println("🧭 We are getting here...");

        FileWriter fileWriter = new FileWriter(filename);

        fileWriter.write(TEMPLATE);

        fileWriter.close();

        Thread.sleep(500);
        System.out.println("✅ And it's all done! You are ready to sail off!");
    }

    public static void terminal() {
        RunConfiguration.getInstance().mode = RunMode.TERMINAL;

        System.out.println("""
                          oooooo
                       _ooo
                       H
                |======H========|
                \\     BOAT     /
                 \\____________/
                ~~~~~~~~~~~~~~~~~
                """);
        System.out.println("Welcome to Boat terminal!\n\n");
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("Boat > ");

            String command = scanner.nextLine();

            if (command.equalsIgnoreCase("exit")) {
                break;
            }

            input = command;

            ErrorLog log = ErrorLog.getInstance();
            log.clear();
            log.registerInput(command);

            Lexer lexer = new Lexer(command);
            ArrayList<Token> tokens = lexer.tokenize();

//            StringJoiner joiner = new StringJoiner("\n");
//
//            for (Token token : tokens) {
//                joiner.add(token.toString());
//            }

            //System.out.println(joiner);

            Context globalContext = new Context();

            StandardLibrary stdLibrary = new StandardLibrary();
            // System.out.println(stdLibrary.functions);
            globalContext.loadContext(stdLibrary.asContext());

            // System.out.println(globalContext.getFunctions());
            // System.out.println(globalContext.getVariables());

            Parser parser = new Parser(tokens);
            ProgramNode result = parser.parse();
            //System.out.println(result);

            if (ErrorLog.getInstance().hasCriticalErrors) {
                ErrorLog.getInstance().process();
                System.exit(1);
            }

            System.out.println(result.get(globalContext).result.represent());
            //System.out.println(globalContext.getTypes());

            log.process();
        }
    }
}