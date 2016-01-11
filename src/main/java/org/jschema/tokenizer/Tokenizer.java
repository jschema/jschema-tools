package org.jschema.tokenizer;

import java.util.ArrayList;
import java.util.List;

public class Tokenizer
{
  private String _string;

  public Tokenizer( String string )
  {
    _string = string;
  }

  public List<Token> tokenize()
  {
    return new ArrayList<Token>();
  };
}
