package org.jschema.tokenizer;

import java.util.ArrayList;
import java.util.List;

import static org.jschema.tokenizer.Token.TokenType.*;

public class Tokenizer
{
  private String _string;
  private char[] _chars;
  private int _offset;
  private int _line;
  private int _column;

  public Tokenizer(String string )
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
    if( match( 't', 'r', 'u', 'e' ) )
    {
    return null;
  }
    if( match( 'f', 'a', 'l', 's', 'e' ) )
    {
      return null;
    }
    if( match( 'n', 'u', 'l', 'l' ) )
    {
      return null;
    }
    if(isPunc(currentChar())){
      return null;
    }
    if(isNumber(currentChar())){
      return null;
    }
    if(currentChar() == '-'){
      return null;
    }


    String t = "";
    Token token;
    while(moreChars()){
      if(currentChar() == ' '){
        break;
      }
      if(isPunc(currentChar())){
        break;
      }
      if(currentChar() == '\\'){
        bumpOffset(1);
        if(currentChar() == '"'){
          t += '"';
          bumpOffset(1);
          continue;
        }
        if(currentChar() == 'b'){
          t += '\b';
          bumpOffset(1);
          continue;
        }
        if(currentChar() == 'f'){
          t += '\f';
          bumpOffset(1);
          continue;
        }
        if(currentChar() == 'n'){
          t += '\n';
          bumpOffset(1);
          continue;
        }
        if(currentChar() == 'r'){
          t += '\r';
          bumpOffset(1);
          continue;
        }
        if(currentChar() == 'u'){
          int j;
          String uni = "\\u";
          for(j = 0; j < 5; j++){
            bumpOffset(1);
            if(isHexDigit(currentChar())) {
              uni += currentChar();
            }
          }
          char c = (char) Integer.parseInt(uni.substring(2), 16);
          t += c ;
          continue;
        }
      }
      t += currentChar();
      bumpOffset(1);
    }

    if(t.charAt(t.length() - 1) == '"'){
      if(t.charAt(0) == '"') {
        t = t.substring(1, t.length() - 1);
      }
      else{
        token = newToken(ERROR, ">> BAD TOKEN : " +t);
        return token;
      }
    }
    else{
      token = newToken(ERROR, ">> BAD TOKEN : " +t);
      return token;
    }
    token = newToken(STRING, t);
    if(!t.equals("")){
      return token;
    }
    return null;
  }

  private Token consumeNumber()
  {
    if( match( 't', 'r', 'u', 'e' ) )
    {
      return null;
    }
    if( match( 'f', 'a', 'l', 's', 'e' ) )
    {
      return null;
    }
    if( match( 'n', 'u', 'l', 'l' ) )
    {
      return null;
    }
    if(isPunc(currentChar())){
      return null;
    }
    String t = "";
    int dotcount = 0;
    Token token;
    int count = 0;
    while(moreChars()){
      if(isPunc(currentChar())){
        break;
      }
      if(isLetter(currentChar()) && currentChar() != 'e' && currentChar() != 'E'){
        break;
      }
      if(currentChar() == ' '){
        break;
      }
      if(count == 0 && currentChar() == '0'){
        bumpOffset(1);
        if(isNumber(currentChar())){
          t = "0";
          break;
        }
        else{
          bumpOffset(-1);
        }
      }
      count++;
      if (currentChar() == '.'){
        t += currentChar();
        dotcount++;
      }
      if(currentChar() == '-' || currentChar() == '+'){
        t +=currentChar();
      }
      if(isNumber(currentChar())) {
        t += currentChar();
      }
      if(currentChar() == 'e' || currentChar() == 'E'){
        t += currentChar();
      }
      bumpOffset(1);
    }

    if (dotcount > 1) {
      token = newToken(ERROR, ">> BAD TOKEN : " + t);
      return token;
    }
    token = newToken(NUMBER, t);
    return token;
  }

  private Token consumePunctuation()
  {
    if(match('['))
    {
      Token t = newToken(PUNCTUATION, "[");
      bumpOffset(1);
      return t;
    }
    if(match(']'))
    {
      Token t = newToken(PUNCTUATION, "]");
      bumpOffset(1);
      return t;
    }
    if(match('{'))
    {
      Token t = newToken(PUNCTUATION, "{");
      bumpOffset(1);
      return t;
    }
    if(match('}'))
    {
      Token t = newToken(PUNCTUATION, "}");
      bumpOffset(1);
      return t;
    }
    if(match(':'))
    {
      Token t = newToken(PUNCTUATION, ":");
      bumpOffset(1);
      return t;
    }
    if(match(','))
    {
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
      Token t = newToken(CONSTANT, "true");
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

  private boolean isNumber(char c){
    if(Character.isDigit(c)){
      return true;
    }
    return false;
  }
  private boolean isLetter(char c){
    if(Character.isLetter(c)){
      return true;
    }
    return false;
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

  private Token appendToken(Token.TokenType type, String tokenValue,  String newValue)
  {
    Token token = newToken(type, tokenValue + newValue );
    return token;
  }


  private char currentChar()
  {
    return _chars[_offset];
  }

  private boolean moreChars()
  {
    return _offset < _chars.length;
  }

  private boolean isPunc(char ch){
    if(ch == ':' || ch == '{' || ch == '}' || ch == '[' || ch == ']' || ch ==','){
      return true;
    }
    return false;
  }

  private boolean isHexDigit(char ch) {
    return ch >= '0' && ch <= '9' || ch >= 'A' && ch <= 'F' || ch >= 'a' && ch <= 'f';
  }

}