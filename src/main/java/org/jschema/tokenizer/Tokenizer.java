//1,234.02

//lets not make it with regular expressions, and lets try doing it in another way, although it might be a bit weird
//it doesn't look pretty, i know ^^

package org.jschema.tokenizer;

import java.util.ArrayList;
import java.util.List;

import static org.jschema.tokenizer.Token.TokenType.*;

public class Tokenizer
{
    private String _string;//imput to be tokenized
    private char[] _chars;//imput to be tokenized
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

    while(moreChars()) {//executes for each character of the string != of white space?
        eatWhiteSpace(); // eat leading whitespace

        if(!moreChars()) break; // if we got to the end of the string, exit

        //added consumeConstant the first thing to compare, since it is made by several characters
        Token constant = consumeConstant();
        if(constant != null)
        {
            tokens.add( constant );
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

        Token string = consumeString();
        if(string != null)
        {
            tokens.add( string );
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
        //either the string starts and ends with " or is something escaped, such as \r  \"  etc
        boolean [] foundFirstEscape = new boolean[]{false};//if true, we are in a String
        Token t = null;
        String str = "";
        while (isString(foundFirstEscape)){
            if(!moreChars()) break;//it can happened that we find a " but nothing behind it
            //we know that a string has started, and that is has at least one char more
            str = str + _chars[_offset];
            bumpOffset(1);
        }

        return t;
    }

    private boolean isString(boolean[] foundFirstEscapeYet) {
        //check if in the current position there is a "
        if ( _chars[_offset] == ((int)'\"') ){
            bumpOffset(1);
            if (!foundFirstEscapeYet[0]){        //if it is the first one found, then we are in a String
                foundFirstEscapeYet[0] = true;
                return true;
            }else{        //if it is the second, the string has ended, and we reset the variable
                foundFirstEscapeYet[0] = false;
                return false;
            }
        }
        return false;
    }

    private Token consumeNumber()
    {
        Token t = null;
        if ( itIsANumber() ) {
            //if it is a number lets get it
            //since it seems that when we store it we do not differentiate between float or integer, i do not take it into consideration here
            String numb = getNumber();
            t = newToken( NUMBER, numb );

            //return Token;
        }
        return t;
    }

    private Token consumePunctuation()
    {
        Token t = null;
        if ( isItPunctuation()){
            String punc = getPunctuation();
            t = newToken( PUNCTUATION, punc );
        }

        return t;
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

    private boolean itIsANumber() {
        //determines whether the character being analysed(reading char in position _offset) is a number or not
        if ( (_chars[_offset] > 47 && _chars[_offset] < 58) || _chars[_offset] == (int)'-') {//it can be > or < than 0
          return true;
        }
        return false;
    }

    public String getNumber(){
        //gets number from the charArray and creates a string with it (work for both 123 and 12.3, both as a )
        //we only get here if the current position is a number, not if it is a dot
        String temp = "";
        boolean []foundSomething = new boolean[]{false, false, false};//0 is DOT, 1 is -, 2 is EXP
        while ( itIsANumber() ){
            if(!moreChars()) break;
            temp = temp + _chars[_offset]; //add new number to the string that will be part of the token (type NUMBER)
            bumpOffset(1);//update offset
            //if next position is a dot/-/e/E, the number continues (1 time only)
            if (!numberContinues(foundSomething) ){
                break;//is not great doing it this way, but,....
            }
        }
        return temp;
    }

    public boolean numberContinues(boolean [] haveFoundSomething){//haveFoundSomething: 0 is DOT, 1 is -, 2 is EXP
        //checks if current position is the first dot in the number
        if(!moreChars()) return false;
        if (_chars[_offset] == 46 && !haveFoundSomething[0]) {
            haveFoundSomething[0] = true;
            return true;
        }else if (_chars[_offset] == (int)'-' && !haveFoundSomething[1]){
            haveFoundSomething[1] = true;
            return true;
        }else if (_chars[_offset] == (int)'e' && !haveFoundSomething[2]){
            haveFoundSomething[2] = true;
            return true;
        }else if (_chars[_offset] == (int)'E' && !haveFoundSomething[2]){
            haveFoundSomething[2] = true;
            return true;
        }
        return false;
    }

    public boolean isItPunctuation(){
        //checks if the current possition is one of this    [  ]  {  }  ,  :
        //we could have a loop with an array containing what is considered punctuation, but for now this works
        switch (_chars[_offset]){
            case 44:case 58:case 91:case 93:case 123:case 125: return true;
            default: return false;
        }
    }

    public String getPunctuation(){
        //creates and returns a string containig the punctuation found in the current position
        //it seems form the test that (for example) each [ is a different token
        bumpOffset(1);
        return "" + _chars[_offset - 1];
    }

}
