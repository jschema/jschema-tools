package org.jschema.tokenizer;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class TokenizerTest
{

  @Test
  public void bootstrapTest()
  {
    List<Token> tokens = tok( "true" );
    Assert.assertEquals( 1, tokens.size() );
  }

  private List<Token> tok( String aTrue )
  {
    return new Tokenizer( aTrue ).tokenize();
  }

}
