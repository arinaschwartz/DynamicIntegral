/*
 * ParseExpression.java - Parse an expression.
 *
 * Copyright (c) 1996 Chuck McManis, All Rights Reserved.
 *
 * Permission to use, copy, modify, and distribute this software
 * and its documentation for NON-COMMERCIAL purposes and without
 * fee is hereby granted provided that this copyright notice
 * appears in all copies.
 *
 * CHUCK MCMANIS MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE
 * SUITABILITY OF THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING
 * BUT NOT LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE, OR NON-INFRINGEMENT. CHUCK MCMANIS
 * SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT
 * OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 * 
 * 2009, 2010: Modified by Anne Rogers to use FoX 
 * and add interval, sqrt, trigonometric operators.
 */

import java.io.StreamTokenizer;
import java.io.IOException;
import java.io.StringReader;

/**
 * This class implements a simple recursive-descent parser for the
 * expression grammar I have designed. As an exercise it is right out
 * of Aho and Ullman's compiler design text.
 *
 * This grammar is defined with some nonterminals that allow us to embed the
 * precedence relationship of operators into the grammar. The grammar is
 * defined as follows:
 *
 * ELEMENT    ::=   id
 *             |    constant
 *             |    "(" expression ")"
 * PRIMARY    ::=   "-" ELEMENT
 *             |    "!" ELEMENT
 *             |    ELEMENT
 * FACTOR     ::=   PRIMARY "^" FACTOR
 *             |    PRIMARY
 * TERM       ::=   TERM "*" FACTOR
 *             |    TERM "/" FACTOR
 *             |    FACTOR
 * SUM        ::=   SUM "+" TERM
 *             |    SUM "-" TERM
 *             |    TERM
 * EXPRESSION ::=   EXPRESSION "&" SUM
 *             |    EXPRESSION "#" SUM
 *             |    EXPRESSION "|" SUM
 *             |    SUM
 *
 * Precidence rules from lowest to highest :
 *  1.  &, |, ^
 *  2.  +, -
 *  3.  *, /
 *  4.  **
 *  5.  unary -, unary !
 *
 */
class ParseExpression {
    static Dbl2Dbl element(StreamTokenizer st) {
        Dbl2Dbl result = null;

        try {
            switch (st.nextToken()) {
            case StreamTokenizer.TT_NUMBER :
                result = (Dbl2Dbl) new FofX(st.nval);
                break;

            case StreamTokenizer.TT_WORD :
                String s = st.sval;
                if (s.equals("x")) {
                    result = (Dbl2Dbl) new FofX("x", null);
                } else if (s.equals("pi")) {
                    result = (Dbl2Dbl) new FofX(Math.PI);
                } else if (s.equals("sqrt") || 
                           s.equals("sin") || 
                           s.equals("cos") || 
                           s.equals("tan") || 
                           s.equals("acos") || 
                           s.equals("atan") || 
                           (s.equals("atan"))) {
                    st.nextToken();
                    if (st.ttype != '(') {
                        syntaxError("expected (");
                    }
                    result = new FofX(s, expression(st));
                    st.nextToken();
                    if (st.ttype != ')') {
                        syntaxError("Mismatched parenthesis:" + st.sval);
                    }
                }
                else if (s.equals("interval")) {
                    // interval(lb, ub, expression)
                    st.nextToken();
                    if (st.ttype != '(') {
                        syntaxError("expected (");
                    }

                    double lb = 0;
                    st.nextToken();
                    if (st.ttype == StreamTokenizer.TT_NUMBER)
                        lb = st.nval;
                    else
                        syntaxError("Expected a double");

                    st.nextToken();
                    if (st.ttype != ',')
                        syntaxError("Expected a comma 1:" + st.sval);

                    double ub = 0;
                    st.nextToken();
                    if (st.ttype == StreamTokenizer.TT_NUMBER)
                        ub = st.nval;
                    else
                        syntaxError("Expected a double");

                    st.nextToken();
                    if (st.ttype != ',')
                        syntaxError("Expected a comma 2" + st.sval);

                    result = new FofX(lb, ub, expression(st));

                    st.nextToken();
                    if (st.ttype != ')') {
                        syntaxError("Mismatched parenthesis:" + st.sval);
                    }
                } else 
                    syntaxError("unknown variable:" + st.sval + ":");
                break;
            case '(' :
                result = expression(st);
                st.nextToken();
                if (st.ttype != ')') {
                    syntaxError("Mismatched parenthesis:" + st.sval);
                }
                break;
            default:
                syntaxError("Unexpected symbol on input.");
            }
        } catch (IOException ioe) {
            syntaxError("Caught an I/O exception.");
        }
        return result;
    }

    static Dbl2Dbl primary(StreamTokenizer st)  {
        try {
            switch (st.nextToken()) {
            case '-' :
                return new FofX("unary-minus", primary(st));
            default:
                st.pushBack();
                return element(st);
            }
        } catch (IOException ioe) {
            syntaxError("Caught an I/O Exception.");
        }

        // should not get here.
        return null;

    }

    static Dbl2Dbl factor(StreamTokenizer st)  {
        Dbl2Dbl result;

        result = primary(st);
        try {
            switch (st.nextToken()) {
            case '^':
                result = new FofX("pow", result, factor(st));
                break;
            default:
                st.pushBack();
                break;
            }
        } catch (IOException ioe) {
            syntaxError("Caught an I/O Exception.");
        }
        return result;
    }

    static Dbl2Dbl term(StreamTokenizer st)  {
        Dbl2Dbl result;
        boolean done = false;

        result = factor(st);
        while (! done) {
            try {
                switch (st.nextToken()) {
                case '*' :
                    result = new FofX("mult", result, factor(st));
                    break;
                case '/' :
                    result = new FofX("div", result, factor(st));
                    break;
                default :
                    st.pushBack();
                    done = true;
                    break;
                }
            } catch (IOException ioe) {
                syntaxError("Caught an I/O exception.");
            }
        }
        return result;
    }

    static Dbl2Dbl sum(StreamTokenizer st)  {
        Dbl2Dbl result;
        boolean done = false;

        result = term(st);

        while (! done) {
            try {
                switch (st.nextToken()) {
                case '+':
                    result = new FofX("add", result, term(st));
                    break;
                case '-':
                    result = new FofX("subtract", result, term(st));
                    break;
                default :
                    st.pushBack();
                    done = true;
                    break;
                }
            } catch (IOException ioe) {
                syntaxError("Caught an I/O Exception.");
            }
        }
        return result;
    }

    static Dbl2Dbl expression(StreamTokenizer st) {
        Dbl2Dbl result;
        boolean done = false;

        result = sum(st);
        return result;
    }

    static Dbl2Dbl expression(String s) {
        StreamTokenizer st = new StreamTokenizer(new StringReader(s));
        return expression(st);
    }

    static void syntaxError(String s) {
        System.out.println(s);
        System.exit(0);
    }

    public static void main(String[] args) {
        StreamTokenizer st = new StreamTokenizer(new StringReader(args[0]));
        Dbl2Dbl f = ParseExpression.expression(st);
        f.print();
        System.out.println();
    }
}
