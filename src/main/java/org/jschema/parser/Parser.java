package org.jschema.parser;

import org.jschema.parser.Token.TokenType;
import sun.org.mozilla.javascript.ast.WhileLoop;

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
    boolean closed = false;

    if(match(RCURLY)){
      nextToken();
      return map;
    }
    // cgross - I would expect you to loop on COMMA/EOF, not !RCURLY
    while(!match(EOF) && !match(RCURLY)){
      try {
        map = (HashMap<String, Object>) parseMember(map);

        if (!match(RCURLY) || match(COMMA)) {
          nextToken();
        }
        if (match(RCURLY)) {
          closed = true;
        }
      }
      catch (ClassCastException e){
        // cgross - probably better to use instanceof rather than a ClassCastException
        return error();
      }
    }
    nextToken();
    if(closed){
      return map;
    }
    return error();
  }

  private Object parseMember( HashMap map )
  {
    String key;
    Object obj;

    key = _currentToken.getTokenValue();
    nextToken();
    if(match(COLON)){
      nextToken();
      obj = parseValue();
      if (obj instanceof Error) {
        return error();
      }
      map.put(key, obj);
    }
    else{
      nextToken();
      return error();
    }
    // cgross - this should be handled in the loop above, not with recursion 
    if (match(COMMA)) {
      nextToken();
      return parseMember(map);
    }
    else if (match(RCURLY)) {
      return map;
    }
    else {
      return error();
    }
  }

  public Object parseArray()
  {
    ArrayList list = new ArrayList();
    boolean closed = false;
    if (match(RSQUARE)) {
      nextToken();
      return list;
    }
    // cgross - again, probably should be looping on the comma, not on !RSQUARE
    while (!match(RSQUARE) && !match(EOF)) {
      Object val = parseValue();
      if (val == null || !val.equals(",")) {
        list.add(val);
      } else {
        return new ArrayList();
      }
      if (match(RSQUARE)) {
        closed = true;
      }
      else if (match(COMMA)) {
        nextToken();
      }
      else {
        nextToken();
        return error();
      }
    }
    nextToken();
    if (!closed) {
      return error();
    }
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
