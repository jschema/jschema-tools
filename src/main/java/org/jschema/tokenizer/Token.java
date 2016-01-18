package org.jschema.tokenizer;

public class Token
{
  private TokenType _tokenType;
  private String _value;
  private int _lineNumber;
  private int _column;
  private int _offset;

  public enum TokenType {
    PUNCTUATION,
    STRING,
    NUMBER,
    CONSTANT,
    ERROR,
  }

  public Token( TokenType tokenType, String value, int lineNumber, int column, int offset )
  {
    _tokenType = tokenType;
    _value = value;
    _lineNumber = lineNumber;
    _column = column;
    _offset = offset;
  }

  public String getTokenValue() {
    return _value;
  }

  public TokenType getTokenType() {
    return _tokenType;
  }

  public int getLineNumber() {
    return _lineNumber;
  }

  public int getColumn() {
    return _column;
  }

  public int getOffest() {
    return _offset;
  }

  @Override
  public String toString()
  {
    return   "[" + _tokenType + "]" + _value + ":" + _offset;
  }
}
