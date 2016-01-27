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

    private Pattern _numberPattern;
    private Pattern _punctuationPattern;
    private Pattern _constantPattern;
    
    private Pattern _validStringPattern;
    private Pattern _invalidStartingInQuoteStringPattern;
    private Pattern _invalidEndingInQuoteStringPattern;

    private Pattern _unicodePattern;
    private Pattern _potentialUnicodePattern;

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

        // Pattern compilation is expensive, only do it once
        _numberPattern = Pattern.compile("(-?(?:0|[1-9](?:\\d+)?+)(?:\\.+[\\d]+)?(?:[E|e][+|-]?[\\d]+)?)");
        _punctuationPattern = Pattern.compile("(\\[|\\]|\\{|\\}|:|,)");
        _constantPattern = Pattern.compile("\\w+(?!\\w)(?!\")");

        String innerRegex = "((?:[^\\\\]|\\\\\"|\\\\\\\\|\\\\\\/|\\\\[b|f|n|r|t]|\\\\u(?:\\w{4}))*)";
        _invalidStartingInQuoteStringPattern = Pattern.compile("\"" + innerRegex);
        _invalidEndingInQuoteStringPattern = Pattern.compile(innerRegex + "\"");
        _validStringPattern = Pattern.compile("\"" + innerRegex + "\"");

        _unicodePattern = Pattern.compile("\\\\u(\\p{XDigit}{4})");
        _potentialUnicodePattern = Pattern.compile("\\\\u\\w{4}");

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
        String startingInQuoteRegex = matchRegex(_invalidStartingInQuoteStringPattern);
        String endingInQuoteRegex = matchRegex(_invalidEndingInQuoteStringPattern);
        String validRegex = matchRegex(_validStringPattern);

        if (validRegex != null) {
            bumpOffset(validRegex.length());

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

            int unicodeErrorCount = countBadUnicode(validRegex);
            if (unicodeErrorCount == 0) {
                validRegex = replaceUnicode(validRegex);
            } else {
                return newToken(ERROR, ">> BAD TOKEN : " + validRegex);
            }

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
        String value = matchRegex(_numberPattern);
        if (value != null) {
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
        String value = matchRegex(_punctuationPattern);
        if (value != null) {
            Token t = newToken(PUNCTUATION, value);
            bumpOffset(1);
            return t;
        }
        return null;
    }

    private Token consumeConstant()
    {
        String constant = matchRegex(_constantPattern);
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

    private String matchRegex(Pattern pattern) {
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

    private int countBadUnicode(String input) {
        Matcher validMatcher = _unicodePattern.matcher(input);
        Matcher potentialMatcher = _potentialUnicodePattern.matcher(input);
        return countMatches(potentialMatcher) - countMatches(validMatcher);
    }

    private int countMatches(Matcher matcher) {
        int count = 0;
        while (matcher.find())
            count++;
        return count;
    }

    private String replaceUnicode(String input) {
        Matcher matcher = _unicodePattern.matcher(input);
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