package org.jschema.tokenizer;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.jschema.tokenizer.Token.TokenType.*;

public class Tokenizer
{
  private String _string;
  private char[] _chars;
  private int _offset;
  private int _line;
  private int _column;
    private Pattern patternString;
    private Pattern patternNum;

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

      String pattern ="^\"((\\\\[\"\\\\/\b\f\n\r\t]|(\\\\[u][0-9a-fA-F]{4}))|[^\"\\\\])*\"$";
      patternString=Pattern.compile(pattern);

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
      // unrecognized token, add error token (complete string)
        if(_offset+1<_chars.length) {
            int offset=0;
            String tok="";
            while(_offset+offset<_chars.length && !Character.toString(_chars[_offset+offset]).equals(" ")) {
               tok+=_chars[_offset+offset];
                offset++;
            }
            tokens.add(newToken(ERROR, ">> BAD TOKEN : " + tok));
            bumpOffset(offset);
        }
    }

    return tokens;
  }

  //========================================================================================
  //  Tokenization type methods
  //========================================================================================

  private Token consumeString()
  {
      String tok=""; //for actual escaped token

      String input=""; //for checking with regex
      int offset=0;
    //first make sure that string starts with quote
      //loop through characters until ',',':', ']','}',or ' '
      //if escaped character is found, go to helper function, handle token, return valid character
      //add character to token that will be returned
      //then check entire token for validity before returning
      //make sure it starts with a quote
      if(_chars[_offset+offset]=='"') {
          tok += _chars[_offset + offset];
          input += _chars[_offset + offset];
          offset++;
          //look at following chars that aren't end of string and not at end of string
          while (_offset + offset < _chars.length && !isEndOfString(_chars[_offset + offset])) {
              //if backslash, handle special case
              if (_chars[_offset + offset] == '\\') {
                  //if u, handle unicode
                  if (_offset + offset + 1 < _chars.length && _chars[_offset + offset + 1] == 'u') {
                      offset++;
                      String uni = unicode(offset);
                      if (uni != null) {
                          try {
                              char token = (char) Integer.parseInt(uni.substring(2), 16);
                              tok += token;
                              input += uni;
                              offset += 5;
                          } catch (NumberFormatException e) {
                             // tok += uni;
                              input += uni;
                              offset += 5;
                          }
                      } else {
                          input+="\\"+_chars[offset];
                          offset++;
                          //invalid unicode, so just grab rest of string and break out of loop
                          while(_offset+offset<_chars.length && !isEndOfString(_chars[_offset + offset])){
                              input+=_chars[_offset+offset];
                              offset++;
                          }
                          break;
                      }
                      //not unicode
                  } else {
                      String temp = unescapeChar(offset);
                      //not unescaped character
                      if (temp != null) {
                          tok += temp;
                          input += "\\" + temp;
                          offset += 2;
                      } else {
                          //invalid character escaping, so just grab rest of string and break out of loop
                          while(_offset+offset<_chars.length && !isEndOfString(_chars[_offset + offset])){
                              input+=_chars[_offset+offset];
                              offset++;
                          }
                          break;
                      }
                  }
              } else {
                  tok += _chars[_offset + offset];
                  input += _chars[_offset + offset];
                  offset++;
              }
          }
          bumpOffset(offset);
          Matcher m = patternString.matcher(input);
          if (m.find()) {
              return newToken(STRING, tok.substring(1, tok.length() - 1));
          } else {
              //check if lack of matching parenthesis (on right hand side) is cause of no match
              if (input.charAt(input.length() - 1) != '"') {
                  return newToken(ERROR, ">> BAD TOKEN : " + input.substring(0, input.length()));
              } else {
                  return newToken(ERROR, ">> BAD TOKEN : " + input.substring(1, input.length() - 1));
              }
          }
      }

      return null;

  }


    //needs to work for integers and decimals
  private Token consumeNumber()
  {
      String token="";
      String regex="(^[\\-]?((0\\.[1-9]+)|([1-9]+\\.[0-9]+)|([1-9]\\d*))([eE][+-]?[0-9]+)?$)";
      int offset=0;
      //if it looks like it wants to be a number
      if(Character.isDigit(_chars[_offset+offset])||_chars[_offset]=='.'||_chars[_offset]=='-') {
          //Add chars to token until " " or ',' or last character
          while (_offset + offset < _chars.length && _chars[_offset+offset]!=',' && !Character.toString(_chars[_offset+offset]).equals(" ")) {
              token += _chars[_offset + offset];
              offset++;
          }
          //bump offset regardless of whether token is valid or not
          bumpOffset(offset);
          if(token.matches(regex)){
              return newToken(NUMBER,token);
          }else{
              return newToken(ERROR,">> BAD TOKEN : " + token);
          }
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
    //creates tokens for punctuation
    private Token createPuncToken(String token){
        Token t = newToken( PUNCTUATION, token );
        bumpOffset(1);
        return t;
    }
    //handles case where there is a partial match to "true" or "false"
    private Token invalidConstant(int offset) {
        if (((_offset + offset) < _chars.length) && _chars[_offset + offset]!=','
                && !(Character.toString(_chars[_offset + offset]).equals(" "))) {
            String token = "true";
            int offsetNew = offset;
            while (_offset + offsetNew < _chars.length && _chars[_offset + offsetNew]!=','
                    &&!(Character.toString(_chars[_offset + offsetNew]).equals(" "))) {

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
    //convert to unicode
    private String unicode(int offset){
        int newOffset=_offset+offset+1;
        int hexDigs=4;
        String tok="\\u";
        while(newOffset<_chars.length && hexDigs>0){
            tok+=_chars[newOffset+_offset];
            newOffset++;
            hexDigs--;
        }
        if(hexDigs==0){
            return tok;
        }
        return null;

    }
    //checks if end of string
    private boolean isEndOfString(char endOfString){
        if (endOfString==','||endOfString==':'||endOfString==']'||endOfString=='}'){
            return true;
        }
        return false;
    }
    //unescapes characters
    private String unescapeChar(int curr_offset){
        int newOffset=_offset+curr_offset+1;
        //check to see if next character is " \ / b f n r t
        if(newOffset<_chars.length){
            switch(_chars[newOffset]){
                case '"': return "\"";
                case '\\': return "\\";
                case '/': return "/";
                case 'b': return "\b";
                case 'f': return "\f";
                case 'n': return "\n";
                case 'r': return "\r";
                default: return null;
            }

        }

        return null;
    }

}
