package com.libertas.parser.nodes;

import com.libertas.errors.BoatError;
import com.libertas.errors.ErrorLog;
import com.libertas.errors.ErrorType;
import com.libertas.parser.Context;
import com.libertas.parser.NodeResult;
import com.libertas.variables.*;

public class ConditionNode extends Node {
    public ArgumentNode first;
    public String operator;
    public ArgumentNode second;

    public ConditionNode(ArgumentNode first, String operator, ArgumentNode second) {
        super(first.region.combine(second.region));
        this.first = first;
        this.operator = operator;
        this.second = second;
    }

    @Override
    public NodeResult get(Context context) {
        Variable firstValue = first.get(context).result.get(context);
        Variable secondValue = second.get(context).result.get(context);

        if (operator.equals("==")) {
            return new NodeResult(new Switch(firstValue.get(context).value.equals(secondValue.get(context).value)));
        }

        if (operator.equals("!=")) {
            return new NodeResult(new Switch(!firstValue.get(context).value.equals(secondValue.get(context).value)));
        }

        if (!(firstValue instanceof Barrel) || !(secondValue instanceof Barrel)) {
            ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "MismatchedTypes", "Cannot compare '" + firstValue.displayName + "' and '" + secondValue.displayName + "' using " + operator + " operator.", region), true);
        }

        Double firstDouble = (Double) firstValue.get(context).value;
        Double secondDouble = (Double) secondValue.get(context).value;

        if (operator.equals("<")) {
            return new NodeResult(new Switch(firstDouble < secondDouble));
        }

        if (operator.equals(">")) {
            return new NodeResult(new Switch(firstDouble > secondDouble));
        }

        if (operator.equals("<=")) {
            return new NodeResult(new Switch(firstDouble <= secondDouble));
        }

        if (operator.equals(">=")) {
            return new NodeResult(new Switch(firstDouble >= secondDouble));
        }

        return new NodeResult(new None());
    }

    @Override
    public String toString() {
        return "[ConditionNode: " + first + " " + operator + " " + second + "]";
    }
}
