package com.libertas.parser.nodes;

import com.libertas.generics.Region;
import com.libertas.parser.Context;
import com.libertas.parser.NodeResult;
import com.libertas.variables.None;
import com.libertas.variables.Switch;
import com.libertas.variables.Variable;

import java.util.List;

public class IfNode extends StatementNode {
    public ConditionNode condition;
    public List<StatementNode> ifTrue;
    public List<StatementNode> ifFalse;

    public IfNode(ConditionNode condition, List<StatementNode> ifTrue, List<StatementNode> ifFalse, Region region) {
        super(region);
        this.condition = condition;
        this.ifTrue = ifTrue;
        this.ifFalse = ifFalse;
    }

    @Override
    public NodeResult get(Context context) {
        if ((boolean)condition.get(context).result.get(context).value) {
            for (StatementNode node : ifTrue) {
                NodeResult result = node.get(context);

                if (result.shouldStop) return result;
            }
            return new NodeResult(new Switch(true));
        }

        ifFalse.forEach(node -> node.get(context));
        return new NodeResult(new Switch(false));
    }

    @Override
    public String toString() {
        return "[IfNode: if " + condition + " -> " + ifTrue + ", else -> " + ifFalse + "]";
    }
}
