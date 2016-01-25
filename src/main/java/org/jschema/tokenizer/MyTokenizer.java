package org.jschema.tokenizer;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        String innerRegex = "((?:[^\\\\]|\\\\\"|\\\\\\\\|\\\\\\/|\\\\[b|f|n|r|t]|\\\\u(?:[0-9A-Fa-f]{4}))*)";
        String startingInQuoteRegex = matchRegex("\"" + innerRegex);
        String endingInQuoteRegex = matchRegex(innerRegex + "\"");
        String validRegex = matchRegex("\"" + innerRegex + "\"");

        if (validRegex != null) {
            int originalValueLength = validRegex.length();

            validRegex = validRegex.substring(1, validRegex.length() - 1);

            // Escaped quote
            validRegex = validRegex.replace("\\\"", "\"");

            // Escaped front slash
            validRegex = validRegex.replace("\\/", "/");

            // Escaped back slash
            validRegex = validRegex.replace("\\\\", "\\");

            // Special characters
            validRegex = validRegex.replace("\\b", "\b");
            validRegex = validRegex.replace("\\f", "\f");
            validRegex = validRegex.replace("\\n", "\n");
            validRegex = validRegex.replace("\\r", "\r");

            validRegex = replaceUnicode(validRegex);

            bumpOffset(originalValueLength);
            return newToken(STRING, validRegex);

        } else if (startingInQuoteRegex != null) {
            bumpOffset(startingInQuoteRegex.length());
            return newToken(ERROR, ">> BAD TOKEN : " + startingInQuoteRegex);
        } else if (endingInQuoteRegex != null) {
            return null;
        }
        return null;
    }

    private Token consumeNumber()
    {
        String value = matchRegex("(-?(?:0|[1-9](?:\\d+)?+)(?:\\.+[\\d]+)?(?:[E|e][+|-]?[\\d]+)?)");
        if (value != null) {
            int originalValueLength = value.length();

            // Check invalid decimal
            int dotCount = 0;
            for (int i = 0; i < value.length(); i++) {
                if (value.charAt(i) == '.') {
                    dotCount++;
                }
            }

            Token t = newToken(NUMBER, value);
            if (dotCount > 1) {
                t = newToken(ERROR, ">> BAD TOKEN : " + value);
            }

            bumpOffset(value.length());
            return t;
        }
        return null;
    }

    private Token consumePunctuation()
    {
        String value = matchRegex("(\\[|\\]|\\{|\\}|:|,)");
        if (value != null) {
            Token t = newToken(PUNCTUATION, value);
            bumpOffset(1);
            return t;
        }
        return null;
    }

    private Token consumeConstant()
    {
        String constant = matchRegex("\\w+");
        if (constant == null) {
            return null;
        }

        bumpOffset(constant.length());
        if (constant.equals("true") || constant.equals("false") || constant.equals("null")) {
            return newToken(CONSTANT, constant);
        }

        return newToken(ERROR, ">> BAD TOKEN : "+constant);
    }

    //========================================================================================
    //  Utility methods
    //========================================================================================

    private void bumpOffset( int amt )
    {
        _offset += amt;
    }

    private Token newToken( Token.TokenType type, String tokenValue ) {
        return new Token(type, tokenValue, _line, _column, _offset + 1);
    }

    private boolean matchString(String string) {
        for( int i = 0; i < string.length(); i++ )  {
            if( !peekAndMatch( i, string.charAt(i) ))  {
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

    private String matchRegex(String patternString) {
        Pattern pattern = Pattern.compile(patternString);

        // Build string to match from current position
        StringBuffer remainingCharsBuffer = new StringBuffer(_chars.length);
        for (int i = _offset; i < _chars.length; i++){
            remainingCharsBuffer.append(_chars[i]);
        }
        String remainingCharsString =  remainingCharsBuffer.toString();

        // Get matches and ensure first match exists at beginning of string
        Matcher matcher = pattern.matcher(remainingCharsString);
        if (matcher.find()) {
            boolean didMatch = matchString(matcher.group(0));
            return didMatch ? matcher.group(0) : null;
        } else {
            // No match
            return null;
        }
    }

    private String replaceUnicode(String input) {
        Pattern pattern = Pattern.compile("\\\\u(\\p{XDigit}{4})");
        Matcher matcher = pattern.matcher(input);
        StringBuffer buffer = new StringBuffer(input.length());
        while (matcher.find()) {
            String character = String.valueOf((char)Integer.parseInt(matcher.group(1), 16));
            matcher.appendReplacement(buffer, Matcher.quoteReplacement(character));
        }
        matcher.appendTail(buffer);
        return buffer.toString();
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