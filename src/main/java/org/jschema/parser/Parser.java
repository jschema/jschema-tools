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
      String tokenValue = getValue();
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
      String tokenValue = getValue();
      nextToken();
      if (tokenValue.indexOf('.') >= 0 || tokenValue.toLowerCase().indexOf('e') >= 0){
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
    HashMap<String, Object> map = new HashMap<String, Object>();
    // cgross - should be looping on COMMA, not RCURLY
    while(!match(RCURLY)){
      if(parseMember(map) instanceof Error){
        // cgross - probably better to return the returned error
        return error();
      }
      if(match(RCURLY)){
        break;
      }
      if(!match(COMMA) || match(EOF)){
        return error();
      }
      nextToken();
      if(match(RCURLY)){
        return error();
      }
    }
    if(match(RCURLY)){
      nextToken();
      return map;
    }
    return error();
  }

  private Object parseMember( HashMap map )
  {
    if(match(STRING)){
      String key = getValue();
      nextToken();
      if(!match(COLON)){
        return error();
      }
      nextToken();
      Object value = parseValue();
      if(!(value instanceof Error)){
        map.put(key, value);
        return map;
      }
    }
    return error();
  }

  public Object parseArray()
  {
    ArrayList arrayList = new ArrayList();
    // cgross - again, should be matching on COMMA, not RSQUARE
    while(!match(RSQUARE)){
      if(match(EOF)) {
        return error();
      }
      Object value = parseValue();
      if((value instanceof Error)){
        return error();
      }
      arrayList.add(value);
      if(match(RSQUARE)){
        break;
      }
      if(!match(COMMA) || match(EOF)){
        return error();
      }
      nextToken();
      if(match(RSQUARE)){
        return error();
      }
    }
    nextToken();
    return arrayList;
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

  private String getValue(){
    return _currentToken.getTokenValue();
  }

  private Error error()
  {
    return new Error("Unexpected Token: " + _currentToken.toString());
  }
}
