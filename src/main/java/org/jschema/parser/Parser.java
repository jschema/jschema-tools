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
        HashMap<String, Object> map = new HashMap<>();
        Object val;
        if(match(STRING)){
            val=parseMember(map);
            if(val instanceof Error){
                return val;
            }else{
                map=(HashMap<String, Object>)val;
            }
        }
        while (match(COMMA)) {
            nextToken();
                val=parseMember(map);
                if(val instanceof Error){
                    return val;
                }else{
                    map=(HashMap<String, Object>)val;
                }
        }
        if (!match(RCURLY)) {
            nextToken();
            return error();
        }
        nextToken();
        return map;
    }

    private Object parseMember(HashMap map) {
        String key="";
        if(match(STRING)) {
            key = _currentToken.getTokenValue();
            nextToken();
            if (match(COLON)) {
                nextToken();
                Object val = parseValue();
                if (val instanceof Error) {
                    return error();
                }
                map.put(key, val);
                return map;
            }
        }
            return error();
    }


    public Object parseArray() {
        ArrayList list = new ArrayList();
        Object val;
        //for empty arrays
        if (!match(RSQUARE)) {
            val=parseValue();
            if (val instanceof Error) {
                return error();
            } else {
                list.add(val);
            }
        }
        while (match(COMMA)) {
            nextToken();
            val = parseValue();
            if (val instanceof Error) {
                return error();
            } else {
                list.add(val);
            }
        }
        if (!match (RSQUARE)) {
            return error();
        }
        nextToken();
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