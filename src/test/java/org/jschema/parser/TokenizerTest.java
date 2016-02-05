package org.jschema.parser;

import org.junit.Ignore;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.jschema.parser.Token.TokenType.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class TokenizerTest
{

  @Test
  @Ignore("Download ")
  public void readBigFile() {
    String bigJson = null;
    try {
      String path = getClass().getClassLoader().getResource("citylots.json").getPath();
      bigJson = new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);
    } catch(Exception e) {
      fail( "Please copy the file 'citylots.json' in the src/main/resources folder" );
    }
    long times = 1;
    long before = System.currentTimeMillis();
    int errors = 0;
    for(int i = 0; i < times; i++) {
      Tokenizer tokenizer = new Tokenizer(bigJson);
      Token token = tokenizer.next();
      while(token.getTokenType() != Token.TokenType.EOF) {
        token = tokenizer.next();
      }
      errors = tokenizer.getErrorCount();
    }
    long after = System.currentTimeMillis();
    System.out.println("Time: " + (after-before) / (double) times + " ms");
    assertEquals(0, errors);
  }


  @Test
  public void bootstrapTest()
  {
    //basic constant
    List<Token> tokens = tokenize( "true" );
    assertTokensAre( tokens, token( TRUE, "true" ) );

    // leading whitespace
    tokens = tokenize( "   true" );
    assertTokensAre( tokens, token( TRUE, "true" ) );

    // trailing whitespace
    tokens = tokenize( "true   " );
    assertTokensAre(tokens, token(TRUE, "true"));

    // trailing whitespace
    tokens = tokenize( "true   " );
    assertTokensAre( tokens, token(TRUE, "true"));

    // two tokens whitespace
    tokens = tokenize( "true   false" );
    assertTokensAre( tokens, token( TRUE, "true" ), token( FALSE, "false" ) );
  }

  @Test
  public void testPunctuation() {
    List<Token> tokens;

      // invalid constant (constant with typo)
      tokens = tokenize( "truel false" );
      assertTokensAre( tokens,token(ERROR,">> BAD TOKEN : truel"),token(CONSTANT,"false"));

    //braces
    tokens = tokenize( "[]" );
    assertTokensAre( tokens, token( LSQUARE, "[" ), token( RSQUARE, "]" ) );

    //single curly brace
    tokens = tokenize( "{" );
    assertTokensAre(tokens,token(LCURLY,"{"));

    //curly braces with white space
    tokens = tokenize( "{   }" );
    assertTokensAre( tokens, token( LCURLY, "{" ), token( RCURLY, "}" ) );

    //comma
    tokens = tokenize( "," );
    assertTokensAre( tokens, token( COMMA, "," ));

    //colon
    tokens = tokenize( ":" );
    assertTokensAre( tokens, token( COLON, ":" ));


    //tests for PUNCTUATION
    tokens = tokenize("[");
    assertTokensAre( tokens, token(LSQUARE, "["));

    tokens = tokenize("]");
    assertTokensAre( tokens, token(RSQUARE, "]"));

    tokens = tokenize("{");
    assertTokensAre( tokens, token(LCURLY, "{"));

    tokens = tokenize("}");
    assertTokensAre( tokens, token(RCURLY, "}"));

    tokens = tokenize(":");
    assertTokensAre( tokens, token(COLON, ":"));

    tokens = tokenize(",");
    assertTokensAre( tokens, token(COMMA, ","));
  }

  @Test
  public void testStrings() {
    List<Token> tokens;

    //Test basic string
    tokens = tokenize( "\"test\"" );
    assertTokensAre( tokens, token(STRING, "test"));

    //string not ending in quote
    tokens = tokenize( "\"test" );
    assertTokensAre( tokens, token(ERROR,"test"));

    //string not beginning in quote
    tokens = tokenize( "test\"" );
    //assertTokensAre( tokens, token(ERROR,">> BAD TOKEN : t"),token(ERROR,">> BAD TOKEN : e"),token(ERROR,">> BAD TOKEN : s"),token(ERROR,">> BAD TOKEN : t"),token(ERROR,">> BAD TOKEN : \""));
      assertTokensAre( tokens, token(ERROR,">> BAD TOKEN : test\""));

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

      //Escaped unicode with text
      tokens = tokenize( '"' + backSlash( "u263Ahello" ) + '"' );
      assertTokensAre( tokens, token(STRING, "\u263Ahello"));
      assertTokensAre( tokens, token(STRING, "☺hello"));

      //text with spaces
      tokens = tokenize( "\"how are you?\"");
      assertTokensAre( tokens, token(STRING, "how are you?"));

      //text with spaces and nested quotes
      tokens = tokenize( "\"how " +backSlash('"')+"are"+ backSlash('"')+" you?\"");
      assertTokensAre( tokens, token(STRING, "how \"are\" you?"));

      //text with spaces and unmatched nested quotes
      tokens = tokenize( "\"how " +backSlash('"')+"are"+" you?\"");
      assertTokensAre( tokens, token(STRING, "how \"are you?"));

      //escape unicode with spaces
      tokens = tokenize( '"'+backSlash("u263A") +" "+backSlash("u263A") +'"');
      assertTokensAre( tokens, token(STRING, "\u263A \u263A"));
      assertTokensAre( tokens, token(STRING, "☺ ☺"));

    tokens = tokenize( '"' + backSlash( "u263G" ) + '"' );
    assertTokensAre( tokens, token(ERROR, ""), token(ERROR, ""));

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
    assertTokensAreNumbers(tokens, numberToken(NUMBER, "1234", 1234.0));

    //multiple numbers
    tokens = tokenize( "1 2 3" );
    assertTokensAreNumbers(tokens, numberToken(NUMBER, "1", 1.0), numberToken(NUMBER, "2", 2.0), numberToken(NUMBER, "3", 3.0));

    //decimal number
    tokens = tokenize( "1.23" );
    assertTokensAreNumbers(tokens, numberToken(NUMBER, "1.23", 1.23));

    //invalid decimal number
    tokens = tokenize( "1..23" );
    assertTokensAre(tokens, token(ERROR, "1."), token(ERROR, "."), token(NUMBER, "23"));

    //invalid decimal number
    tokens = tokenize( ".23" );
      assertTokensAre(tokens, token(ERROR, "."), token(NUMBER, "23"));

      //bad number-> important edge case ********************************
     // tokens = tokenize( "a23" );
      //assertTokensAre(tokens, token(ERROR, ">> BAD TOKEN : a23"));


    //multiple fractions
    tokens = tokenize( "0.23 1.56" );
    assertTokensAre(tokens, token(NUMBER, "0.23"), token(NUMBER, "1.56"));

      //invalid decimal
    tokens = tokenize( "01.3" );
    assertTokensAreNumbers(tokens, numberToken(NUMBER, "0", 0.0), numberToken(NUMBER, "1.3", 1.3));

    //exponents lowercase e
    tokens = tokenize( "2e1" );
    assertTokensAreNumbers(tokens, numberToken(NUMBER, "2e1", 20.0));

    //exponents uppercase e
    tokens = tokenize( "3E4" );
    assertTokensAreNumbers(tokens, numberToken(NUMBER, "3E4", 30000.0));

    //negative number
    tokens = tokenize( "-2" );
    assertTokensAreNumbers(tokens, numberToken(NUMBER, "-2", -2.0));

    //negative decimal
    tokens = tokenize( "-4.2" );
    assertTokensAreNumbers(tokens, numberToken(NUMBER, "-4.2", -4.2));

    //negative exp
    tokens = tokenize( "-3E+4" );
    assertTokensAreNumbers(tokens, numberToken(NUMBER, "-3E+4", -30000.0));

    //negative exp
    tokens = tokenize( "2E-4" );
    assertTokensAreNumbers(tokens, numberToken(NUMBER, "2E-4", 0.0002));


    tokens = tokenize( "3E-4" );
    assertTokensAreNumbers(tokens, numberToken(NUMBER, "3E-4", 3.0 * Math.pow(10, -4)));

    tokens = tokenize( "-0.1E4" );
    assertTokensAreNumbers(tokens, numberToken(NUMBER, "-0.1E4", -1000.0));

    //double negative exp
    tokens = tokenize( "-2E-4" );
    assertTokensAreNumbers(tokens, numberToken(NUMBER, "-2E-4", -0.000200));

    //double negative exp two

    tokens = tokenize( "-2E-4 -5e1" );
    assertTokensAreNumbers(tokens, numberToken(NUMBER, "-2E-4", -0.000200), numberToken(NUMBER, "-5e1", -50.0));

      //exp and neg num
    tokens = tokenize( "3E4," );
    assertTokensAre( tokens, token(NUMBER,"3E4"),token(COMMA,","));

    //invalid input exp and decimal
    tokens = tokenize( "3E-4.0" );
      assertTokensAre( tokens, token(NUMBER, "3E-4"), token(ERROR, "."), token(NUMBER, "0"));

    //invalid input exp
    tokens = tokenize( "3E-4a" );
    assertTokensAre( tokens, token(NUMBER, "3E-4"), token(ERROR, "a"));

    //invalid input decimal
    tokens = tokenize( "3.4a" );
    assertTokensAre( tokens, token(NUMBER, "3.4"), token(ERROR, "a"));

  }

  private Token numberToken(Token.TokenType type, String value, double num) {
    return new Token( type, value, 0, 0, 0, num );
  }

  @Test
  public void testLiterals() {
    List<Token> tokens;

    tokens = tokenize( "true" );
    assertTokensAre( tokens, token( TRUE, "true" ) );

    tokens = tokenize( "false" );
    assertTokensAre( tokens, token( FALSE, "false" ) );

    tokens = tokenize( "null" );
    assertTokensAre( tokens, token( NULL, "null" ) );

  }

  @Test
  public void testCombos() {
    List<Token> tokens;

    //assortment
    tokens=tokenize("\"type\": \"array\":");
    assertTokensAre(tokens,token(STRING,"type"), token(COLON,":"), token(STRING,"array"), token(COLON,":"));


    tokens = tokenize("123, true");
    assertTokensAre( tokens, token(NUMBER, "123"), token(COMMA, ","), token(TRUE, "true"));

  }

  @Test
  public void testErrors() {
    List<Token> tokens;


    // unclosed string
    tokens = tokenize( "\"foo" );
    assertTokensAre( tokens,token(ERROR,"foo"));

    // invalid constant (constant with typo)
    tokens = tokenize( "truel" );
    assertTokensAre( tokens,token(ERROR,"truel"));

    // invalid constant (constant with typo)
    tokens = tokenize( "truel false" );
    assertTokensAre( tokens,token(ERROR,"truel"),token(FALSE,"false"));

      tokens = tokenize( "truefalse" );
      assertTokensAre(tokens, token(ERROR, ">> BAD TOKEN : truefalse"));

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
      assertTokenMatches( matches[i], tokens.get( i ) );
    }
  }
  private void assertTokensAreNumbers( List<Token> tokens, Token... matches )
  {
    if( tokens.size() != matches.length )
    {
      fail("Did not find " + matches.length + " tokens: " + tokens);
    }
    for(int i = 0; i < matches.length; i++) {
      assertEquals(matches[i].getTokenType(), tokens.get(i).getTokenType());
      assertEquals(matches[i].getTokenValue(), tokens.get(i).getTokenValue());
      assertEquals(matches[i].getTokenNumberValue(), tokens.get(i).getTokenNumberValue(), Double.MIN_VALUE);
    }
  }

  private void assertTokenMatches( Token match, Token token) {
    assertEquals(match.getTokenType(), token.getTokenType());
    if( match.getTokenValue() != null ) {
      assertEquals(match.getTokenValue(), token.getTokenValue());
    }
    if( match.getLineNumber() > 0) {
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
    return new Token( type, value, line, col, offset, 0 );
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
