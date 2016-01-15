package org.jschema.tokenizer;

import java.util.ArrayList;
import java.util.List;

import static org.jschema.tokenizer.Token.TokenType.*;
import java.util.*;


public class Tokenizer
{
  private String _string;
  private char[] _chars;
  private int _offset;
  private int _line;
  private int _column;

  public Tokenizer( String string )
  {
    _string = string;
  }

  //========================================================================================
  //  Main tokenization loop
  //========================================================================================

  public List<Token> tokenize()
  {
    ArrayList<Token> tokens = new ArrayList<Token>();

    _chars = _string.toCharArray();
    _offset = 0;
    _line = 1;
    _column = 0;

    while(moreChars()) {
      eatWhiteSpace(); // eat leading whitespace

      if(!moreChars()) break; // if we got to the end of the string, exit

      Token string = consumeString();
      if(string != null)
      {
        tokens.add( string );
        continue;
      }

      Token number = consumeNumber();
      if(number != null)
      {
        tokens.add( number );
        continue;
      }

      Token punctuation = consumePunctuation();
      if(punctuation != null)
      {
        tokens.add( punctuation );
        continue;
      }

      Token constant = consumeConstant();
      if(constant != null)
      {
        tokens.add( constant );
        continue;
      }

      // unrecognized token, add error token
      tokens.add( newToken( ERROR, ">> BAD TOKEN : " + currentChar() ) );
      bumpOffset( 1 );
    }

    return tokens;
  }

  //========================================================================================
  //  Tokenization type methods
  //========================================================================================

  private Token consumeString()
  {
    //TODO - implement

    String inputstring = _string;
    while(moreChars()){
      for(int i = 0; i < _string.length(); i++){
        if(inputstring.charAt( i+1 ) == ' '){
          break;
        }else{
          //form input string here if not blank space
        }
        Token n = newToken(STRING, inputstring);
        int slength = inputstring.length();
        bumpOffset(slength);
        return n;

      }


    }
    return null;
  }


  private Token consumeNumber()
  {
    //TOOD - implement
    /*
    if( match('1')){
      Token n = newToken(NUMBER, "1");
      bumpOffset(1);
      return n;
    }
    */

    int i = 0;
    int inputnum = _chars[i];

    while(moreChars()){                //as long as there's more to read
      for(int j = 0 ; j < _chars.length; j++){
        if(_chars[j] == ' '){
          break;
        }
        if(Character.isDigit(_chars[j])){ //if what is read is an integer
          //collect info to form inputnum



        }
        Token n = newToken(NUMBER, Integer.toString(inputnum));
        int length = String.valueOf(inputnum).length();
        bumpOffset(length);
        return n;
      }
    }
    //scan.close();
    return null;
  }


  private Token consumePunctuation()
  {
    //TOOD - implement

    if( match('[')){
        Token t = newToken(PUNCTUATION, "[");
        bumpOffset(1);
        return t;
    }
    if( match(']')){
        Token t = newToken(PUNCTUATION, "]");
        bumpOffset(1);
        return t;
    }
    if( match('{')){
        Token t = newToken(PUNCTUATION, "{");
        bumpOffset(1);
        return t;
    }
    if( match('}')){
        Token t = newToken(PUNCTUATION, "}");
        bumpOffset(1);
        return t;
    }
    if( match(':')){
        Token t = newToken(PUNCTUATION, ":");
        bumpOffset(1);
        return t;
    }
    if( match(',')){
        Token t = newToken(PUNCTUATION, ",");
        bumpOffset(1);
        return t;
    }

    return null;
  }

  private Token consumeConstant()
  {
    if( match( 't', 'r', 'u', 'e' ) )
    {
      Token t = newToken( CONSTANT, "true" );
      bumpOffset(4);
      return t;
    }
    if( match( 'f', 'a', 'l', 's', 'e' ) )
    {
      Token t = newToken( CONSTANT, "false" );
      bumpOffset(5);
      return t;
    }
    if( match( 'n', 'u', 'l', 'l' ) )
    {
      Token t = newToken( CONSTANT, "null" );
      bumpOffset(4);
      return t;
    }
    return null;
  }

  //========================================================================================
  //  Utility methods
  //========================================================================================

  private void bumpOffset( int amt )
  {
    _offset += amt;
  }

  private Token newToken( Token.TokenType type, String tokenValue )
  {
    return new Token( type, tokenValue, _line, _column, _offset + 1 );
  }

  private boolean match( char... charArray)
  {
    for( int i = 0; i < charArray.length; i++ )
    {
      if( !peekAndMatch( i, charArray[i] ))
      {
        return false;
      }
    }
    return true;
  }

/*
  //function for string matching
  private boolean matchString( String charArray){
    for( int i = 0; i < charArray.length; i++){
      if( !peekAndMatch( i, charArray[i] )){
        return false;
      }
    }
    return true;
  }

*/


  private boolean peekAndMatch( int i, char toMatch )
  {
    if( _offset + i < _chars.length )
    {
      return _chars[_offset + i] == toMatch;
    } else {
      return false;
    }
  }

  private void eatWhiteSpace()
  {
    while( moreChars() && Character.isWhitespace( currentChar() ) )
    {
      char c = currentChar();
      if( c == '\n' ) // if we are at a newline character, bump the line number and reset the column
      {
        _line++;
        _column = 0;
      }
      _offset = _offset + 1; // bump offset
      _column = _column + 1; // bump column
    }
  }

  private char currentChar()
  {
    return _chars[_offset];
  }

  private boolean moreChars()
  {
    return _offset < _chars.length;
  }
}
