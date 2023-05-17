package com.libertas.parser.nodes;

import com.libertas.generics.Region;
import com.libertas.parser.Context;
import com.libertas.functions.BoatFunctionArgumentValue;
import com.libertas.parser.NodeResult;
import com.libertas.variables.Variable;

import java.util.ArrayList;
import java.util.List;

public class CommandNode extends StatementNode {
    public String name;
    public List<ArgumentNode> arguments;

    public CommandNode(String name, List<ArgumentNode> arguments, Region region) {
        super(region);
        this.arguments = arguments;
        this.name = name;
    }

    @Override
    public NodeResult get(Context context) {
        List<BoatFunctionArgumentValue> values = new ArrayList<>(arguments.stream().map(argumentNode -> new BoatFunctionArgumentValue(argumentNode.get(context).result, argumentNode.region)).toList());

        return new NodeResult(context.runFunction(name, values, region));
    }

    @Override
    public String toString() {
        return "[CommandNode: " + name + " " + arguments + "]";
    }
}
