package org.jschema.tokenizer;

import java.util.ArrayList;
import java.util.List;
import java.lang.*;

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
/*
    //prevent consumerString from identifying possible STRINGS to be CONSTANTS
    if( match('t','r','u','e') || match('f','a','l','s','e') || match('n','u','l','l')){
      return null;
    }

    Token s = newToken(STRING, "");


    if(_chars[0] == '\"'){          //if first thing read is an open quote, read for letters.
      bumpOffset(1);
      while(Character.isLetter(currentChar())){
        s = appendT(STRING, s.getTokenValue(), "" + currentChar());
        bumpOffset(1);
      }
      if(_chars[_offset] == '\"'){                //if open & close matches after all letters are read.
        return s;
      }else {                                      //if last char != '\"'
        return newToken(ERROR, ">> BAD TOKEN : " + currentChar());
      }
    }else{                                          //if _char[0] != '\"' and last char == '\"'
      while(Character.isLetter(currentChar())){
        s = appendT(STRING, s.getTokenValue(), "" + currentChar());
        bumpOffset(1);
      }
      if(_chars[_offset] == '\"'){                      //return an error token for each char read.
        for(int i = 0; i < _chars.length; i++) {
          return newToken(ERROR, ">> BAD TOKEN : " + _chars[i]);
        }
      }
    }

    // b, f, n, r

      if(currentChar() == 'b' || currentChar() == 'f'
              || currentChar() == 'n' || currentChar() == 'r'){
        s = appendT(STRING, s.getTokenValue(), "\"" + currentChar());
        bumpOffset(1);
      }

      //new code for string test cases.
      if(currentChar() == '"'){
        s = appendT(STRING, s.getTokenValue(), "" + currentChar());
      }else{         //if no quote
        s = newToken(ERROR, ">> BAD TOKEN : " + currentChar());
      }
      bumpOffset(1);

      if(!s.getTokenValue().equals("")) {
        return s;
      }
      */
    return null;
  }


/*
  private boolean isHex(char chr){
    return chr >= '0' && chr <= '9' || chr >= 'A' && chr <= 'F' || chr >= 'a' && chr <= 'f';
  }*/


  private Token consumeNumber()
  {


    //System.out.print("Inside consume number method");
    //TOOD - implement

    boolean digitExist = false;
    boolean dotExist = false;
    boolean negativeExist = false;           //boolean positive/negative flag

    Token t = newToken(NUMBER, "");

    if (currentChar() == '-'){                       //negative number, continue
      negativeExist = true;
      t = appendT(NUMBER, t.getTokenValue(), "" + currentChar());
      bumpOffset(1);
    }

    //while (true) {

      //System.out.println("inside while true");
      while (Character.isDigit(currentChar())) {
        //System.out.println("I came inside isDigit true");
        digitExist = true;                        //digit exists  ex: "0.12" -> 0 exists.
        t = appendT(NUMBER, t.getTokenValue(), "" + currentChar());
        bumpOffset(1);
        if (!moreChars()) {
          break;
        }

        if (digitExist) {
          if (currentChar() == '.') {
            dotExist = true;
            t = appendT(NUMBER, t.getTokenValue(), "" + currentChar());
            bumpOffset(1);
          }
        } else {
          //if no digit before dot.
          return newToken(ERROR, ">> BAD TOKEN : " + currentChar());
        }
      }
/*
        if(digitExist && dotExist) {
            if (currentChar() == '.') {              //if another dot is read.
              return newToken(ERROR, ">> BAD TOKEN : " + currentChar());
            }
        }


      if (dotExist && currentChar() == '.') {              //if another dot is read.
        return newToken(ERROR, ">> BAD TOKEN : " + currentChar());
      }

      if (currentChar() == 'e' || currentChar() == 'E') {            //don't return error if e or E for euler's number
        boolean negativeExp = false;                                //flag for exponent
        bumpOffset(1);
        if (currentChar() == '-') {                                      //read for negative sign in front of exponent
          negativeExp = true;
          t = appendT(NUMBER, t.getTokenValue(), "" + currentChar());
          bumpOffset(1);
        } else if (currentChar() == '+') {
          t = appendT(NUMBER, t.getTokenValue(), "" + currentChar());
          bumpOffset(1);
        }

        if(negativeExp && currentChar() == '-'){                    //if another negative sign exists after one is read after e/E.
          return newToken(ERROR, ">> BAD TOKEN : " + currentChar());     //this check might not be necessary
        }

        if (Character.isDigit(currentChar())) {                    //append digits for exponential value
          while (Character.isDigit(currentChar())) {
            t = appendT(NUMBER, t.getTokenValue(), "" + currentChar());
            bumpOffset(1);
          }
        }
      }

*/

      if (!t.getTokenValue().equals("")) {
        return t;
      }
      //return null;
      //break;



    return null;
  }



  //Append token method for tokens used in NUMBER and STRING
  private Token appendT(Token.TokenType type, String curVal, String newVal){
    Token t = newToken(type, curVal + newVal); //string 1 + string 2
    return t;
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
