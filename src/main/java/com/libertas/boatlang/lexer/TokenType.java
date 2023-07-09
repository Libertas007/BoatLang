package com.libertas.boatlang.lexer;

public enum TokenType {
    BARREL,
    PACKAGE,
    IDENTIFIER,  // CAN BE AT THE START
    OPERATOR,
    LINE_BREAK, // CAN BE AT THE START
    RETURN_GROUP_START,
    RETURN_GROUP_END,
    BLOCK_START,
    BLOCK_END, // CAN BE AT THE START
    KEYWORD, // CAN BE AT THE START
    FUNCTION_RETURN, // CAN BE AT THE START
    END_OF_FILE,
    ARGUMENT_KEYWORD,
    METHOD_ACCESS,
    LIST_START,
    LIST_END,
    SEPARATOR,
}

/*
    || valid syntax:


    statement:      (
                        ((IDENTIFIER|KEYWORD) Argument*)| ✅
                        FUNCTION_RETURN statement| ✅
                        Argument METHOD_ACCESS IDENTIFIER
                        if-condition| ✅
                        loop-times| ✅
                        loop-times-as| ✅
                        loop-if| ✅
                    )
    definition:     (
                        function| ✅
                        import| ✅
                        export ✅
                    )

    if-condition:   ✅
                    KEYWORD[IF] Condition BLOCK_START
                    statement*
                    (BLOCK_END|else-condition)
    else-condition: ✅
                    KEYWORD[ELSE] Condition BLOCK_START
                    statement*
                    BLOCK_END
    loop-times:     ✅
                    KEYWORD[LOOP] Argument KEYWORD[TIMES] BLOCK_START
                    statement*
                    BLOCK_END
    loop-times-as:  ✅
                    KEYWORD[LOOP] Argument KEYWORD[TIMES] KEYWORD[AS] IDENTIFIER BLOCK_START
                    statement*
                    BLOCK_END
    loop-if:        ✅
                    KEYWORD[LOOP] KEYWORD[IF] Condition BLOCK_START
                    statement*
                    BLOCK_END

    import:         KEYWORD[LOAD] IDENTIFIER|PACKAGE ✅

    export:         KEYWORD[UNLOAD] IDENTIFIER ✅

    function:       ✅
                    KEYWORD[DEFINE] (IDENTIFIER IDENTIFIER)* BLOCK_START
                    statement*
                    BLOCK_END


    || wrappers:

    Argument    = (
                    BARREL| ✅
                    PACKAGE| ✅
                    IDENTIFIER| ✅
                    (RETURN_GROUP_START statement RETURN_VALUE_END) ✅
                    LIST_START (Argument SEPARATOR)* LIST_END
                  )
    Condition   = (
                    (Argument OPERATOR Argument)| ✅
                    Argument
                  )
 */