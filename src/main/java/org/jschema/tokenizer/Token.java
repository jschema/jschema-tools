package org.jschema.tokenizer;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Token
{
  private TokenType _tokenType;
  private String _value;
  private int _lineNumber;
  private int _column;
  private int _offset;
  private double _num;
  static final Set<String> constants = initializeConstants();

  public enum TokenType {
    PUNCTUATION,
    STRING,
    NUMBER,
    CONSTANT,
    ERROR,
    EOF
  }

  public Token( TokenType tokenType, String value, int lineNumber, int column, int offset, double num)
  {
    _tokenType = tokenType;
    _value = value;
    _lineNumber = lineNumber;
    _column = column;
    _offset = offset;
    _num = num;
  }

  private static Set<String> initializeConstants() {
    HashSet<String> set = new HashSet<String>();
    set.add("true");
    set.add("false");
    set.add("null");
    return Collections.unmodifiableSet(set);
  }

  public String getTokenValue() {
    return _value;
  }

  public double getTokenNumberValue() {
    return _num;
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

  public int getOffset() {
    return _offset;
  }

  @Override
  public String toString()
  {
    return   "[" + _tokenType + "]" + _value + ":" + _offset;
  }
}
