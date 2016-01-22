package org.jschema.tokenizer;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

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
      String tok="";


      int newOffset=0;
      //check if starts with quote
    if(_chars[_offset]=='"'){
        tok+=_chars[_offset+newOffset];
        newOffset++;
        //go until a space is found
        //should cover case with escaped quotes inside string
        while(_offset+newOffset<_chars.length
                &&_chars[_offset+newOffset]!=':' &&_chars[_offset+newOffset]!=','){
            tok+=_chars[_offset+newOffset];
            //escaped " case
            if(_chars[_offset+newOffset]=='\\' && _offset+newOffset+1<_chars.length &&_chars[_offset+newOffset]=='"'){
                newOffset++;
            }
            newOffset++;
            //break if last " is found and next letter is space
            if(_chars[_offset+newOffset-1]=='"' &&_offset+newOffset<_chars.length && !Character.toString(_chars[_offset+newOffset]).equals(" ")) {
                newOffset++;
                break;
            }


        }

        //check to make sure ends in quote and is not a single quote
        if(!tok.substring(tok.length()-1).equals("\"") || tok.length()==1){
            bumpOffset(newOffset);
            return newToken(ERROR,">> BAD TOKEN : " + tok);
        }else{
            bumpOffset(newOffset);

            return newToken(STRING,tok);
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
    //checks exponents to make sure they are valid
    private Token checkValidExp(int offset, String tok, boolean isDec) {
        Token t=newToken(NUMBER, tok);;
        String regex = "-?[0-9]+";
        //check if number before e
        if(!(isDec && tok.substring(0,offset).equals(""))) {
            //make sure isn't small decimal like .23
            if ((offset - 1 < 0) || !(tok.substring(0, offset).matches(regex))) {
                t = newToken(ERROR, ">> BAD TOKEN : " + tok);

                //check if number or negative sign after e
                //TODO need to check more for validity
            } else {
                //get anything after token
                int offsetAfter = 0;
                String badTok = tok;
                //System.out.println("tok is" +tok.length());
                //get any bad input after exponent
                while (tok.length() + _offset+ offsetAfter < _chars.length &&
                        !Character.toString(_chars[_offset+tok.length() + offsetAfter]).equals(" ")&&
                        _chars[_offset+tok.length() + offsetAfter]!=',') {
                    badTok += _chars[tok.length() + offsetAfter];
                    offsetAfter++;
                }
                bumpOffset(offsetAfter);
                if (offsetAfter > 0) {
                    t = newToken(ERROR, ">> BAD TOKEN : " + badTok);
                } else {
                    t = newToken(NUMBER, tok);
                }
            }
        }
        return t;
    }

}
