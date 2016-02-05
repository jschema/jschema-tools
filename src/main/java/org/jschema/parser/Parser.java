package org.jschema.parser;

import org.jschema.parser.Token.TokenType;

import java.util.ArrayList;
import java.util.HashMap;

import static org.jschema.parser.Token.TokenType.*;

public class Parser
{

  private final Tokenizer _tokenizer;
  private Token _currentToken;

  public Parser( String src )
  {
    _tokenizer = new Tokenizer( src );
    nextToken();
  }

  //=================================================================================
  //  JSON Grammar
  //=================================================================================

  public Object parse() {
    Object value = parseValue();
    if(match(EOF)) {
      return value;
    }
    else {
      return error();
    }
  }

  public Object parseValue()
  {
    if(match(LCURLY)) {
      nextToken();
      return parseObject();
    }

    // parse arrays
    if(match(LSQUARE)) {
      Object array = parseArray();
      nextToken();
      return array;
    }

    // parse literals (e.g. true, false, strings, numbers)
    if(match(STRING)) {
      String tokenValue = _currentToken.getTokenValue();
      nextToken();
      return tokenValue;
    } else if (match(NUMBER)) {
      String tokenValue = _currentToken.getTokenValue();
      nextToken();
      try {
        return Integer.parseInt(tokenValue);
      } catch(NumberFormatException exception) {
        // Definitely a double value
        try {
          return Double.parseDouble(tokenValue);
        } catch(NumberFormatException e) {
          return error();
        }
      }
    } else if (match(TRUE)) {
      nextToken();
      return true;
    } else if (match(FALSE)) {
      nextToken();
      return false;
    } else if (match(NULL)) {
      nextToken();
      return null;
    }

    System.out.println("Returning error in parseValue()!");
    return error();
  }

  public Object parseObject()
  {
    HashMap<String, Object> map = new HashMap<>();
    Object nextMemberResult = null;
    while(!(nextMemberResult instanceof Token)) {
      nextMemberResult = addNextMember(map);
      if (nextMemberResult instanceof Error) {
        return nextMemberResult;
      } else if (nextMemberResult instanceof HashMap) {
        map = (HashMap<String, Object>) nextMemberResult;
      }
    }
    return map;
  }

  private Object addNextMember( HashMap map )
  {
    String key;
    Object value;
    nextToken();
    if (match(RCURLY)) { return _currentToken; }
    if (!match(STRING)) { return error(); }
    key = _currentToken.getTokenValue();
    nextToken();
    if (!match(COLON)) { return error(); }
    nextToken();
    value = _currentToken.getTokenValue();
    map.put(key, value);
    return map;
  }

  public Object parseArray()
  {
    ArrayList<Object> array = new ArrayList<>();

    // step over left square bracket
    nextToken();

    // array contents
    while (!match(RSQUARE)) {
      if (match(ERROR) || match(EOF)) {
        // Error or reach end of file prematurely
        return error();
      } else if (match(LSQUARE)) {
        // sub-array, parse recursively
        Object inArray = parseArray();
        if (inArray instanceof Error) {
          return inArray;
        } else {
          array.add(inArray);
        }
      } else if (!match(COMMA)) {
        array.add(_currentToken.getTokenValue());
      }
      nextToken();
    }

    return array;
  }

  //=================================================================================
  //  Tokenizer helpers
  //=================================================================================
  private void nextToken()
  {
    _currentToken = _tokenizer.next();
  }

  private boolean match( TokenType type )
  {
    return _currentToken.getTokenType() == type;
  }

  private Error error()
  {
    return new Error("Unexpected Token: " + _currentToken.toString());
  }
}
