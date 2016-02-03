// "asdf/badUnicode..." that is a bad token wor the whole string
// /badUnicode ONE error with that unicode
// what if "/u1234 /123W" what error do we have?

//if we find a \"\" we return a STRING token with empty string
//it does not detect wrong escaped commands. It could, but is not done (such as \q \w \e ......)


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
                //since for unicode we have to add 2 tokens, either we have the variable tokens as parameter of consumeString(), or we check if is unicode here, and add 2 tokens if it is
                //String strToken = string.getTokenValue();
                //if (isEscapedUnicode(strToken)){
                //    if (isUnicodeCorrect(strToken)){
                //        System.out.println("HA DETECTADO UNICODE CORRECTO");
                //        //tokens.add( string );
                //        tokens.add( newToken(STRING,"\u263A"));
                //        System.out.println(string.getTokenValue());
                //        //tokens.add( newToken(STRING, "" + strToken.toString().charAt(0) + "" ) );
                //    }else{//unicode is incorrect
                //        System.out.println("SI QUE HA DETECTADO QUE ESTA MAL HECHO el unicode");
                //        tokens.add( newToken( ERROR, ">> BAD TOKEN : "  ) );
                //        tokens.add( newToken( ERROR, ">> BAD TOKEN : "  ) );
                //    }
                //}else {
                tokens.add( string );
                //}

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

    private Token consumeString(){
        //Empty STRING token is possible
        //something escaped withing a string, is NOT recognised as something different, is just part of the string
        // \" hey \"  will be a string ( hey )
        // \" hey \b \"  will be a string ( hey \b )

        Token t = null;
        String []str = new String[2];
        //str[0] = if it is error or not
        //str[1] = actual string that we read
        if (isQuotationMark()){
            str = getString();
        }else{
            return t;
        }
        //at this point we know we found at least a " but we don't know if it had a closure ", or if it is an empty String
        if (str[0] == null){
            //if it was not correct (only option so far is with unicode) wew do not consume current position
            t = newToken( ERROR, ">> BAD TOKEN : " + str[1] );
            return t;
        }else if (moreChars() && isQuotationMark()){
            //if  current position is " , it was a correct String (we found the second " and we close the string)
            t = newToken(STRING, str[1]);
            bumpOffset(1);//we consume the second "
            return t;
            //}else if (moreChars() && foundEscape()){// backslash
            //    mmmmmmmmmmm
        }else {// moreChars()== true & isQuotationMark== false should not be possible, but is NOT controlled.
            //we reached the end of the input without a second "
            //so we did start the string and never finished it
            //so the error string must contain a " at the beginning
            //str[1] = "\"" + str[1];
            t = newToken(ERROR, ">> BAD TOKEN : " + str[1]);
            return t;
        }
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
        //  System.out.println("Just Bumped:  " + _chars[_offset]);
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
            if(!moreChars()) {
                break;
            }
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
        if(!moreChars()) {
            return false;
        }else if (itIsANumber()){//if it is a number, we just keep going
            return true;
        }else if (_chars[_offset] == 46 && !haveFoundSomething[0]) {// dot
            haveFoundSomething[0] = true;
            return true;
        }else if (_chars[_offset] == (int)'-' && !haveFoundSomething[1]){// -
            haveFoundSomething[1] = true;
            return true;
        }else if (_chars[_offset] == (int)'e' && !haveFoundSomething[2]){// e
            haveFoundSomething[2] = true;
            return true;
        }else if (_chars[_offset] == (int)'E' && !haveFoundSomething[2]){// E
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


    private boolean isQuotationMark(){//tests if the current position is a " AND previous was not a backslash
        //if previous char was a \, a " found in the current position would be escaped, and is not consider end of string
        //not the most elegant way of doing it...
        if ( _chars[_offset] != (int)'\"'/* ||  ( (_offset - 1 >= 0) &&  _chars[_offset - 1] == ((int)'\\') ) */) {
            return false;
        }else {
            return true;
        }
    }

    public String[] getString(){//returns the string, from the first " to a second " or to the end of the input
        bumpOffset(1);//so we start by consuming the "
        String [] str = new String[2];
        str[0] = "Milhouse: But my mom says i'm cool";
        String readString = "";
        boolean isErrorString = false; //in case we find wrong unicode, such as /u123r
        while (moreChars() && !isQuotationMark() ){
            if (foundEscape()){//if we find a \, we check the next char and add that one to the string
                bumpOffset(1);//we consume the escape
                if (!moreChars()){
                    break;
                }else{
                    switch (_chars[_offset]){
                        case 98:
                            readString = readString + "\b";
                            bumpOffset(1);
                            break;
                        case 102:
                            readString = readString + "\f";
                            bumpOffset(1);
                            break;
                        case 110:
                            readString = readString + "\n";
                            bumpOffset(1);
                            break;
                        case 114:
                            readString = readString + "\r";
                            bumpOffset(1);
                            break;
                        case 117:
                            bumpOffset(1);
                            String possibleUnicode = foundUnicode();
                            if (possibleUnicode != null ){//if it was correct, we get the whole unicode
                                readString = readString + possibleUnicode;
                            }else {//if it was not correct, we just consume / and u and iterate again to get next char
                                //readString = readString + "\\" + "u";
                                isErrorString = true;
                            }
                            break;
                        default:
                            readString = readString + _chars[_offset];
                            bumpOffset(1);
                    }
                }
            }else{
                //we will stop iterating just before reaching a " or just when input ends
                readString = readString + _chars[_offset];
                bumpOffset(1);
            }
        }
        if (isErrorString){
            //if we found an unicode that is not correct, so we add an error token
            str[0] = null;
        }
        str[1] = readString;
        return str;
    }

    public String foundUnicode(){
        //returns the escaped unicode if it is correct, if not, returns an error
        //_offset just after the u
        String str = "";
        int ii = 0;
        int hex = 0;
        char aux;
        while (moreChars() && ii<4){
            aux = _chars[_offset + ii];
            hex = hex * 16 + aux - '0';
            if (aux >= 'A'){
                hex = hex - 7;
            }
            str = str + _chars[_offset + ii];
            ii++;
        }
        bumpOffset(ii);
        if (ii == 4 && isUnicodeCorrect(str)){
            return "" + (char)hex;
        }else {
            return null;
        }
    }

    public boolean foundEscape(){//checks if we have a Escape, which means, the input being like "  \"\something\"     "
        if ( _chars[_offset] == ((int)'\\') ){
            return true;
        }else {
            return false;
        }
    }


    public boolean isEscapedUnicode(String str){//checks if we have a Escape Unicode, which means, the input being like  \ u
        if ( str.length() > 1 && str.charAt(0) == ((int)'\\') && str.charAt(1) == ((int)'u')  ){
            return true;
        }else {
            return false;
        }
    }

    public boolean isUnicodeCorrect(String str){
        //4 characters, have to be either numbers or leters A..F or a..f
        if (str.length() == 4){
            str = str.toLowerCase();
            char aux;
            for (int ii = 0; ii<4; ii++){
                aux = str.charAt(ii);
                if ( ! ( (aux >= '0' && aux <= '9') || (aux >= 'a' && aux <= 'f') ) ){
                    //enters only if not number nor letter a..f
                    return false;
                }
            }
            return true;
        }else {
            return false;
        }
    }
/*
    public boolean isUnicodeCorrect(String str){
        //Precondition: validated head of the string == \ u
        //4 characters after \ u, those are either numbers or leters A..F or a..f
        if (str.length() == 6){
            str = str.toLowerCase();
            char aux;
            for (int ii = 2; ii<6; ii++){
                aux = str.charAt(ii);
                if ( ! ( (aux >= '0' && aux <= '9') || (aux >= 'a' && aux <= 'f') ) ){
                    //enters only if not number nor letter a..f
                    return false;
                }
            }
            return true;
        }else {
            return false;
        }
    }
*/

}















