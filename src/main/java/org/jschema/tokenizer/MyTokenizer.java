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

  public MyTokenizer( String string )
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
    if (_chars[_offset] == '\"') {
      StringBuilder sb = new StringBuilder();
      sb.append(_chars[_offset]);
      int i = _offset + 1;
      while (i < _chars.length) {
        if(_chars[i] == '\\'){
          i++;
          if(i == _chars.length){
            sb.append('\\');
            break;
          }
          switch(_chars[i]) {
            case 'b':
              sb.append('\b');
              break;
            case 'f':
              sb.append('\f');
              break;
            case 'n':
              sb.append('\n');
              break;
            case 'r':
              sb.append('\r');
              break;
            case 't':
              sb.append('\t');
              break;
            case 'u':
              StringBuilder udigits = new StringBuilder();
              for(int j = 0; j < 4; j++){
                i++;
                if (i == _chars.length) {
                  sb.append(udigits);
                  Token t = badToken(sb.toString());
                  bumpOffset(i - _offset);
                  return t;
                }
                udigits.append(_chars[i]);
              }
              if(!isHex(udigits.toString()) || udigits.length() != 4) {
                sb.append("\\u");
                sb.append(udigits);
                i++;
                while(i <_chars.length && _chars[i] != ' '){
                  sb.append(_chars[i]);
                  Token t = badToken(sb.toString());
                  bumpOffset(i - _offset);
                  return t;
                }

              }
              sb.append(Character.toString((char)Integer.parseInt(udigits.toString(), 16)));
              break;
            default:
              // check
              sb.append(_chars[i]);
          }
          i++;
          continue;
        }
        sb.append(_chars[i]);
        if(_chars[i++] == '\"'){
          break;
        }
      }
      Token t = (sb.length() > 1 && sb.charAt(sb.length() - 1) == '\"') ?
              newToken(STRING, sb.substring(1, sb.length()-1)) :
              badToken(sb.toString());
      bumpOffset(i - _offset);
      return t;
      }
      return null;
  }

  private Token consumeNumber()
  {
    if (_chars[_offset] == '-' || Character.isDigit(_chars[_offset])) {
      StringBuilder num = new StringBuilder();
      int i = _offset;
      while (i < _chars.length && _chars[i] != ' ' && _chars[i] !=',') {
        num.append(_chars[i++]);
      }
      Token t = matchNumber(num.toString()) ?
              newToken(NUMBER, num.toString()) :
              badToken(num.toString());
      bumpOffset(i-_offset);
      return t;
      }
      return null;
  }

  private Token consumePunctuation()
  {
    if ( _chars.length > _offset && matchPunctuation(String.valueOf(_chars[_offset]))) {
      Token t = newToken( PUNCTUATION, String.valueOf(_chars[_offset]));
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

  private Token badToken(String str){
    return newToken(ERROR, ">> BAD TOKEN : " + str);
  }
  private Token newToken( Token.TokenType type, String tokenValue )
  {
    return new Token( type, tokenValue, _line, _column, _offset + 1 );
  }

  private boolean isHex(String n){
    return n.matches("[0-9A-F]+");
  }
  private boolean matchNumber(String n){

    return n.matches("(-?((\\d+\\.\\d+)|([1-9]\\d*))([eE]{1}[-+]?\\d+)?)");
  }

  private boolean matchPunctuation (String p){
    return p.matches("([\\[\\]{}:,])");
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
