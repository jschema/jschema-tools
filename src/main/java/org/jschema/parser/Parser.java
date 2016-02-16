package org.jschema.parser;

import org.jschema.parser.Token.TokenType;

import java.util.ArrayList;
import java.util.HashMap;

import static org.jschema.parser.Token.TokenType.*;

public class Parser {

    private final Tokenizer _tokenizer;
    private Token _currentToken;

    public Parser(String src) {
        _tokenizer = new Tokenizer(src);
        nextToken();
    }

    //=================================================================================
    //  JSON Grammar
    //=================================================================================

    public Object parse() {
        Object value = parseValue();
        if (match(EOF)) {
            return value;
        } else {
            return error();
        }
    }

    public Object parseValue() {
        //parse objects
        if (match(LCURLY)) {
            nextToken();
            return parseObject();
        }

        // parse arrays
        if (match(LSQUARE)) {
            nextToken();
            return parseArray();
        }

        // parse literals (e.g. true, false, strings, numbers)
        if (match(STRING)) {
            String tokenValue = _currentToken.getTokenValue();
            nextToken();
            return tokenValue;
        }

        if (match(TRUE)) {
            nextToken();
            return true;
        }
        if (match(FALSE)) {
            nextToken();
            return false;
        }
        if (match(NULL)) {
            nextToken();
            return null;
        }

        if (match(NUMBER)) {
            double num = _currentToken.getTokenNumberValue();
            //check if is an int
            if (!isExponent()) {
                nextToken();
                return ((int) num);
            }
            nextToken();
            return num;

        }
        if (match(ERROR)) {
            return error();
        }


        return error();
    }


    public Object parseObject() {
        //pass the map into parseMember to populate
        HashMap<String, Object> map = new HashMap<>();
        //for empty object
        // cgross - can this be rewritten/removed so that the while loop catches it?
        if (match(RCURLY)) {
            nextToken();
            return map;
        }
        int rBrace = 0;
        //keep going if there is a comma
        // cgross - while loop should be on COMMA/EOF if there is a RCURLY 
        while (!match(RCURLY) && !match(EOF)) {
            try {
                // cgross - would use an instanceof test, rather than a ClassCastException
                map = (HashMap<String, Object>) parseMember(map);

                if (!match(RCURLY) || match(COMMA)) {
                    nextToken();
                }
                if (match(RCURLY)) {
                    rBrace = 1;
                }
                //if a map isn't returned, an error must be returned
            } catch (ClassCastException e) {
                // cgross - should return the error returned by parseMember
                return error();
            }
        }
        nextToken();
        // cgross - if you get the while-loop correct, this check should not be necessary
        //if no matching right brace, return error
        if (rBrace != 1) {
            return error();
        }
        return map;
    }

    private Object parseMember(HashMap map) {
        //get the key
        // cgross - should verify the token is a string?
        String key = _currentToken.getTokenValue();
        Object val;
        //now check that colon is separator
        nextToken();
        if (!match(COLON)) {
            nextToken();
            return error();
        } else {
            //now get value
            nextToken();
            val = parseValue();
            //if error returned
            if (val instanceof Error) {
                return error();
            }
            map.put(key, val);
        }
        
        // cgross - Ah, interesting that you are recursing here.  I would do this with the loop above to avoid recursive blowout
        if (match(COMMA)) {
            nextToken();
            return parseMember(map);
        } else if (match(RCURLY)) {
            return map;
        } else {
            return error();
        }
    }


    public Object parseArray() {
        ArrayList list = new ArrayList();
        //for empty arrays
        if (match(RSQUARE)) {
            nextToken();
            return list;
        }
        int rBrace = 0;
        // cgross - as above, I would expect the loop to be on COMMA/EOF, not RSQUARE
        while (!match(RSQUARE) && !match(EOF)) {
            //first get item
                Object val = parseValue();
                //if not comma, add to list since
                if (val == null || !val.equals(",")) {
                    list.add(val);
                } else {
                    //return error if not valid, already on next token
                    //so don't need to switch index
                    error();
                    return new ArrayList();
                }
                //if a comma, move to next token
                if (match(RSQUARE)) {
                    rBrace = 1;
                } else if (match(COMMA)) {
                    nextToken();
                } else {
                    nextToken();
                    return error();
                }

        }
        nextToken();
        if (rBrace != 1) {
            return error();
        }
        return list;
    }

    //=================================================================================
    //  Tokenizer helpers
    //=================================================================================

    private void nextToken() {
        _currentToken = _tokenizer.next();
    }

    private boolean match(TokenType type) {
        return _currentToken.getTokenType() == type;
    }

    private Error error() {
        return new Error("Unexpected Token: " + _currentToken.toString());
    }

    private boolean isExponent() {
        String val = _currentToken.getTokenValue();
        return val.contains(".") || val.contains("e") || val.contains("E");
    }
}
