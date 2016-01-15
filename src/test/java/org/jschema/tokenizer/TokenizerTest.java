package org.jschema.tokenizer;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import static org.jschema.tokenizer.Token.TokenType.CONSTANT;
import static org.jschema.tokenizer.Token.TokenType.PUNCTUATION;
import static org.jschema.tokenizer.Token.TokenType.NUMBER;
import static org.jschema.tokenizer.Token.TokenType.STRING;



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

    //test cases for NUMBERS
    tokens = tokenize("1");
    assertTokensAre( tokens, token(NUMBER, "1"));

    tokens = tokenize("2");
    assertTokensAre( tokens, token(NUMBER, "2"));

    tokens = tokenize("3");
    assertTokensAre( tokens, token(NUMBER, "3"));

    tokens = tokenize("4");
    assertTokensAre( tokens, token(NUMBER, "4"));

    tokens = tokenize("5");
    assertTokensAre( tokens, token(NUMBER, "5"));

    tokens = tokenize("6");
    assertTokensAre( tokens, token(NUMBER, "6"));

    tokens = tokenize("7");
    assertTokensAre( tokens, token(NUMBER, "7"));

    tokens = tokenize("8");
    assertTokensAre( tokens, token(NUMBER, "8"));

    tokens = tokenize("9");
    assertTokensAre( tokens, token(NUMBER, "9"));

    tokens = tokenize("0");
    assertTokensAre( tokens, token(NUMBER, "0"));


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
    if( match.getOffest() > 0 )
    {
      Assert.assertEquals( match.getOffest(), token.getOffest() );
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
