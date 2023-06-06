package com.libertas.parser.nodes;

import com.libertas.errors.ErrorType;
import com.libertas.generics.ConsoleColors;
import com.libertas.generics.Region;
import com.libertas.parser.Context;
import com.libertas.parser.NodeResult;
import com.libertas.variables.None;
import com.libertas.variables.Variable;

import static com.libertas.Main.input;

public abstract class Node {
    public Region region;

    public Node(Region region) {
        this.region = region;
    }
    public NodeResult get(Context context) {
        return new NodeResult(new None());
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

            return "|\n" +
                    "| " + line + "\n" +
                    "| "  + underline + "\n" +
                    "| \n" +
                    "| on line " + (region.lineStart + 1) + ", characters " + region.charStart + "-" + region.charEnd;
        }

        return "";
    }
}
