package com.libertas.parser.nodes;

import com.libertas.generics.Region;
import com.libertas.parser.Context;
import com.libertas.parser.NodeResult;
import com.libertas.variables.Barrel;
import com.libertas.variables.None;
import com.libertas.variables.Variable;
import org.apache.commons.math3.fraction.Fraction;

import java.util.List;

public class LoopTimesNode extends LoopNode {
    ArgumentNode times;

    public LoopTimesNode(ArgumentNode times, List<StatementNode> body, Region region) {
        super(body, region);
        this.times = times;
    }

    @Override
    public NodeResult get(Context context) {
        int value = ((Barrel) times.get(context).result.get(context)).value.intValue();

        for (int i = 0; i < value; i++) {
            iter(i, context);
        }

        return new NodeResult(new Barrel(new Fraction(iterations)));
    }

    @Override
    public NodeResult iter(int i, Context context) {
        super.iter(i, context);
        for (StatementNode statement: body) {
            NodeResult result = statement.get(context);

            if (result.shouldStop) return result;
        }

        return new NodeResult(new None());
    }

    @Override
    public String toString() {
        return "[LoopTimesNode: " + times + " times -> " + body + "]";
    }
}
