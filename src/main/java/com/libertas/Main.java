package com.libertas;

import com.libertas.errors.BoatError;
import com.libertas.errors.ErrorLog;
import com.libertas.errors.ErrorType;
import com.libertas.generics.Region;
import com.libertas.libraries.std.StandardLibrary;
import com.libertas.parser.Context;
import com.libertas.parser.Parser;
import com.libertas.parser.nodes.ProgramNode;
import com.libertas.structure.ExecutableFile;
import com.libertas.variables.Barrel;
import com.libertas.lexer.*;
import org.apache.commons.math3.fraction.Fraction;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringJoiner;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            terminal();
            return;
        }

        if (args[0].equals("run")) {
            // "C:\Users\adams\Documents\Projekty\BoatLang\examples\example.boat"

            Context globalContext = new Context();

            StandardLibrary stdLibrary = new StandardLibrary();
            globalContext.loadContext(stdLibrary.asContext());

            ExecutableFile file = new ExecutableFile(args[1], globalContext);

            file.run();
        }
    }

    public static void terminal() {
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

            ErrorLog log = ErrorLog.getInstance();
            log.clear();
            log.registerInput(command);

            Lexer lexer = new Lexer(command);
            ArrayList<Token> tokens = lexer.tokenize();

            StringJoiner joiner = new StringJoiner("\n");

            for (Token token : tokens) {
                joiner.add(token.toString());
            }

            System.out.println(joiner);

            Context globalContext = new Context();
            globalContext.setVariable("test", new Barrel(new Fraction(69)));

            StandardLibrary stdLibrary = new StandardLibrary();
            System.out.println(stdLibrary.functions);
            globalContext.loadContext(stdLibrary.asContext());

            System.out.println(globalContext.getFunctions());
            System.out.println(globalContext.getVariables());

            Parser parser = new Parser(tokens);
            ProgramNode result = parser.parse();
            System.out.println(result);
            System.out.println(result.get(globalContext));

            log.process();
        }
    }
}