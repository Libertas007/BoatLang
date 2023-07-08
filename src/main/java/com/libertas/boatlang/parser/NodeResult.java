package com.libertas.boatlang.parser;

import com.libertas.boatlang.variables.Variable;

public class NodeResult {
    public Variable result;
    public boolean shouldStop;

    public NodeResult(Variable result) {
        this.result = result;
        shouldStop = false;
    }

    public NodeResult(Variable result, boolean shouldStop) {
        this.result = result;
        this.shouldStop = shouldStop;
    }

    @Override
    public String toString() {
        return "RES: " + result + ", stop?: " + shouldStop;
    }
}
