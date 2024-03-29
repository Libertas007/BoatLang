package com.libertas.boatlang.parser;

import com.libertas.boatlang.errors.BoatError;
import com.libertas.boatlang.errors.ErrorLog;
import com.libertas.boatlang.errors.ErrorType;
import com.libertas.boatlang.functions.BoatFunction;
import com.libertas.boatlang.functions.BoatFunctionArgument;
import com.libertas.boatlang.generics.Region;
import com.libertas.boatlang.lexer.Token;
import com.libertas.boatlang.lexer.TokenType;
import com.libertas.boatlang.parser.nodes.*;
import com.libertas.boatlang.variables.None;
import com.libertas.boatlang.variables.Package;
import com.libertas.boatlang.variables.VariableReference;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Parser {
    private final ArrayList<Token> tokens;
    private Token currentToken;
    private int pointer = -1;

    public Parser(ArrayList<Token> tokens) {
        this.tokens = tokens;
        advance();
    }

    public ProgramNode parse() {
        List<Node> nodes = new ArrayList<>();

        boolean makeStatements = false;

        while (pointer < tokens.size()) {
            if (currentToken.getType() == TokenType.IDENTIFIER && currentToken.getValue().value.equals("SAIL")) {
                advance();
                if (currentToken.getType() == TokenType.ARGUMENT_KEYWORD && currentToken.getValue().value.equals("ON")) {
                    advance();
                    advance();
                    makeStatements = true;
                }
            }

            if (currentToken.getType() == TokenType.IDENTIFIER && currentToken.getValue().value.equals("ARRIVE")) {
                advance();
                if (currentToken.getType() == TokenType.ARGUMENT_KEYWORD && currentToken.getValue().value.equals("AT")) {
                    advance();
                    advance();
                    makeStatements = false;
                }
            }

            if (makeStatements) {
                nodes.add(makeStatement());
            } else {
                nodes.add(makeDefinition());
            }
        }

        return new ProgramNode(nodes);
    }

    private void advance() {
        pointer++;
        if (pointer < tokens.size()) {
            currentToken = tokens.get(pointer);
        }
    }

    private ArgumentNode makeArgument(boolean allowProperties) {
        if (currentToken.getType() == TokenType.BARREL || currentToken.getType() == TokenType.PACKAGE)
            return new ArgumentNode(currentToken.getValue(), currentToken.getRegion());
        if (currentToken.getType() == TokenType.ARGUMENT_KEYWORD)
            return new ArgumentNode(currentToken.getValue(), currentToken.getRegion());
        if (currentToken.getType() == TokenType.IDENTIFIER) {
            /*if (Arrays.stream(types).toList().contains(currentToken.getValue().value.toString())) {
                return new ArgumentNode(new TypeName(currentToken.getValue().value.toString()), currentToken.getRegion());
            }*/

            if (tokens.get(pointer + 1).getType() == TokenType.METHOD_ACCESS && allowProperties) {
                ArgumentNode root = new ArgumentNode(new VariableReference((String) currentToken.getValue().value, new VariableAccessNode((String) currentToken.getValue().value, currentToken.getRegion())), currentToken.getRegion());
                advance();
                advance();

                if (currentToken.getType() != TokenType.IDENTIFIER) {
                    ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidSyntax", "Expected an identifier.", currentToken.getRegion()), true);
                }

                String name = currentToken.getValue().value.toString();

                return new ArgumentNode(new PropertyAccessNode(root, name, root.region.combine(currentToken.getRegion())));
            }
            return new ArgumentNode(new VariableReference((String) currentToken.getValue().value, new VariableAccessNode((String) currentToken.getValue().value, currentToken.getRegion())), currentToken.getRegion());
        }
        if (currentToken.getType() == TokenType.RETURN_GROUP_START) {
            advance();
            StatementNode node = makeStatement();
            if (currentToken.getType() != TokenType.RETURN_GROUP_END) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidSyntax", "Expected ')'.", currentToken.getRegion()), true);
            }

            return new ArgumentNode(node);
        }
        if (currentToken.getType() == TokenType.LIST_START) {
            advance();
            List<ArgumentNode> argumentNodes = new ArrayList<>();
            argumentNodes.add(makeArgument(true));

            advance();
            while (currentToken.getType() != TokenType.LIST_END) {
                if (currentToken.getType() != TokenType.SEPARATOR) {
                    ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidSyntax", "Expected ','.", currentToken.getRegion()), true);
                }
                advance();
                argumentNodes.add(makeArgument(true));
                advance();
            }

            return new ArgumentNode(new ListLiteralNode(argumentNodes, argumentNodes.get(0).region.combine(argumentNodes.get(argumentNodes.size() - 1).region)));
        }

        if (currentToken.getType() == TokenType.SET_START) {
            Region start = currentToken.getRegion();
            advance();
            HashSet<ArgumentNode> argumentNodes = new HashSet<>();
            argumentNodes.add(makeArgument(true));

            advance();
            while (currentToken.getType() != TokenType.SET_END) {
                if (currentToken.getType() != TokenType.SEPARATOR) {
                    ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidSyntax", "Expected ','.", currentToken.getRegion()), true);
                }
                advance();
                argumentNodes.add(makeArgument(true));
                advance();
            }

            return new ArgumentNode(new SetLiteralNode(argumentNodes, start.combine(currentToken.getRegion())));
        }

        ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidSyntax", "Expected BARREL, PACKAGE, an identifier, '(' or '['.", currentToken.getRegion()), true);
        return new ArgumentNode(new None(), new Region());
    }

    private boolean matchesArgumentStart(Token token) {
        return token.getType() == TokenType.BARREL || token.getType() == TokenType.PACKAGE || token.getType() == TokenType.IDENTIFIER || token.getType() == TokenType.RETURN_GROUP_START || token.getType() == TokenType.ARGUMENT_KEYWORD || token.getType() == TokenType.LIST_START || token.getType() == TokenType.SET_START;
    }

    private boolean matchesCommandStart(Token token, Token next) {
        return (token.getType() == TokenType.IDENTIFIER && next.getType() != TokenType.METHOD_ACCESS) || token.getType() == TokenType.KEYWORD;
    }

    private StatementNode makeStatement() {
        if (currentToken.getType() == TokenType.LINE_BREAK || currentToken.getType() == TokenType.END_OF_FILE) {
            Region region = currentToken.getRegion();
            advance();
            return new EmptyStatementNode(region);
        }

        if (currentToken.getType() == TokenType.FUNCTION_RETURN) {
            return makeFunctionReturn();
        }

        if (currentToken.getType() == TokenType.KEYWORD && currentToken.getValue().value.equals("IF")) {
            return makeIf();
        }

        if (currentToken.getType() == TokenType.KEYWORD && currentToken.getValue().value.equals("LOOP")) {
            return makeLoop();
        }

        if (matchesCommandStart(currentToken, tokens.get(pointer + 1))) {
            return makeCommand();
        }

        if (matchesArgumentStart(currentToken)) {
            return makeMethodAccess();
        }

        ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidSyntax", "Unexpected token '" + currentToken.getValue().value + "'.", currentToken.getRegion()), true);
        return new EmptyStatementNode(currentToken.getRegion());
    }

    private DefinitionNode makeDefinition() {
        if (currentToken.getType() == TokenType.LINE_BREAK || currentToken.getType() == TokenType.END_OF_FILE) {
            Region region = currentToken.getRegion();
            advance();
            return new EmptyDefinitionNode(region);
        }

        if (currentToken.getType() == TokenType.KEYWORD && currentToken.getValue().value.equals("DECLARE")) {
            return makeFunctionDefinition();
        }

        if (currentToken.getType() == TokenType.KEYWORD && currentToken.getValue().value.equals("LOAD")) {
            return makeImport();
        }

        if (currentToken.getType() == TokenType.KEYWORD && currentToken.getValue().value.equals("UNLOAD")) {
            return makeExport();
        }

        ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidSyntax", "Unexpected token '" + currentToken.getValue().value + "'.", currentToken.getRegion()), true);
        return new EmptyDefinitionNode(currentToken.getRegion());
    }

    private FunctionDefinitionNode makeFunctionDefinition() {
        Region startRegion = currentToken.getRegion();
        advance();

        if (currentToken.getType() != TokenType.IDENTIFIER) {
            ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidSyntax", "Expected an identifier.", currentToken.getRegion()), true);
        }

        String name = (String) currentToken.getValue().value;

        List<BoatFunctionArgument> arguments = new ArrayList<>();

        advance();

        while (currentToken.getType() != TokenType.BLOCK_START) {
            if (currentToken.getType() != TokenType.IDENTIFIER) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidSyntax", "Expected an identifier.", currentToken.getRegion()), true);
            }

            String type = (String) currentToken.getValue().value;
            advance();

            if (currentToken.getType() != TokenType.IDENTIFIER) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidSyntax", "Expected an identifier.", currentToken.getRegion()), true);
            }

            String argName = (String) currentToken.getValue().value;
            advance();

            arguments.add(new BoatFunctionArgument(type, argName));
        }

        if (currentToken.getType() != TokenType.BLOCK_START) {
            ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidSyntax", "Expected ':'.", currentToken.getRegion()), true);
        }
        advance();

        List<StatementNode> body = new ArrayList<>();

        while (pointer < tokens.size()) {
            if (currentToken.getType() == TokenType.BLOCK_END) break;

            if (currentToken.getType() == TokenType.END_OF_FILE) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidSyntax", "The 'DECLARE' block must be closed by 'END'.", currentToken.getRegion()), true);
                break;
            }

            StatementNode node = makeStatement();

            body.add(node);
        }

        advance();


        startRegion.combine(body.stream().map(statementNode -> statementNode.region).toList());

        return new FunctionDefinitionNode(new BoatFunction(name, arguments, body), startRegion);
    }

    private ImportNode makeImport() {
        Region startRegion = currentToken.getRegion();

        advance();
        if (currentToken.getType() == TokenType.IDENTIFIER || currentToken.getType() == TokenType.PACKAGE) {
            ImportNode node = new ImportNode(currentToken.getValue(), startRegion.combine(currentToken.getRegion()));
            advance();
            return node;
        }
        ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidSyntax", "Expected PACKAGE or IDENTIFIER.", currentToken.getRegion()), true);
        return new ImportNode(new Package(""), new Region());
    }

    private ExportNode makeExport() {
        Region startRegion = currentToken.getRegion();

        advance();
        if (currentToken.getType() == TokenType.IDENTIFIER) {
            ExportNode node = new ExportNode(((Package) currentToken.getValue()).value, startRegion.combine(currentToken.getRegion()));
            advance();
            return node;
        }
        ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidSyntax", "Expected an IDENTIFIER.", currentToken.getRegion()), true);
        return new ExportNode("", new Region());
    }

    private MethodAccessNode makeMethodAccess() {
        ArgumentNode root = makeArgument(false);
        advance();

        if (currentToken.getType() != TokenType.METHOD_ACCESS) {
            ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidSyntax", "Expected '.'.", currentToken.getRegion()), true);
            return new MethodAccessNode(new ArgumentNode(new None(), new Region()), "", new ArrayList<>(), new Region());
        }

        advance();
        Token nameToken = currentToken;
        advance();

        List<ArgumentNode> arguments = new ArrayList<>();
        while (true) {
            if (matchesArgumentStart(currentToken)) {
                arguments.add(makeArgument(true));
                advance();
            } else {
                break;
            }
        }

        return new MethodAccessNode(root, (String) nameToken.getValue().value, arguments, nameToken.getRegion().combine(arguments.stream().map(argumentNode -> argumentNode.region).toList()));
    }

    private CommandNode makeCommand() {
        Token nameToken = currentToken;
        advance();

        List<ArgumentNode> arguments = new ArrayList<>();
        while (true) {
            if (matchesArgumentStart(currentToken)) {
                arguments.add(makeArgument(true));
                advance();
            } else {
                break;
            }
        }

        return new CommandNode((String) nameToken.getValue().value, arguments, nameToken.getRegion().combine(arguments.stream().map(argumentNode -> argumentNode.region).toList()));
    }

    private LoopNode makeLoop() {
        advance();
        if (currentToken.getType() == TokenType.KEYWORD && currentToken.getValue().value.equals("IF")) {
            return makeLoopIf();
        }

        ArgumentNode times = makeArgument(true);
        advance();

        if (currentToken.getType() != TokenType.KEYWORD || !currentToken.getValue().value.equals("TIMES")) {
            ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidSyntax", "Expected 'TIMES'.", currentToken.getRegion()), true);
        }

        advance();

        if (currentToken.getType() == TokenType.KEYWORD && currentToken.getValue().value.equals("AS")) {
            return makeLoopTimesAs(times);
        }

        if (currentToken.getType() != TokenType.BLOCK_START) {
            ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidSyntax", "Expected ':'.", currentToken.getRegion()), true);
        }

        return makeLoopTimes(times);
    }

    private LoopTimesAsNode makeLoopTimesAs(ArgumentNode times) {
        advance();
        if (currentToken.getType() != TokenType.IDENTIFIER) {
            ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidSyntax", "Expected an identifier.", currentToken.getRegion()), true);
        }

        String name = (String) currentToken.getValue().value;

        advance();
        if (currentToken.getType() != TokenType.BLOCK_START) {
            ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidSyntax", "Expected ':'.", currentToken.getRegion()), true);
        }
        advance();

        List<StatementNode> body = new ArrayList<>();

        while (pointer < tokens.size()) {
            if (currentToken.getType() == TokenType.BLOCK_END) break;

            if (currentToken.getType() == TokenType.END_OF_FILE) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidSyntax", "The 'LOOP' body must be closed by 'END'.", currentToken.getRegion()), true);
                break;
            }

            StatementNode node = makeStatement();
            body.add(node);
        }

        advance();

        Region region = times.region;

        region.combine(body.stream().map(statementNode -> statementNode.region).toList());

        return new LoopTimesAsNode(times, name, body, region);
    }

    private LoopTimesNode makeLoopTimes(ArgumentNode times) {
        advance();
        List<StatementNode> body = new ArrayList<>();

        while (pointer < tokens.size()) {
            if (currentToken.getType() == TokenType.BLOCK_END) break;

            if (currentToken.getType() == TokenType.END_OF_FILE) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidSyntax", "The 'LOOP' body must be closed by 'END'.", currentToken.getRegion()), true);
                break;
            }

            StatementNode node = makeStatement();
            body.add(node);
        }

        advance();

        Region region = times.region;

        region.combine(body.stream().map(statementNode -> statementNode.region).toList());

        return new LoopTimesNode(times, body, region);
    }

    private LoopIfNode makeLoopIf() {
        advance();
        ConditionNode condition = makeCondition();
        if (currentToken.getType() != TokenType.BLOCK_START) {
            ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidSyntax", "Expected ':'.", currentToken.getRegion()), true);
        }
        advance();

        List<StatementNode> body = new ArrayList<>();

        while (pointer < tokens.size()) {
            if (currentToken.getType() == TokenType.BLOCK_END) break;

            if (currentToken.getType() == TokenType.END_OF_FILE) {
                ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidSyntax", "The 'LOOP' body must be closed by 'END'.", currentToken.getRegion()), true);
                break;
            }

            StatementNode node = makeStatement();
            body.add(node);
        }

        advance();

        Region region = condition.region;

        region.combine(body.stream().map(statementNode -> statementNode.region).toList());

        return new LoopIfNode(condition, body, region);
    }

    private IfNode makeIf() {
        Region region = currentToken.getRegion();


        advance();
        ConditionNode condition = makeCondition();
        if (currentToken.getType() != TokenType.BLOCK_START) {
            ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidSyntax", "Expected ':'.", currentToken.getRegion()), true);
        }
        advance();

        boolean inElseBlock = false;
        List<StatementNode> ifBlock = new ArrayList<>();
        List<StatementNode> elseBlock = new ArrayList<>();

        while (pointer < tokens.size()) {
            if (currentToken.getType() == TokenType.BLOCK_END) break;

            if (currentToken.getType() == TokenType.END_OF_FILE) {
                if (inElseBlock) {
                    ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidSyntax", "The 'ELSE' block must be closed by 'END'.", currentToken.getRegion()), true);
                } else {
                    ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidSyntax", "The 'IF' block must be closed by 'END' or by 'ELSE' block.", currentToken.getRegion()), true);
                }
                break;
            }

            if (currentToken.getType() == TokenType.KEYWORD && currentToken.getValue().value.equals("ELSE")) {
                if (inElseBlock) {
                    ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidSyntax", "Unexpected start of 'ELSE' block.", currentToken.getRegion()), true);
                }

                advance();
                if (currentToken.getType() != TokenType.BLOCK_START) {
                    ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidSyntax", "Expected ':'.", currentToken.getRegion()), true);
                }
                advance();
                inElseBlock = true;
            }

            StatementNode node = makeStatement();

            if (inElseBlock) {
                elseBlock.add(node);
            } else {
                ifBlock.add(node);
            }
        }

        advance();

        region.combine(ifBlock.stream().map(statementNode -> statementNode.region).toList());
        region.combine(elseBlock.stream().map(statementNode -> statementNode.region).toList());

        return new IfNode(condition, ifBlock, elseBlock, region);
    }

    private ConditionNode makeCondition() {
        ArgumentNode first = makeArgument(true);
        advance();
        if (currentToken.getType() != TokenType.OPERATOR) {
            ErrorLog.getInstance().registerError(new BoatError(ErrorType.CRITICAL, "InvalidSyntax", "Expected '==', '!=', '<', '>', '<=' or '>='.", currentToken.getRegion()), true);
        }
        String operator = currentToken.getValue().value.toString();
        advance();
        ArgumentNode second = makeArgument(true);
        advance();
        return new ConditionNode(first, operator, second);
    }

    private FunctionReturnNode makeFunctionReturn() {
        advance();
        StatementNode toReturn = makeStatement();
        advance();
        return new FunctionReturnNode(toReturn);
    }
}
