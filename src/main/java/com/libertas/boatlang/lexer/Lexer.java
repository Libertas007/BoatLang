package com.libertas.boatlang.lexer;

import com.libertas.boatlang.errors.BoatError;
import com.libertas.boatlang.errors.ErrorLog;
import com.libertas.boatlang.errors.ErrorType;
import com.libertas.boatlang.generics.Region;
import com.libertas.boatlang.variables.Barrel;
import com.libertas.boatlang.variables.None;
import com.libertas.boatlang.variables.Package;
import org.apache.commons.math3.fraction.Fraction;

import java.util.ArrayList;
import java.util.Arrays;

public class Lexer {

    private final String input;
    private int position;
    private int positionInLine = 0;
    private int line = 0;
    private final String[] keywords = {
            "IF",
            "ELSE",
            "LOOP",
            "AS",
            "TIMES",
            "DECLARE",
            "TYPE",
            "PUBLIC",
            "PRIVATE",
            "LOAD",
            "UNLOAD",
            "STATIC",
    };

    private final String[] argumentKeywords = {
            "TO",
            "BY",
            "FROM",
            "ON",
            "AT",
    };


    public Lexer(String input) {
        this.input = input;
        this.position = 0;
    }

    private void advance() {
        position++;
        positionInLine++;
    }

    private void advanceLine() {
        position++;
        positionInLine = 0;
        line++;
    }

    public ArrayList<Token> tokenize() {
        ArrayList<Token> tokens = new ArrayList<>();
        while (position < input.length()) {
            char currentChar = input.charAt(position);

            if (Character.isDigit(currentChar)) {
                tokens.add(readBarrel());
            } else if (Character.isLetter(currentChar)) {
                tokens.add(readIdentifier());
            } else if (currentChar == '"') {
                tokens.add(readPackage());
            } else if (currentChar == '\n') {
                tokens.add(new Token(TokenType.LINE_BREAK, new None(), new Region(line, positionInLine)));
                advanceLine();
            } else if (currentChar == ';') {
                tokens.add(new Token(TokenType.LINE_BREAK, new None(), new Region(line, positionInLine)));
                advance();
            } else if (currentChar == '(') {
                tokens.add(new Token(TokenType.RETURN_GROUP_START, new Package("("), new Region(line, positionInLine)));
                advance();
            } else if (currentChar == ')') {
                tokens.add(new Token(TokenType.RETURN_GROUP_END, new Package(")"), new Region(line, positionInLine)));
                advance();
            } else if (currentChar == ':') {
                tokens.add(new Token(TokenType.BLOCK_START, new Package(":"), new Region(line, positionInLine)));
                advance();
            } else if (currentChar == '.') {
                tokens.add(new Token(TokenType.METHOD_ACCESS, new Package("."), new Region(line, positionInLine)));
                advance();
            } else if (currentChar == '[') {
                tokens.add(new Token(TokenType.LIST_START, new Package("["), new Region(line, positionInLine)));
                advance();
            } else if (currentChar == ']') {
                tokens.add(new Token(TokenType.LIST_END, new Package("]"), new Region(line, positionInLine)));
                advance();
            } else if (currentChar == '{') {
                tokens.add(new Token(TokenType.SET_START, new Package("{"), new Region(line, positionInLine)));
                advance();
            } else if (currentChar == '}') {
                tokens.add(new Token(TokenType.SET_END, new Package("}"), new Region(line, positionInLine)));
                advance();
            } else if (currentChar == ',') {
                tokens.add(new Token(TokenType.SEPARATOR, new Package(","), new Region(line, positionInLine)));
                advance();
            } else if (currentChar == '/') {
                skipComment();
                advance();
            } else if (canBeInOperator(currentChar)) {
                tokens.add(readOperator());
                advance();
            } else if (Character.isWhitespace(currentChar)) {
                advance();
            } else {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "UnexpectedCharacter", "Unexpected character '" + currentChar + "'", new Region(line, positionInLine)));
                advance();
            }
        }
        tokens.add(new Token(TokenType.LINE_BREAK, new None(), new Region(line, positionInLine + 1)));
        tokens.add(new Token(TokenType.END_OF_FILE, new None(), new Region(line, positionInLine + 1)));
        return tokens;
    }

    private Token readBarrel() {
        StringBuilder builder = new StringBuilder();
        StringBuilder denomBuilder = new StringBuilder();
        boolean buildingDenom = false;
        int start = positionInLine;

        while (position < input.length()) {
            char currentChar = input.charAt(position);
            if (Character.isDigit(currentChar)) {
                if (buildingDenom) {
                    denomBuilder.append(currentChar);
                } else {
                    builder.append(currentChar);
                }
            } else if (currentChar == '/') {
                buildingDenom = true;
            } else {
                break;
            }
            advance();
        }

        if (buildingDenom) {
            return new Token(TokenType.BARREL, new Barrel(new Fraction(Integer.parseInt(builder.toString()), Integer.parseInt(denomBuilder.toString()))), new Region(line, line, start, positionInLine - 1));
        }

        return new Token(TokenType.BARREL, new Barrel(new Fraction(Integer.parseInt(builder.toString()))), new Region(line, line, start, positionInLine - 1));
    }

    private void skipComment() {
        while (position < input.length()) {
            char currentChar = input.charAt(position);
            if (currentChar == '\n') {
                break;
            }

            advance();
        }
    }

    private Token readPackage() {
        StringBuilder builder = new StringBuilder();
        int start = positionInLine;
        boolean escapeNext = false;
        advance();

        while (position < input.length()) {
            char currentChar = input.charAt(position);
            if (currentChar == '"' && !escapeNext) {
                break;
            }

            if (escapeNext) {
                if (currentChar == 'n') {
                    builder.append("\n");
                }

                if (currentChar == '\\') {
                    builder.append("\\");
                }
            }

            if (currentChar == '\\' && !escapeNext) {
                escapeNext = true;
            } else if (!escapeNext) {
                builder.append(currentChar);
            } else {
                escapeNext = false;
            }


            advance();

            if (position == input.length() || currentChar == '\n') {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "UnterminatedPackage", "Unterminated PACKAGE literal", new Region(line, line, start, positionInLine - 1)));
            }
        }

        advance();
        return new Token(TokenType.PACKAGE, new Package(builder.toString()), new Region(line, line, start, positionInLine - 1));
    }

    private Token readIdentifier() {
        StringBuilder builder = new StringBuilder();
        int start = positionInLine;
        while (position < input.length() && (Character.isLetterOrDigit(input.charAt(position)))) {
            builder.append(input.charAt(position));
            position++;
            positionInLine++;
        }

        String finalString = builder.toString();

        if (finalString.equals("END")) {
            return new Token(TokenType.BLOCK_END, new Package(finalString), new Region(line, line, start, positionInLine - 1));
        } else if (Arrays.asList(keywords).contains(finalString)) {
            return new Token(TokenType.KEYWORD, new Package(finalString), new Region(line, line, start, positionInLine - 1));
        } else if (Arrays.asList(argumentKeywords).contains(finalString)) {
            return new Token(TokenType.ARGUMENT_KEYWORD, new Package(finalString), new Region(line, line, start, positionInLine - 1));
        }

        return new Token(TokenType.IDENTIFIER, new Package(finalString), new Region(line, line, start, positionInLine - 1));
    }

    private Token readOperator() {
        StringBuilder builder = new StringBuilder();
        int start = position;
        while (position < input.length() && canBeInOperator(input.charAt(position)) && builder.length() < 2) {
            builder.append(input.charAt(position));
            position++;
            positionInLine++;
        }

        String finalOperator = builder.toString();

        if (finalOperator.equals("->")) {
            return new Token(TokenType.FUNCTION_RETURN, new None(), new Region(line, line, start, positionInLine - 1));
        }

        if (!isValidOperator(finalOperator)) {
            ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "UnexpectedToken", "Unexpected token '" + finalOperator + "'", new Region(line, line, start, positionInLine - 1)));
        }

        return new Token(TokenType.OPERATOR, new Package(builder.toString()), new Region(line, line, start, positionInLine - 1));
    }

    private boolean canBeInOperator(char c) {
        return c == '<' || c == '>' || c == '=' || c == '!' || c == '-';
    }

    private boolean isValidOperator(String op) {
        return op.equals("==") || op.equals("<=") || op.equals(">=") || op.equals("!=") || op.equals("!") || op.equals(">") || op.equals("<");
    }
}
