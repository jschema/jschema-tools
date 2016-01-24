package org.jschema.tokenizer;

import java.util.ArrayList;
import java.util.List;

import static org.jschema.tokenizer.Token.TokenType.*;


public class MyTokenizer
{
  private String _string;
  private char[] _chars;
  private int _offset;
  private int _line;
  private int _column;



  public MyTokenizer(String string )
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

      /********************added for error implementation **************/

      Token error = consumeError();
      if(error != null){
        // unrecognized token, add error token
        tokens.add( newToken( ERROR, ">> BAD TOKEN : " + currentChar() ) );
        bumpOffset( 1 );
        continue;
      }

    }

    return tokens;
  }

  //========================================================================================
  //  Tokenization type methods
  //========================================================================================

  private Token consumeString()
  {

    //prevent consumerString from identifying possible STRINGS to be CONSTANTS
    if( match('t','r','u','e') || match('f','a','l','s','e') || match('n','u','l','l')){
      return null;
    }

    Token s = newToken(STRING, "");


      while(Character.isLetter(currentChar())){                      //if what we're reading currently is a letter...

        _chars[_offset] = currentChar();   //array to add each char to, later check if char at that position is '\*', increment i.
        _offset++;

        s = appendT(STRING, s.getTokenValue(), "" + currentChar());
        bumpOffset(1);

        if(!moreChars()){
          break;
        }

      }

      if(_chars[0] == '\"' && _chars[_offset - 1] == '\"'){
        //if both the starting position and ending position is '\"', basic string test should pass
        //return the token
        s = appendT(STRING, s.getTokenValue(), "" + currentChar());
      }

      /*if((_checkString[0] == '"' && _checkString[_cs-1] != ' ')
            || (_checkString[0] != ' ' && _checkString[_cs-1] == '"')){
        e = appendT( ERROR, e.getTokenValue(), ">> BAD TOKEN : " + currentChar());
      }*/

      if(!s.getTokenValue().equals("")) {
        return s;
      }
    return null;
  }


  private Token consumeError(){
    Token e = newToken(ERROR, "");

    if(!e.getTokenValue().equals("")){
      return e;
    }

    return null;
  }




  private Token consumeNumber()
  {
    //TOOD - implement

    Token t = newToken(NUMBER, "");
    Token e = newToken(ERROR, "");

    if( currentChar() == '.'){
      //if the first thing read is a '.' , return error token, then continue reading. Do below.
      e = appendE(ERROR, e.getTokenValue(), ">> BAD TOKEN : " + currentChar() );

      bumpOffset(1);
    }
    if( currentChar() == '-'){
      //negative number, continue
      bumpOffset(1);
    }
    if( Character.isLetter(currentChar()) ){
      //if it's a letter, return error token.
      e = appendE(ERROR, e.getTokenValue(), ">> BAD TOKEN : " + currentChar() );
      bumpOffset(1);
    }

       while( Character.isDigit(currentChar())){ //if what is read is an integer and not a char
         t = appendT(NUMBER, t.getTokenValue(), "" + currentChar()); // "" + "1" = "1" ...
          bumpOffset(1);

          if(!moreChars()){
            break;
          }
        }

        if( !t.getTokenValue().equals("") ) {
          return t;
        }
        if( !e.getTokenValue().equals("") ){
          return e;
        }
    return null;
  }



  //appendT method for tokens used in NUMBER and STRING
  private Token appendT(Token.TokenType type, String curVal, String newVal){
    Token t = newToken(type, curVal + newVal); //string 1 + string 2
    return t;
  }

  //appendE method for error tokens....not sure if this is necessary
  private Token appendE(Token.TokenType type, String curVal, String newVal){
    Token e = newToken(type, curVal + newVal); //string 1 + string 2
    return e;
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
    if( match('[',']')){
        Token t = newToken(PUNCTUATION, "[]");
        bumpOffset(2);
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
