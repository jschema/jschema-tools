package org.jschema.parser;

import org.jschema.parser.Token.TokenType;

import java.util.ArrayList;
import java.util.HashMap;
import java.lang.*;
import java.util.*;

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

    // parse literals (e.g. true, false, strings, numbers)
    if( match( STRING ) )
    {
      String tokenValue = _currentToken.getTokenValue();
        nextToken();
        return tokenValue;
    }

    //TODO implement other literals
    

    if( match ( NUMBER ) ){
      double tokenNumberValue = _currentToken.getTokenNumberValue();

      if (!_currentToken.getTokenValue().contains(".")) {             //if it's an integer
        if( _currentToken.getTokenValue().contains("e") ||
                _currentToken.getTokenValue().contains("E")){
          nextToken();
          return tokenNumberValue;
        }
          int temp = (int) (_currentToken.getTokenNumberValue());
          nextToken();
          return temp;
      }
      nextToken();
      return tokenNumberValue;
    }


    
    if( match ( TRUE )){
      nextToken();
      return true;
    }

    if( match ( FALSE )){
      nextToken();
      return false;
    }

    if( match ( NULL )){
      nextToken();
      return null;
    }

    if( match ( ERROR )){

      nextToken();
      return error();
    }

    return error();
  }

  public Object parseObject()
  {
    //TODO implement, return a map of name/value pairs, and Error if an error is detected
    //                pass the map into parseMember to populate
    HashMap<String, Object> map = new HashMap<>();

    while ( !match( RCURLY ) ){
      if( match ( EOF ) || match ( ERROR ) ){
        return error();
      }
      map = (HashMap<String, Object>) parseMember(map);
      nextToken();
    }

    if( match( RCURLY ) ){
      nextToken();
      return map;
    }else{
      return error();
    }

  }

  private Object parseMember( HashMap map )
  {
    //TODO implement, parse the key and value, return the map if it is good, Error otherwise.
    //key value pair

    if( !match( STRING ) ){
      return error();
    }

    String key = _currentToken.getTokenValue();
    nextToken();

    if( !match( COLON ) ){
      return error();
    }

    Object value = _currentToken.getTokenValue();
    nextToken();

    if( match( RCURLY ) ){               //end is reached
      return _currentToken;
    }
    map.put(key, value);
    return map;
  }

  public Object parseArray()
  {
    //TODO implement, parse the elements inline, return Error if any element is error

    List<Object> mylist = new ArrayList<>();
    Object s;

    while( !match( RSQUARE ) ) {
      if ( match( EOF ) || match( ERROR ) ){
        return error();
      }else if( !match( COMMA ) ){
        s = _currentToken.getTokenValue();
        mylist.add(s);
      }
      nextToken();                    //if matches comma, not eof and not error, nextToken.
    }

    if ( match( RSQUARE ) ){                                   //once it hits the end
      nextToken();
    }else{
      return error();
    }
    //return new ArrayList();
    return mylist;
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
