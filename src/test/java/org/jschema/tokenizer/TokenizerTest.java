package org.jschema.tokenizer;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import static org.jschema.tokenizer.Token.TokenType.CONSTANT;
import static org.jschema.tokenizer.Token.TokenType.PUNCTUATION;
import static org.jschema.tokenizer.Token.TokenType.NUMBER;
import static org.jschema.tokenizer.Token.TokenType.STRING;
import static org.jschema.tokenizer.Token.TokenType.ERROR;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class TokenizerTest
{

  @Test
  public void bootstrapTest()
  {
    //basic constant
    List<Token> tokens = tokenize( "true" );
    assertTokensAre( tokens, token( CONSTANT, "true" ) );

    // leading whitespace
    tokens = tokenize( "   true" );
    assertTokensAre( tokens, token( CONSTANT, "true" ) );

    // trailing whitespace
    tokens = tokenize( "true   " );
    assertTokensAre( tokens, token( CONSTANT, "true" ) );

    // trailing whitespace
    tokens = tokenize( "true   " );
    assertTokensAre( tokens, token( CONSTANT, "true" ) );

    // two tokens whitespace
    tokens = tokenize( "true   false" );
    assertTokensAre( tokens, token( CONSTANT, "true" ), token( CONSTANT, "false" ) );
  }

  @Test
  public void testPunctuation() {
    List<Token> tokens;

      // invalid constant (constant with typo)
      tokens = tokenize( "truel false" );
      assertTokensAre( tokens,token(ERROR,">> BAD TOKEN : truel"),token(CONSTANT,"false"));

    //braces
    tokens = tokenize( "[]" );
    assertTokensAre( tokens, token( PUNCTUATION, "[" ), token( PUNCTUATION, "]" ) );

    //single curly brace
    tokens = tokenize( "{" );
    assertTokensAre(tokens,token(PUNCTUATION,"{"));

    //curly braces with white space
    tokens = tokenize( "{   }" );
    assertTokensAre( tokens, token( PUNCTUATION, "{" ), token( PUNCTUATION, "}" ) );

    //comma
    tokens = tokenize( "," );
    assertTokensAre( tokens, token( PUNCTUATION, "," ));

    //colon
    tokens = tokenize( ":" );
    assertTokensAre( tokens, token( PUNCTUATION, ":" ));


    //tests for PUNCTUATION
    tokens = tokenize("[");
    assertTokensAre( tokens, token(PUNCTUATION, "["));

    tokens = tokenize("]");
    assertTokensAre( tokens, token(PUNCTUATION, "]"));

    tokens = tokenize("{");
    assertTokensAre( tokens, token(PUNCTUATION, "{"));

    tokens = tokenize("}");
    assertTokensAre( tokens, token(PUNCTUATION, "}"));

    tokens = tokenize(":");
    assertTokensAre( tokens, token(PUNCTUATION, ":"));

    tokens = tokenize(",");
    assertTokensAre( tokens, token(PUNCTUATION, ","));
  }

  @Test
  public void testStrings() {
    List<Token> tokens;

    //Test basic string
    tokens = tokenize( "\"test\"" );
    assertTokensAre( tokens, token(STRING, "test"));

    //string not ending in quote
    tokens = tokenize( "\"test" );
    assertTokensAre( tokens, token(ERROR,">> BAD TOKEN : \"test"));

    //string not beginning in quote
    tokens = tokenize( "test\"" );
    assertTokensAre( tokens, token(ERROR,">> BAD TOKEN : t"),token(ERROR,">> BAD TOKEN : e"),token(ERROR,">> BAD TOKEN : s"),token(ERROR,">> BAD TOKEN : t"),token(ERROR,">> BAD TOKEN : \""));

    //escaped quote in string
    tokens = tokenize( '"' + backSlash( '"' ) + '"' );
    assertTokensAre( tokens, token(STRING, "\""));

    //escaped backslash in string
    tokens = tokenize( '"' + backSlash( '\\' ) + '"' );
    assertTokensAre( tokens, token(STRING, "\\"));

    //escaped frontslash in string
    tokens = tokenize( '"' + backSlash( '/' ) + '"' );
    assertTokensAre( tokens, token(STRING, "/"));

    //escaped backspace in string
    tokens = tokenize( '"' + backSlash( 'b' ) + '"' );
    assertTokensAre( tokens, token(STRING, "\b"));

    //escaped form feed in string
    tokens = tokenize( '"' + backSlash( 'f' ) + '"' );
    assertTokensAre( tokens, token(STRING, "\f"));

    //escaped new line in string
    tokens = tokenize( '"' + backSlash( 'n' ) + '"' );
    assertTokensAre( tokens, token(STRING, "\n"));

    //escaped carriage return in string
    tokens = tokenize( '"' + backSlash( 'r' ) + '"' );
    assertTokensAre( tokens, token(STRING, "\r"));

    //Escaped unicode
    tokens = tokenize( '"' + backSlash( "u263A" ) + '"' );
    assertTokensAre( tokens, token(STRING, "\u263A"));
    assertTokensAre( tokens, token(STRING, "☺"));

    tokens = tokenize( '"' + backSlash( "u263G" ) + '"' );
    assertTokensAre( tokens, token(ERROR, ">> BAD TOKEN : "), token(ERROR, ">> BAD TOKEN : "));

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
    assertTokensAre( tokens, token( NUMBER, "1234" ));

    //multiple numbers
    tokens = tokenize( "1 2 3" );
    assertTokensAre( tokens, token(NUMBER,"1"),token( NUMBER, "2" ), token(NUMBER,"3"));

    //decimal number
    tokens = tokenize( "1.23" );
    assertTokensAre( tokens, token(NUMBER,"1.23"));

    //invalid decimal number
    tokens = tokenize( "1..23" );
    assertTokensAre( tokens, token(ERROR,">> BAD TOKEN : 1..23"));

    //decimal number
    tokens = tokenize( ".23" );
    assertTokensAre(tokens, token(ERROR, ">> BAD TOKEN : ."), token(NUMBER, "23"));

    //multiple fractions
    tokens = tokenize( "0.23 1.56" );
    assertTokensAre(tokens, token(NUMBER, "0.23"), token(NUMBER, "1.56"));

    tokens = tokenize( "01.3" );
    assertTokensAre(tokens, token(NUMBER, "0"), token(NUMBER, "1.3"));

    //exponents lowercase e
    tokens = tokenize( "2e1" );
    assertTokensAre( tokens, token(NUMBER,"2e1"));

    //exponents uppercase e
    tokens = tokenize( "3E4" );
    assertTokensAre( tokens, token(NUMBER,"3E4"));

    //negative number
    tokens = tokenize( "-2" );
    assertTokensAre( tokens, token(NUMBER,"-2"));

    //negative decimal
    tokens = tokenize( "-4.2" );
    assertTokensAre( tokens, token(NUMBER,"-4.2"));

    //negative exp
    tokens = tokenize( "-3E+4" );
    assertTokensAre(tokens, token(NUMBER, "-3E+4"));

    //negative exp
    tokens = tokenize( "2E-4" );
    assertTokensAre(tokens, token(NUMBER, "2E-4"));

    tokens = tokenize( "3E-4" );
    assertTokensAre( tokens, token(NUMBER,"3E-4"));

    tokens = tokenize( "-0.1E4" );
    assertTokensAre(tokens, token(NUMBER, "-0.1E4"));

    //double negative exp
    tokens = tokenize( "-3E-4" );
    assertTokensAre( tokens, token(NUMBER,"-3E-4"));

    //double negative exp two
    tokens = tokenize( "-3E-4 -5e1" );
    assertTokensAre( tokens, token(NUMBER,"-3E-4"),token(NUMBER,"-5e1"));

    //exp and neg num
    tokens = tokenize( "3E4," );
    assertTokensAre( tokens, token(NUMBER,"3E4"),token(PUNCTUATION,","));

    //invalid input exp and decimal
    tokens = tokenize( "3E-4.0" );
    assertTokensAre( tokens, token(NUMBER, "3E-4"), token(ERROR, ">> BAD TOKEN : ."), token(NUMBER, "0"));

    //invalid input exp
    tokens = tokenize( "3E-4a" );
    assertTokensAre( tokens, token(NUMBER, "3E-4"), token(ERROR, ">> BAD TOKEN : a"));

    //invalid input decimal
    tokens = tokenize( "3.4a" );
    assertTokensAre( tokens, token(NUMBER, "3.4"), token(ERROR, ">> BAD TOKEN : a"));

  }

  @Test
  public void testLiterals() {
    List<Token> tokens;

    tokens = tokenize( "true" );
    assertTokensAre( tokens, token( CONSTANT, "true" ) );

    tokens = tokenize( "false" );
    assertTokensAre( tokens, token( CONSTANT, "false" ) );

    tokens = tokenize( "null" );
    assertTokensAre( tokens, token( CONSTANT, "null" ) );

  }

  @Test
  public void testCombos() {
    List<Token> tokens;

    //assortment
    tokens=tokenize("\"type\": \"array\":");
    assertTokensAre(tokens,token(STRING,"type"), token(PUNCTUATION,":"), token(STRING,"array"), token(PUNCTUATION,":"));


    tokens = tokenize("123, true");
    assertTokensAre( tokens, token(NUMBER, "123"), token(PUNCTUATION, ","), token(CONSTANT, "true"));

  }

  @Test
  public void testErrors() {
    List<Token> tokens;

    // unclosed string
    tokens = tokenize( "\"foo" );
    assertTokensAre( tokens,token(ERROR,">> BAD TOKEN : \"foo"));

    // invalid constant (constant with typo)
    tokens = tokenize( "truel" );
    assertTokensAre( tokens,token(ERROR,">> BAD TOKEN : truel"));

    // invalid constant (constant with typo)
    tokens = tokenize( "truel false" );
    assertTokensAre( tokens,token(ERROR,">> BAD TOKEN : truel"),token(CONSTANT,"false"));

  }


  //========================================================================================
  // Test Helpers
  //========================================================================================

  private void assertTokensAre( List<Token> tokens, Token... matches )
  {
    if( tokens.size() != matches.length )
    {
      Assert.fail( "Did not find " + matches.length + " tokens: " + tokens);
    }
    for( int i = 0; i < matches.length; i++ )
    {
      assertTokenMatches( matches[i], tokens.get( i ) );
    }
  }

  private void assertTokenMatches( Token match, Token token )
  {
    assertEquals(match.getTokenType(), token.getTokenType());
    if( match.getTokenValue() != null )
    {
      assertEquals(match.getTokenValue(), token.getTokenValue());
    }
    if( match.getLineNumber() > 0 )
    {
      assertEquals(match.getLineNumber(), token.getLineNumber());
    }
    if( match.getOffset() > 0 )
    {
      assertEquals(match.getOffset(), token.getOffset());
    }
    if( match.getColumn() > 0 )
    {
      assertEquals(match.getColumn(), token.getColumn());
    }
  }

  private Token token(Token.TokenType type)
  {
    return token( type, null );
  }

  private Token token(Token.TokenType type, String value)
  {
    return token( type, value, -1);
  }

  private Token token(Token.TokenType type, String value, int offset)
  {
    return token( type, value, offset, -1);
  }

  private Token token(Token.TokenType type, String value, int offset, int line)
  {
    return token( type, value, offset, line, -1);
  }

  private Token token(Token.TokenType type, String value, int offset, int line, int col)
  {
    return new Token( type, value, line, col, offset );
  }

  private List<Token> tokenize( String str )
  {
    return new Tokenizer( str ).tokenize();
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
