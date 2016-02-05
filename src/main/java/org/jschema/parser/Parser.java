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
    if( match( EOF ) )
    {
      return value;
    }
    else
    {
      return error();
    }
  }

  public Object parseValue() {
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

    if (match(NUMBER)){
      String tokenValue = _currentToken.getTokenValue();
      nextToken();
      if (tokenValue.indexOf('.') >= 0){
        return Double.parseDouble(tokenValue);
      }
      else{
        return Integer.parseInt(tokenValue);
      }
    }
    return error();
  }
  public Object parseObject()
  {
    //TODO implement, return a map of name/value pairs, and Error if an error is detected
    //                pass the map into parseMember to populate
    HashMap<String, Object> map = new HashMap<>();
    if( match( RCURLY ) )
    {
      nextToken();
      return map;
    }
    else
    {
      return error();
    }
  }

  private Object parseMember( HashMap map )
  {
    //TODO implement, parse the key and value, return the map if it is good, Error otherwise.
    return map;
  }

  public Object parseArray()
  {
    //TODO implement, parse the elements inline, return Error if any element is error
    return new ArrayList();
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
