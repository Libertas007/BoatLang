package com.libertas.boatlang.parser.nodes;

import com.libertas.boatlang.generics.Region;
import com.libertas.boatlang.parser.Context;
import com.libertas.boatlang.parser.NodeResult;

import java.util.List;
import java.util.StringJoiner;

public class ProgramNode extends Node {
    private List<Node> nodes;

    public ProgramNode(List<Node> nodes) {
        super(new Region());
        this.nodes = nodes;
    }

    @Override
    public NodeResult get(Context context) {
        for (Node node : nodes) {
            node.get(context);
        }
        return nodes.get(nodes.size() - 1).get(context);
    }

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner("\n\t");

        joiner.add("");
        joiner.add("NODES:");

        for (Node node : nodes) {
            joiner.add(node.toString());
        }

        return "[ProgramNode:" + joiner + "\n]";
    }
}
