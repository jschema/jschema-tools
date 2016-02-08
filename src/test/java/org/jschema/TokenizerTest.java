package org.jschema;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class TokenizerTest
{
  @Test
  public void bootstrapTest()
  {
    //basic constant
    List<Token> tokens = tokenize( "true" );
    assertTokensAre( tokens, token( TokenType.TRUE, "true" ) );

    // leading whitespace
    tokens = tokenize( "   true" );
    assertTokensAre( tokens, token( TokenType.TRUE, "true" ) );

    // trailing whitespace
    tokens = tokenize( "true   " );
    assertTokensAre(tokens, token(TokenType.TRUE, "true"));

    // trailing whitespace
    tokens = tokenize( "true   " );
    assertTokensAre( tokens, token(TokenType.TRUE, "true"));

    // two tokens whitespace
    tokens = tokenize( "true   false" );
    assertTokensAre( tokens, token( TokenType.TRUE, "true" ), token( TokenType.FALSE, "false" ) );
  }

  @Test
  public void testPunctuation() {
    List<Token> tokens;

    //braces
    tokens = tokenize( "[]" );
    assertTokensAre( tokens, token( TokenType.LSQUARE, "[" ), token( TokenType.RSQUARE, "]" ) );

    //single curly brace
    tokens = tokenize( "{" );
    assertTokensAre(tokens,token(TokenType.LCURLY,"{"));

    //curly braces with white space
    tokens = tokenize( "{   }" );
    assertTokensAre( tokens, token( TokenType.LCURLY, "{" ), token( TokenType.RCURLY, "}" ) );

    //comma
    tokens = tokenize( "," );
    assertTokensAre( tokens, token( TokenType.COMMA, "," ));

    //colon
    tokens = tokenize( ":" );
    assertTokensAre( tokens, token( TokenType.COLON, ":" ));


    //tests for PUNCTUATION
    tokens = tokenize("[");
    assertTokensAre( tokens, token(TokenType.LSQUARE, "["));

    tokens = tokenize("]");
    assertTokensAre( tokens, token(TokenType.RSQUARE, "]"));

    tokens = tokenize("{");
    assertTokensAre( tokens, token(TokenType.LCURLY, "{"));

    tokens = tokenize("}");
    assertTokensAre( tokens, token(TokenType.RCURLY, "}"));

    tokens = tokenize(":");
    assertTokensAre( tokens, token(TokenType.COLON, ":"));

    tokens = tokenize(",");
    assertTokensAre( tokens, token(TokenType.COMMA, ","));
  }

  @Test
  public void testStrings() {
    List<Token> tokens;

    //Test basic string
    tokens = tokenize( "\"test\"" );
    assertTokensAre( tokens, token(TokenType.STRING, "test"));

    //string not ending in quote
    tokens = tokenize( "\"test" );
    assertTokensAre( tokens, token(TokenType.ERROR,"test"));

    //string not beginning in quote
    tokens = tokenize( "test\"" );
    assertTokensAre( tokens, token(TokenType.ERROR,"test"),token(TokenType.ERROR,""));

    //escaped quote in string
    tokens = tokenize( '"' + backSlash( '"' ) + '"' );
    assertTokensAre( tokens, token(TokenType.STRING, "\""));

    //escaped backslash in string
    tokens = tokenize( '"' + backSlash( '\\' ) + '"' );
    assertTokensAre( tokens, token(TokenType.STRING, "\\"));

    //escaped frontslash in string
    tokens = tokenize( '"' + backSlash( '/' ) + '"' );
    assertTokensAre( tokens, token(TokenType.STRING, "/"));

    //escaped backspace in string
    tokens = tokenize( '"' + backSlash( 'b' ) + '"' );
    assertTokensAre( tokens, token(TokenType.STRING, "\b"));

    //escaped form feed in string
    tokens = tokenize( '"' + backSlash( 'f' ) + '"' );
    assertTokensAre( tokens, token(TokenType.STRING, "\f"));

    //escaped new line in string
    tokens = tokenize( '"' + backSlash( 'n' ) + '"' );
    assertTokensAre( tokens, token(TokenType.STRING, "\n"));

    //escaped carriage return in string
    tokens = tokenize( '"' + backSlash( 'r' ) + '"' );
    assertTokensAre( tokens, token(TokenType.STRING, "\r"));

    //Escaped unicode
    tokens = tokenize( '"' + backSlash( "u263A" ) + '"' );
    assertTokensAre( tokens, token(TokenType.STRING, "\u263A"));
    assertTokensAre( tokens, token(TokenType.STRING, "☺"));

    tokens = tokenize( '"' + backSlash( "u263G" ) + '"' );
    assertTokensAre( tokens, token(TokenType.ERROR, ""), token(TokenType.ERROR, ""));

  }

  @Test
  public void unicodeSanityCheck(){
    assertEquals("\u263A", "☺");
  }

  @Test
  public void testNumbers() {
    List<Token> tokens;

    //number by itself
    tokens = tokenize( "1234" );
    assertTokensAreNumbers(tokens, numberToken(TokenType.NUMBER, "1234", 1234.0));

    //multiple numbers
    tokens = tokenize( "1 2 3" );
    assertTokensAreNumbers(tokens, numberToken(TokenType.NUMBER, "1", 1.0), numberToken(TokenType.NUMBER, "2", 2.0), numberToken(TokenType.NUMBER, "3", 3.0));

    //decimal number
    tokens = tokenize( "1.23" );
    assertTokensAreNumbers(tokens, numberToken(TokenType.NUMBER, "1.23", 1.23));

    //invalid decimal number
    tokens = tokenize( "1..23" );
    assertTokensAre(tokens, token(TokenType.ERROR, "1."), token(TokenType.ERROR, "."), token(TokenType.NUMBER, "23"));

    //decimal number
    tokens = tokenize( ".23" );
    assertTokensAre(tokens, token(TokenType.ERROR, "."), token(TokenType.NUMBER, "23"));

    //multiple fractions
    tokens = tokenize( "0.23 1.56" );
    assertTokensAre(tokens, token(TokenType.NUMBER, "0.23"), token(TokenType.NUMBER, "1.56"));

    tokens = tokenize( "01.3" );
    assertTokensAreNumbers(tokens, numberToken(TokenType.NUMBER, "0", 0.0), numberToken(TokenType.NUMBER, "1.3", 1.3));

    //exponents lowercase e
    tokens = tokenize( "2e1" );
    assertTokensAreNumbers(tokens, numberToken(TokenType.NUMBER, "2e1", 20.0));

    //exponents uppercase e
    tokens = tokenize( "3E4" );
    assertTokensAreNumbers(tokens, numberToken(TokenType.NUMBER, "3E4", 30000.0));

    //negative number
    tokens = tokenize( "-2" );
    assertTokensAreNumbers(tokens, numberToken(TokenType.NUMBER, "-2", -2.0));

    //negative decimal
    tokens = tokenize( "-4.2" );
    assertTokensAreNumbers(tokens, numberToken(TokenType.NUMBER, "-4.2", -4.2));

    //negative exp
    tokens = tokenize( "-3E+4" );
    assertTokensAreNumbers(tokens, numberToken(TokenType.NUMBER, "-3E+4", -30000.0));

    //negative exp
    tokens = tokenize( "2E-4" );
    assertTokensAreNumbers(tokens, numberToken(TokenType.NUMBER, "2E-4", 0.0002));

    tokens = tokenize( "3E-4" );
    assertTokensAreNumbers(tokens, numberToken(TokenType.NUMBER, "3E-4", 3.0 * Math.pow(10, -4)));

    tokens = tokenize( "-0.1E4" );
    assertTokensAreNumbers(tokens, numberToken(TokenType.NUMBER, "-0.1E4", -1000.0));

    //double negative exp
    tokens = tokenize( "-2E-4" );
    assertTokensAreNumbers(tokens, numberToken(TokenType.NUMBER, "-2E-4", -0.000200));

    //double negative exp two
    tokens = tokenize( "-2E-4 -5e1" );
    assertTokensAreNumbers(tokens, numberToken(TokenType.NUMBER, "-2E-4", -0.000200), numberToken(TokenType.NUMBER, "-5e1", -50.0));

    //exp and neg num
    tokens = tokenize( "3E4," );
    assertTokensAre( tokens, token(TokenType.NUMBER,"3E4"),token(TokenType.COMMA,","));

    //invalid input exp and decimal
    tokens = tokenize( "3E-4.0" );
    assertTokensAre( tokens, token(TokenType.NUMBER, "3E-4"), token(TokenType.ERROR, "."), token(TokenType.NUMBER, "0"));

    //invalid input exp
    tokens = tokenize( "3E-4a" );
    assertTokensAre( tokens, token(TokenType.NUMBER, "3E-4"), token(TokenType.ERROR, "a"));

    //invalid input decimal
    tokens = tokenize( "3.4a" );
    assertTokensAre( tokens, token(TokenType.NUMBER, "3.4"), token(TokenType.ERROR, "a"));

    tokens = tokenize( "1.4ea" );
    assertTokensAre( tokens, token(TokenType.ERROR, "1.4e"), token(TokenType.ERROR, "a"));

  }

  private Token numberToken(TokenType type, String value, double num) {
    return new Token( type, value, 0, 0, num );
  }

  @Test
  public void testLiterals() {
    List<Token> tokens;

    tokens = tokenize( "true" );
    assertTokensAre( tokens, token( TokenType.TRUE, "true" ) );

    tokens = tokenize( "false" );
    assertTokensAre( tokens, token( TokenType.FALSE, "false" ) );

    tokens = tokenize( "null" );
    assertTokensAre( tokens, token( TokenType.NULL, "null" ) );

  }

  @Test
  public void testCombos() {
    List<Token> tokens;

    //assortment
    tokens=tokenize("\"type\": \"array\":");
    assertTokensAre(tokens,token(TokenType.STRING,"type"), token(TokenType.COLON,":"), token(TokenType.STRING,"array"), token(TokenType.COLON,":"));


    tokens = tokenize("123, true");
    assertTokensAre( tokens, token(TokenType.NUMBER, "123"), token(TokenType.COMMA, ","), token(TokenType.TRUE, "true"));

  }

  @Test
  public void testErrors() {
    List<Token> tokens;

    // unclosed string
    tokens = tokenize( "\"foo" );
    assertTokensAre( tokens,token(TokenType.ERROR,"foo"));

    // invalid constant (constant with typo)
    tokens = tokenize( "truel" );
    assertTokensAre( tokens,token(TokenType.ERROR,"truel"));

    // invalid constant (constant with typo)
    tokens = tokenize( "truel false" );
    assertTokensAre( tokens,token(TokenType.ERROR,"truel"),token(TokenType.FALSE,"false"));

  }


  //========================================================================================
  // Test Helpers
  //========================================================================================

  private void assertTokensAre( List<Token> tokens, Token... matches )
  {
    if( tokens.size() != matches.length )
    {
      fail("Did not find " + matches.length + " tokens: " + tokens);
    }
    for( int i = 0; i < matches.length; i++ )
    {
      assertTokenMatches(matches[i], tokens.get(i));
    }
  }
  private void assertTokensAreNumbers( List<Token> tokens, Token... matches )
  {
    if( tokens.size() != matches.length )
    {
      fail("Did not find " + matches.length + " tokens: " + tokens);
    }
    for(int i = 0; i < matches.length; i++) {
      assertEquals(matches[i].getType(), tokens.get(i).getType());
      assertEquals(matches[i].getString(), tokens.get(i).getString());
      assertEquals(matches[i].getNumber(), tokens.get(i).getNumber(), Double.MIN_VALUE);
    }
  }

  private void assertTokenMatches( Token match, Token token) {
    assertEquals(match.getType(), token.getType());
    if( match.getString() != null ) {
      assertEquals(match.getString(), token.getString());
    }
    if( match.getLineNumber() > 0) {
      assertEquals(match.getLineNumber(), token.getLineNumber());
    }
    if( match.getColumn() > 0 )
    {
      assertEquals(match.getColumn(), token.getColumn());
    }
  }

  private Token token(TokenType type, String value)
  {
    return token( type, value, -1);
  }

  private Token token(TokenType type, String value, int line)
  {
    return token( type, value, line, -1);
  }

  private Token token(TokenType type, String value, int line, int col)
  {
    return new Token( type, value, line, col, 0 );
  }

  private List<Token> tokenize( String str )
  {
    Tokenizer tokenizer = new Tokenizer(str);
    ArrayList<Token> list = new ArrayList<Token>();
    Token token = tokenizer.next();
    while(token.getType() != TokenType.EOF) {
      list.add(token);
      token = tokenizer.next();
    }
    return list;
  }

  private String backSlash( char s )
  {
    return "\\" + s;
  }

  private String backSlash( String s )
  {
    return "\\" + s;
  }

}
