package com.libertas.boatlang.errors;

import com.libertas.boatlang.generics.ConsoleColors;
import com.libertas.boatlang.generics.Region;
import com.libertas.boatlang.generics.RunConfiguration;
import com.libertas.boatlang.generics.RunMode;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class ErrorLog {
    private List<BoatError> errors = new ArrayList<>();
    private final static ErrorLog instance = new ErrorLog();
    private String input = "";

    public boolean hasCriticalErrors = false;

    private ErrorLog() {

    }

    public static ErrorLog getInstance() {
        return instance;
    }

    public void registerError(BoatError error) {
        errors.add(error);
        if (error.type == ErrorType.CRITICAL) hasCriticalErrors = true;
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

        if (RunConfiguration.getInstance().mode == RunMode.ANALYZE) {
            System.out.println(getAnalysisReport());
            System.exit(0);
            return;
        }

        for (BoatError error : errors) {
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

    public String getAnalysisReport() {
        StringJoiner joiner = new StringJoiner("\n");

        for (BoatError error : errors) {
            if (error.type == ErrorType.CRITICAL) {
                joiner.add("ERROR::CRITICAL::" + error.name + "::" + error.message + "::" + error.region.analysisRepresentation());
            } else if (error.type == ErrorType.WARNING) {
                joiner.add("ERROR::WARNING::" + error.name + "::" + error.message + "::" + error.region.analysisRepresentation());
            }
        }


        return joiner.toString();
    }

    private void printTrace(Region region, ErrorType type) {
        String[] allLines = input.split("\n");

        if (allLines.length < region.lineEnd || region.isEmpty) return;

        if (region.lineStart == region.lineEnd) {
            String line = allLines[region.lineStart - 1];

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
                    "| on line " + (region.lineStart) + ", characters " + region.charStart + "-" + region.charEnd);
        }
    }

    public void clear() {
        errors.clear();
        input = "";
    }
}
