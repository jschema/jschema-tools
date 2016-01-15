package org.jschema.tokenizer;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import static org.jschema.tokenizer.Token.TokenType.*;

public class TokenizerTest
{

  @Test
  public void parseTest() {
    Parser parser = new Parser();
    String json = "{ \"glossary\": { \"title\": \"example glossary\", \"GlossDiv\": { \"title\": \"S\", \"GlossList\": { \"GlossEntry\": { \"ID\": \"SGML\", \"SortAs\": \"SGML\", \"GlossTerm\": \"Standard Generalized Markup Language\", \"Acronym\": \"SGML\", \"Abbrev\": \"ISO 8879:1986\", \"GlossDef\": { \"para\": \"A meta-markup language, used to create markup languages such as DocBook.\", \"GlossSeeAlso\": [\"GML\", \"XML\"] }, \"GlossSee\": \"markup\" } } } } }";
    parser.parse(json);
  }

  @Test
  public void bootstrapTest()
  {
    // basic constant
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
  public void testNumbers()
  {
    // basic number
    List<Token> tokens = tokenize( "123" );
    assertTokensAre( tokens, token( NUMBER, "123" ) );

    // leading whitespace
    tokens = tokenize( "   123.45" );
    assertTokensAre( tokens, token( NUMBER, "123.45" ) );

    // trailing whitespace
    tokens = tokenize( "123.56   " );
    assertTokensAre( tokens, token( NUMBER, "123.56" ) );

    // trailing whitespace
    tokens = tokenize( "123   " );
    assertTokensAre( tokens, token( NUMBER, "123" ) );

    // two tokens whitespace
    tokens = tokenize( "123   456" );
    assertTokensAre( tokens, token( NUMBER, "123" ), token( NUMBER, "456" ) );
  }

  @Test
  public void testPunctuation()
  {
    // basic punctuation
    List<Token> tokens = tokenize( "{" );
    assertTokensAre( tokens, token( PUNCTUATION, "{" ) );

    // curly braces
    tokens = tokenize( "{ }" );
    assertTokensAre( tokens, token( PUNCTUATION, "{" ), token( PUNCTUATION, "}" ) );

    // square brackets
    tokens = tokenize( "[ ]" );
    assertTokensAre( tokens, token( PUNCTUATION, "[" ), token( PUNCTUATION, "]" ) );

    // colons
    tokens = tokenize( ": " );
    assertTokensAre( tokens, token( PUNCTUATION, ":" ) );

    // comma
    tokens = tokenize( ", " );
    assertTokensAre( tokens, token( PUNCTUATION, "," ) );
  }

  @Test
  public void testString() {
    // basic string
    List<Token> tokens = tokenize( "\"hello world\"" );
    assertTokensAre( tokens, token( STRING, "\"hello world\"" ) );

    // leading whitespace
    tokens = tokenize( "   \"hello world\"" );
    assertTokensAre( tokens, token( STRING, "\"hello world\"" ) );

    // trailing whitespace
    tokens = tokenize( "\"hello world\"   " );
    assertTokensAre( tokens, token( STRING, "\"hello world\"" ) );

    // trailing whitespace
    tokens = tokenize( "\"hello world\"   " );
    assertTokensAre( tokens, token( STRING, "\"hello world\"" ) );

    // two tokens whitespace
    tokens = tokenize( "\"hello world\"   \"guidewire\"" );
    assertTokensAre( tokens, token( STRING, "\"hello world\"" ), token( STRING, "\"guidewire\"" ) );
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
    Assert.assertEquals( match.getTokenType(), token.getTokenType() );
    if( match.getTokenValue() != null )
    {
      Assert.assertEquals( match.getTokenValue(), token.getTokenValue() );
    }
    if( match.getLineNumber() > 0 )
    {
      Assert.assertEquals( match.getLineNumber(), token.getLineNumber() );
    }
    if( match.getOffset() > 0 )
    {
      Assert.assertEquals( match.getOffset(), token.getOffset() );
    }
    if( match.getColumn() > 0 )
    {
      Assert.assertEquals( match.getColumn(), token.getColumn() );
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

}
