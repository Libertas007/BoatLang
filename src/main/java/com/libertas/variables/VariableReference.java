package com.libertas.variables;

import com.libertas.parser.Context;
import com.libertas.parser.NodeResult;
import com.libertas.parser.nodes.Node;

public class VariableReference extends Variable {
    public Variable value;
    public String name;
    public Node node;

    public VariableReference(String name, Variable value) {
        super(value, value.displayName);
        this.name = name;
        this.value = value;

        implementations.addAll(value.implementations);
    }

    public VariableReference(String name, Node node) {
        super(new None(), "REFERENCE");
        this.name = name;
        this.value = new None();
        this.node = node;

        implementations.addAll(value.implementations);
    }

    @Override
    public Variable get(Context context) {
        if (node == null) return value;

        Variable variable = node.get(context).result;

        while (variable instanceof VariableReference) {
            variable = variable.get(context);
        }

        value = variable;
        displayName = variable.displayName;
        implementations.addAll(variable.implementations);
        return node.get(context).result;
    }
}
