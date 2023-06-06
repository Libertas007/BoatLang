package com.libertas.lexer;

import com.libertas.generics.Region;
import com.libertas.variables.Variable;

import static com.libertas.Main.input;

public class Token {
    private final TokenType type;
    private final Variable value;
    private final Region region;

    public Token(TokenType type, Variable value, Region region) {
        this.type = type;
        this.value = value;
        this.region = region;
    }

    public TokenType getType() {
        return type;
    }

    public Variable getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Token{" +
                "type=" + type +
                ", value='" + value + '\'' +
                ", region=" + region +
                '}';
    }

    public Region getRegion() {
        return region;
    }

    protected String representRegion() {
        String[] allLines = input.split("\n");

        if (allLines.length < region.lineEnd || region.isEmpty) return "";

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

            return  "| " + type + ": " + value + "\n" +
                    "|\n" +
                    "| " + line + "\n" +
                    "| "  + underline + "\n" +
                    "| \n" +
                    "| on line " + (region.lineStart + 1) + ", characters " + region.charStart + "-" + region.charEnd;
        }

        return "";
    }

    public String analysisRepresentation() {
        return "TOKEN::" + type + "::" + region.analysisRepresentation();
    }
}
