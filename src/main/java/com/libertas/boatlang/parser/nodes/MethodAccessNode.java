package com.libertas.boatlang.parser.nodes;

import com.libertas.boatlang.functions.BoatFunctionArgumentValue;
import com.libertas.boatlang.generics.Region;
import com.libertas.boatlang.parser.Context;
import com.libertas.boatlang.parser.NodeResult;
import com.libertas.boatlang.variables.Variable;

import java.util.ArrayList;
import java.util.List;

public class MethodAccessNode extends StatementNode {
    public ArgumentNode root;
    public String method;
    public List<ArgumentNode> arguments;


    public MethodAccessNode(ArgumentNode root, String method, List<ArgumentNode> arguments, Region region) {
        super(region);
        this.root = root;
        this.method = method;
        this.arguments = arguments;
    }

    @Override
    public NodeResult get(Context context) {
        Variable variable = root.get(context).result.get(context);

        List<BoatFunctionArgumentValue> values = new ArrayList<>(arguments.stream().map(argumentNode -> new BoatFunctionArgumentValue(argumentNode.get(context).result, argumentNode.region)).toList());

        return new NodeResult(variable.runMethod(method, values, region, context));
    }

    @Override
    public String toString() {
        return "[MethodAccessNode: " + root + "." + method + " " + arguments + "]";
    }
}
