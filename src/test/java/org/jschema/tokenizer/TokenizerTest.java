package org.jschema.tokenizer;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import static org.jschema.tokenizer.Token.TokenType.*;

public class TokenizerTest
{

  @Test
  public void bootstrapTest() {
    // basic constant
    List<Token> tokens = tokenize("true");
    assertTokensAre(tokens, token(CONSTANT, "true"));

    // leading whitespace
    tokens = tokenize("   true");
    assertTokensAre(tokens, token(CONSTANT, "true"));

    // trailing whitespace
    tokens = tokenize("true   ");
    assertTokensAre(tokens, token(CONSTANT, "true"));

    // two tokens whitespace
    tokens = tokenize("true   false");
    assertTokensAre(tokens, token(CONSTANT, "true"), token(CONSTANT, "false"));
  }

  @Test
  public void punctuationTest() {
    // basic symbol
    List<Token> tokens = tokenize(":");
    assertTokensAre(tokens, token(PUNCTUATION, ":"));

    // leading whitespace
    tokens = tokenize("   :");
    assertTokensAre(tokens, token(PUNCTUATION, ":"));

    // trailing whitespace
    tokens = tokenize(":    ");
    assertTokensAre(tokens, token(PUNCTUATION, ":"));

    // two tokens
    tokens = tokenize("[ ]");
    assertTokensAre(tokens, token(PUNCTUATION, "["), token(PUNCTUATION, "]"));
  }

  @Test
  public void numberTest(){
    // one-digit number
    List<Token> tokens = tokenize("5");
    assertTokensAre(tokens, token( NUMBER, "5"));

    // two-digit number
    tokens = tokenize("12");
    assertTokensAre(tokens, token( NUMBER, "12"));

    // leading whitespace
    tokens = tokenize("   12");
    assertTokensAre(tokens, token( NUMBER, "12"));

    // trailing whitespace
    tokens = tokenize("12    ");
    assertTokensAre(tokens, token( NUMBER, "12"));

    // negative number
    tokens = tokenize("-40");
    assertTokensAre(tokens, token( NUMBER, "-40"));

    // two numbers
    tokens = tokenize("12 -40");
    assertTokensAre(tokens, token( NUMBER, "12"), token( NUMBER, "-40"));

    // negative sign
    tokens = tokenize("-");
    assertTokensAre(tokens, token( ERROR, "-"));
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
