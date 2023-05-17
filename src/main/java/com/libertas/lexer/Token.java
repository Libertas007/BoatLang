package com.libertas.lexer;

import com.libertas.generics.Region;
import com.libertas.variables.Variable;

public class Token {
    private final TokenType type;
    private final Variable value;
    private final Region region;

    public Token(TokenType type, Variable value, Region region) {
        this.type = type;
        this.value = value;
        this.region = region;
    }

    public TokenType getType() {
        return type;
    }

    public Variable getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Token{" +
                "type=" + type +
                ", value='" + value + '\'' +
                ", region=" + region +
                '}';
    }

    public Region getRegion() {
        return region;
    }
}
