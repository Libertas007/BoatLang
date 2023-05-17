package com.libertas.functions;

import com.libertas.errors.BoatError;
import com.libertas.errors.ErrorLog;
import com.libertas.errors.ErrorType;
import com.libertas.generics.Region;
import com.libertas.parser.Context;
import com.libertas.parser.NodeResult;
import com.libertas.parser.nodes.FunctionReturnNode;
import com.libertas.parser.nodes.StatementNode;
import com.libertas.variables.None;
import com.libertas.variables.Variable;

import java.util.List;

public class BoatFunction {

    public String name;
    public List<StatementNode> body;
    public List<BoatFunctionArgument> arguments;

    public BoatFunction(String name, List<BoatFunctionArgument> arguments, List<StatementNode> body) {
        this.name = name;
        this.arguments = arguments;
        this.body = body;
    }

    public NodeResult run(Context context, List<BoatFunctionArgumentValue> values, Region region) {
        Context newContext = new Context(context);

        if (!doArgumentsMatch(values)) {
            return new NodeResult(new None());
        }

        for (int i = 0; i < arguments.size(); i++) {
            BoatFunctionArgument argument = arguments.get(i);
            Variable value = values.get(i).value();

            newContext.setVariable(argument.name(), value);
        }

        NodeResult last = new NodeResult(new None());

        for (StatementNode statement : body) {
            if (statement instanceof FunctionReturnNode) {
                return statement.get(newContext);
            }
            last = statement.get(newContext);
        }

        return last;
    }

    private boolean doArgumentsMatch(List<BoatFunctionArgumentValue> provided) {
        if (provided.size() != arguments.size()) {
            ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidFunctionSignature", "Expected " + arguments.size() + " parameters, got " + provided.size() + ".", new Region()));
            return false;
        }

        List<Variable> values = provided.stream().map(BoatFunctionArgumentValue::value).toList();

        for (int i = 0; i < values.size(); i++) {
            Variable value = values.get(i);
            BoatFunctionArgument argument = arguments.get(i);

            if (!value.displayName.equals(argument.type())) {
                // TODO: ADD REGION SUPPORT
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidFunctionSignature", "Type '" + value.displayName + "' does not match the type of '" + argument.type() + "'.", new Region()));
                return false;
            }
        }

        return true;
    }
}
