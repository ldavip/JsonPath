package com.jayway.jsonpath.internal;

public class Symbols {
    public static final char DOC_CONTEXT = '$';
    public static final char EVAL_CONTEXT = '@';
    public static final char PARAM_CONTEXT = '?';

    public static final char OPEN_SQUARE_BRACKET = '[';
    public static final char CLOSE_SQUARE_BRACKET = ']';
    public static final char OPEN_PARENTHESIS = '(';
    public static final char CLOSE_PARENTHESIS = ')';
    public static final char OPEN_BRACE = '{';
    public static final char CLOSE_BRACE = '}';

    public static final char WILDCARD = '*';
    public static final char PERIOD = '.';
    public static final char SPACE = ' ';
    public static final char TAB = '\t';
    public static final char CR = '\r';
    public static final char LF = '\n';
    public static final char BEGIN_FILTER = '?';
    public static final char COMMA = ',';
    public static final char SPLIT = ':';
    public static final char MINUS = '-';
    public static final char SINGLE_QUOTE = '\'';
    public static final char DOUBLE_QUOTE = '"';

    public static final char ESCAPE = '\\';
    public static final char REGEX = '/';

    private Symbols() {
    }
}
