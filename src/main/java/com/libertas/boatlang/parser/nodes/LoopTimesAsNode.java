package com.libertas.boatlang.parser.nodes;

import com.libertas.boatlang.generics.Region;
import com.libertas.boatlang.parser.Context;
import com.libertas.boatlang.parser.NodeResult;
import com.libertas.boatlang.variables.Barrel;
import com.libertas.boatlang.variables.None;
import org.apache.commons.math3.fraction.Fraction;

import java.util.List;

public class LoopTimesAsNode extends LoopNode {
    ArgumentNode times;
    String name;

    public LoopTimesAsNode(ArgumentNode times, String name, List<StatementNode> body, Region region) {
        super(body, region);
        this.times = times;
        this.name = name;
    }

    @Override
    public NodeResult get(Context context) {
        int value = ((Barrel) times.get(context).result.get(context)).value.intValue();

        for (int i = 0; i < value; i++) {
            context.setVariable(name, new Barrel(new Fraction(i)));

            NodeResult result = iter(iterations, context);

            if (result.shouldStop) return result;
        }

        return new NodeResult(new Barrel(new Fraction(iterations)));
    }

    @Override
    public NodeResult iter(int i, Context context) {
        super.iter(i, context);
        for (StatementNode statement : body) {
            NodeResult result = statement.get(context);

            if (result.shouldStop) return result;
        }

        return new NodeResult(new None());
    }

    @Override
    public String toString() {
        return "[LoopTimesNode: " + times + " times as " + name + " -> " + body + "]";
    }
}
