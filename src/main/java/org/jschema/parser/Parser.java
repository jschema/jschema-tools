package org.jschema.parser;

import org.jschema.parser.Token.TokenType;
//import sun.org.mozilla.javascript.ast.WhileLoop;

import java.io.EOFException;
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

  public Object parseValue()
  {

    if( match( LCURLY ) )
    {
      nextToken();
      return parseObject();
    }

    // parse arrays
    if( match( LSQUARE ) )
    {
      nextToken();
      return parseArray();
    }

    if( match( TRUE ) )
    {
      nextToken();
      return true;
    }

    if( match( FALSE ) )
    {
      nextToken();
      return false;
    }
    if( match( NULL ) )
    {
      nextToken();
      return null;
    }

    if( match( STRING ) )
    {
      String tokenValue = _currentToken.getTokenValue();
      nextToken();
      return tokenValue;
    }
    if( match( NUMBER ) )
    {
      String tokenValue = _currentToken.getTokenValue();
      double tokenNum = _currentToken.getTokenNumberValue();
      if(tokenValue.indexOf('e') >= 0 || tokenValue.indexOf('E') >= 0){
        nextToken();
        return Double.parseDouble(tokenValue);
      }
      if(tokenNum % 1 == 0){
        nextToken();
        return Integer.parseInt(tokenValue);
      }
      else{
        nextToken();
        return tokenNum;
      }
    }
    return error();
  }

  public Object parseObject()
  {
    HashMap<String, Object> map = new HashMap<>();

    parseMember(map);
    if (match(EOF)) {
      nextToken();
      return map;
    }
    while (match(COMMA)) {
      nextToken();
      if (match(EOF)) {
        return map;
      }
      if(match(ERROR)) {
        nextToken();
        return error();
      }
      if(match(STRING)) {
        parseMember(map);
      }
    }
    nextToken();
    return map;
    }

  private void parseMember( HashMap map )
  {
    String key;
    Object obj;

    if(match(STRING)) {
      key = _currentToken.getTokenValue();
    }
    else{
      nextToken();
      return;
    }
    nextToken();
    if(match(COLON)){
      nextToken();
      obj = parseValue();
      if(obj instanceof Error){
        nextToken();
        return;
      }
      map.put(key, obj);
      if(match(COMMA)){
        return;
      }
    }
  }

  public Object parseArray()
  {
    ArrayList list = new ArrayList();
    if(match(RSQUARE)){
      nextToken();
      return list;
    }
    list.add(parseValue());
    while (match(COMMA)) {
      nextToken();
      list.add(parseValue());
    }
    nextToken();
    return list;
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
