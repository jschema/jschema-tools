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
        System.out.println(currentChar());
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
    return null;
  }

    //needs to work for integers and decimals
  private Token consumeNumber()
  {
      String token="";
      //whether there is a decimal or not
      int isDecimal=0;
      int offset=0;
      while( _offset+offset < _chars.length && (Character.isDigit(_chars[_offset+offset])||_chars[_offset+offset]=='.'||_chars[_offset+offset]=='/')){
          token +=_chars[_offset+offset];
          //don't need to check if number after decimal->valid to have 20. in javascript
          if(_chars[_offset+offset]=='.'){
              isDecimal++;
          }
          offset++;
      }
      if(!token.equals("")){
          //in case multiple decimals appear from invalid input
          Token t;
          //invalid number token
          if(isDecimal>1){
              t= newToken( ERROR, ">> BAD TOKEN : " + token);
              //make sure that fraction isn't null before or after it
          }else {
              t = newToken(NUMBER, token);
          }
          bumpOffset(offset);
          return t;
      }
    return null;
  }

  private Token consumePunctuation()
  {
      //Square brackets, curly brackets, comma, colon
    switch(_chars[_offset]){
        case '{': return createPuncToken("{");
        case '}': return createPuncToken("}");
        case '[': return createPuncToken("[");
        case ']': return createPuncToken("]");
        case ',': return createPuncToken(",");
        case ':': return createPuncToken(":");
        default: break;
    }
    return null;
  }

    private Token createPuncToken(String token){
        Token t = newToken( PUNCTUATION, token );
        bumpOffset(1);
        return t;
    }
    //handles case where there is a partial match to "true" or "false"
    private Token invalidConstant(int offset) {
        if (((_offset + offset) < _chars.length) && !(Character.toString(_chars[_offset + offset]).equals(" "))) {
            String token = "true";
            int offsetNew = offset;
            while (_offset + offsetNew < _chars.length) {
                token += _chars[_offset + offsetNew];
                offsetNew++;
            }
            Token t = newToken(ERROR, ">> BAD TOKEN : " + token);
            bumpOffset(offsetNew);
            return t;
        }else{
            return null;
        }
    }
  private Token consumeConstant()
  {
    if( match( 't', 'r', 'u', 'e' ) )
    {
        Token t=invalidConstant(4);
        if(t!=null){
            return t;
        }else {
            t = newToken(CONSTANT, "true");
            bumpOffset(4);
            return t;
        }
    }
    if( match( 'f', 'a', 'l', 's', 'e' ) )
    {
        Token t=invalidConstant(5);
        if(t!=null){
            return t;
        }else {
            t = newToken(CONSTANT, "false");
            bumpOffset(5);
            return t;
        }
    }
    if( match( 'n', 'u', 'l', 'l' ) )
    {
        Token t=invalidConstant(4);
        if(t!=null){
            return t;
        }else {
            t = newToken(CONSTANT, "null");
            bumpOffset(4);
            return t;
        }
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
    //while there exists more characters and the current character is white space
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
