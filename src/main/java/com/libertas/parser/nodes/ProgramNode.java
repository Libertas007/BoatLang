package com.libertas.parser.nodes;

import com.libertas.generics.Region;
import com.libertas.lexer.Token;
import com.libertas.parser.Context;
import com.libertas.parser.NodeResult;
import com.libertas.variables.Variable;

import java.util.List;
import java.util.StringJoiner;

public class ProgramNode extends Node {
    private List<DefinitionNode> definitions;
    private List<StatementNode> statements;

    public ProgramNode(List<DefinitionNode> definitions, List<StatementNode> statements) {
        super(new Region());
        this.definitions = definitions;
        this.statements = statements;
    }

    @Override
    public NodeResult get(Context context) {
        for (DefinitionNode definition : definitions) {
            definition.get(context);
        }
        for (StatementNode statement : statements) {
            statement.get(context);
        }
        return statements.get(statements.size() - 1).get(context);
    }

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner("\n\t");

        joiner.add("");
        joiner.add("DEFINITIONS:");

        for (DefinitionNode definitionNode : definitions) {
            joiner.add(definitionNode.toString());
        }

        joiner.add("STATEMENTS:");

        for (StatementNode statementNode : statements) {
            joiner.add(statementNode.toString());
        }

        return "[ProgramNode:" + joiner + "\n]";
    }
}
