package com.libertas.errors;

import com.libertas.generics.ConsoleColors;
import com.libertas.generics.Region;

import java.util.*;

public class ErrorLog {
    private List<BoatError> errors = new ArrayList<>();
    private final static ErrorLog instance = new ErrorLog();
    private String input = "";

    private ErrorLog() {

    }

    public static ErrorLog getInstance() {
        return instance;
    }
    
    public void registerError(BoatError error) {
        errors.add(error);
    }
    public void registerError(BoatError error, boolean process) {
        registerError(error);
        if (process) {
            process();
        }
    }
    public void registerInput(String input) {
        this.input = input;
    }

    public void process() {
        int numOfCritical = 0;
        int numOfWarnings = 0;

        for (BoatError error: errors) {
            if (error.type == ErrorType.CRITICAL) {
                numOfCritical++;
                System.out.println(ConsoleColors.RED_BACKGROUND + ConsoleColors.WHITE_BOLD + "A critical error occurred!" + ConsoleColors.RESET);
                System.out.println("| Name: " + ConsoleColors.WHITE_BOLD + error.name + ConsoleColors.RESET);
                System.out.println("| Message: " + error.message);
            } else if (error.type == ErrorType.WARNING) {
                numOfWarnings++;
                System.out.println(ConsoleColors.YELLOW_BRIGHT + "A warning occurred!" + ConsoleColors.RESET);
                System.out.println("| Name: " + ConsoleColors.WHITE_BOLD + error.name + ConsoleColors.RESET);
                System.out.println("| Message: " + error.message);
            }
            printTrace(error.region, error.type);
            System.out.println();
        }

        if (numOfCritical > 0) {
            System.exit(1);
        }
    }

    private void printTrace(Region region, ErrorType type) {
        String[] allLines = input.split("\n");

        if (allLines.length < region.lineEnd || region.isEmpty) return;

        if (region.lineStart == region.lineEnd) {
            String line = allLines[region.lineStart];

            StringBuilder underline = new StringBuilder();

            for (int i = 0; i <= region.charEnd; i++) {
                if (region.charStart <= i) {
                    underline.append("~");
                } else {
                    underline.append(" ");
                }
            }

            System.out.println("|\n" +
                    "| " + line + "\n" +
                    "| " + (type == ErrorType.CRITICAL ? ConsoleColors.RED : ConsoleColors.YELLOW_BRIGHT) + underline + ConsoleColors.RESET + "\n" +
                    "| \n" +
                    "| on line " + (region.lineStart + 1) + ", characters " + region.charStart + "-" + region.charEnd);
        }
    }

    public void clear() {
        errors.clear();
        input = "";
    }
}
