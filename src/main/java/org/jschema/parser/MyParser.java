
package org.jschema.parser;

import org.jschema.parser.Token.TokenType;

import java.util.ArrayList;
import java.util.HashMap;

import static org.jschema.parser.Token.TokenType.*;

public class MyParser {

    private final Tokenizer _tokenizer;
    private Token _currentToken;

    public MyParser(String src ){
        _tokenizer = new Tokenizer( src );
        nextToken();
    }

    //=================================================================================
    //  JSON Grammar
    //=================================================================================

    public Object parse() {
        Object value = parseValue();
        if( match( EOF ) ) {
          return value;
        }
        else {
          return error();
        }
    }

    public Object parseValue(){
        //parse objects
        if( match( LCURLY ) ){
            nextToken();
            return parseObject();
        }

        // parse arrays
        if( match( LSQUARE ) ){
            nextToken();
            //each time we find a new array, new array created
            ArrayList<Object> thisArray = new ArrayList<Object>();
            return parseArray(thisArray);
        }

        // parse literals (e.g. true, false, strings, numbers)
        if( match( STRING ) ){
            String tokenValue = _currentToken.getTokenValue();
            nextToken();
            return tokenValue;
        }

        if( match( NUMBER ) ){
            //check if it is integer or double by looking for a dot in the string that contains the number
            //String tokenValue = _currentToken.getTokenValue();
            int isThereADot = _currentToken.getTokenValue().indexOf('.');
            int thereIsAnExp_e = _currentToken.getTokenValue().indexOf('e');
            int thereIsAnExp_E = _currentToken.getTokenValue().indexOf('E');
            double doubleNumber = _currentToken.getTokenNumberValue();
            nextToken();
            if (isThereADot != -1 || thereIsAnExp_e != -1 || thereIsAnExp_E != -1){
                return doubleNumber;
            }else{
                //then it is an integer
                return (int)doubleNumber;
            }
        }

        if( match( NULL ) ){
            nextToken();
            return null;
        }

        if( match( TRUE ) ){
            nextToken();
            return true;
        }

        if( match( FALSE ) ){
            nextToken();
            return false;
        }

        if( match( ERROR ) ){
            nextToken();
            return error();
        }

        //TODO implement other literals
            return error();
    }

    public Object parseObject()
    {
        //TODO implement, return a map of name/value pairs, and Error if an error is detected
        //                pass the map into parseMember to populate
        HashMap<String, Object> map = new HashMap<String, Object>();
        String key;
        Object value;

        while ( match(STRING) ){
            //we will consume one entire pair in each iteration (string:value)
            key = (String)parseValue();//it will be string,
            if (match(COLON)){
                nextToken();//consume colon
                if (!match(EOF) && !match(ERROR) && isCorrectValue() ){//if i have a value, then i just add it
                    value = parseValue();
                }else{
                    return error(-1);
                }
            }else{
                return error(2);//no : after a the string
            }

            //ad to the map
            map.put(key, value);

            if ( match(COMMA) ){
                //consume coma and iterate to keep adding things
                nextToken();
                if (!match(STRING)){
                    //if after the comma there is not another pair, there is an error
                    return error(3);
                }
            }
        }

        switch (_currentToken.getTokenType()){
            case RCURLY:
                nextToken();
                return map;
            case EOF:
                return error(1);
            default:
                return error(-1);
        }


    }

//    private Object parseMember( HashMap map ) {
//        //TODO implement, parse the key and value, return the map if it is good, Error otherwise.
//        return map;
//    }

    public Object parseArray(ArrayList<Object> thisArray){
        //if we find ',' we parse next token, if we find ']' we have finished the array

        while (!match(RSQUARE) && tokenIsCorrectArray() ){
            //either we have a correct value that we have to add to the arrayList or we have a new array/object
            thisArray.add(parseValue());
            if ( match(COMMA) ){
                //consume coma and iterate to keep adding things
                nextToken();
                if (!isCorrectValue()){
                    return error(4);
                }
            }
        }

        switch (_currentToken.getTokenType()){
            case RSQUARE:
                nextToken();
                return thisArray;
            case EOF:
                return error(0);
            default:
                return error(-1);
        }
    }

    //=================================================================================
    //  My Helpers
    //=================================================================================

    public boolean isCorrectValue(){
        //checks if in an array, next token is what it is supposed to be
        switch  (_currentToken.getTokenType()){
            case COMMA:
            case RCURLY:
            case COLON:
            case ERROR:
            case EOF:
            case RSQUARE:
                return false;
            default://string - object - array - number - true - false - null
                return true;
        }
    }


    public boolean tokenIsCorrectArray(){
        //checks if in an array, next token is what it is supposed to be
        switch  (_currentToken.getTokenType()){
            case COMMA:
            case RCURLY:
            case COLON:
            case ERROR:
            case EOF:
                return false;
            default://string - object - array - number - true - false - null
                return true;
        }
    }

    //=================================================================================
    //  Tokenizer helpers
    //=================================================================================
    private void nextToken()
    {
        _currentToken = _tokenizer.next();
    }

    private boolean match( TokenType type )
    {
        return _currentToken.getTokenType() == type;
    }

    private Error error()
    {
        return new Error("Unexpected Token: " + _currentToken.toString());
    }


    //=================================================================================
    //  ERROR Types
    //=================================================================================

    private Error error(int errorType){
        switch (errorType){
            //TODO case -1, either it is like error() or it has to be divided in several types
            case -1:
                return new Error("Error founded.....");
            case 0:
                return new Error("Array not closed, missing: ]");
            case 1:
                return new Error("Object not closed, missing: }");
            case 2:
                return new Error("Incorrect pair, missing colon");
            case 3:
                return new Error("Incorrect pair, missing string after comma");
            case 4:
                return new Error("Incorrect array, missing value after [");
            default:
                return new Error("______Wrong error code in code");
        }
    }
}
