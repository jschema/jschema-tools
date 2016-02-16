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

    // Arrays
    if(match(LSQUARE)) {
      nextToken();
      return parseArray();
    }

    // Literals (e.g. true, false, strings, numbers)
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

    return error();
  }

  public Object parseObject() {

    HashMap<String, Object> map = new HashMap<>();

    while (!match(RCURLY)) {
      if (match(ERROR) || match(EOF)) {
        return error();
        // cgross - what about a leading comma?  {,"foo":"bar"} ?
      } else if (match(COMMA)) {
        // Step over comma
        nextToken();
      } else {
        // Get the next member
        Object next = parseMember(map);
        if (next instanceof Error) {
          return next;
        } else {
          // cgross - Probably not a necessary assignment
          map = (HashMap<String, Object>)next;
        }
      }
    }
    nextToken();

    return map;
  }

  private Object parseMember( HashMap map )
  {
    // Keys must be strings
    if (!match(STRING)) { return error(); }

    String key = _currentToken.getTokenValue();

    // Step over colon
    nextToken();
    if (!match(COLON)) { return error(); }
    nextToken();

    // Get value and insert into map
    Object value = parseValue();
    if (value instanceof Error) { return value; }
    map.put(key, value);

    return map;
  }

  public Object parseArray()
  {
    ArrayList<Object> array = new ArrayList<>();

    while (!match(RSQUARE)) {
      if (match(ERROR) || match(EOF)) {
        return error();
        // cgross - again, leading comma issue
      } else if (match(COMMA)) {
        // Step over comma
        nextToken();

        // Commas must be followed by a value
        if (match(RSQUARE)) { return error(); }
      } else {
        // Add next item
        Object next = parseValue();
        if (next instanceof Error) {
          return next;
        } else {
          array.add(next);
        }
      }
    }
    nextToken();

    return array;
  }

  //=================================================================================
  //  Tokenizer helpers
  //=================================================================================
  private void nextToken() { _currentToken = _tokenizer.next(); }

  private boolean match( TokenType type )
  {
    return _currentToken.getTokenType() == type;
  }

  private Error error()
  {
    return new Error("Unexpected Token: " + _currentToken.toString());
  }
}
