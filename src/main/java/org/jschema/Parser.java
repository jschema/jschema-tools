package org.jschema;

import java.util.ArrayList;
import java.util.List;

/*
  jsonText = value.
  object = "{" [ member { "," member } ] "}".
  member = string ":" value.
  array = "[" [ value { "," value } ] "]".
  value = object | array | number | string | "true" | "false" | "null" .
  number = [ "-" ] int [ frac ] [ exp ].
  exp = ("e" | "E") [ "-" | "+" ] digit {digit}.
  frac = "." digit {digit}.
  int = "0" |  digit19 {digit}.
  digit = "0" | "1" | ... | "9".
  hex = digit | "A" | ... | "F" | "a" | ... | "F".
  digit19 = "1" | ... | "9".
  string = '"' {char} '"'.
  char = unescaped | "\" ('"' | "\" | "/" | "b" | "f" | "n" | "r" | "t" | "u" hex hex hex hex).
  unescaped = any printable Unicode character except '"' or "\".
  ws =  { " " | "\t" | "\n" | "\r" }.
 */

public class Parser {
  private final Tokenizer tokenizer;
  private Token T;
  private List<String> errors;



  public Parser(Tokenizer tokenizer) {
    this.tokenizer = tokenizer;
    errors = new ArrayList<String>();
    next();
  }

  private void next() {
    T = tokenizer.next();
  }

  // jsonText = value.
  public void parse() {
    if(isValue()) {
      parseValue();
    }
    else {
      addError();
    }
  }

  // array = "[" [ value { "," value } ] "]".
  private void parseArray() {
    next();
    if(isValue()) {
      parseValue();
      while(T.getTokenType() == TokenType.COMMA) {
        next();
        parseValue();
      }
    }
    check(TokenType.RSQUARE, "]");
  }

  // object = "{" [ member { "," member } ] "}".
  private void parseObject() {
    next();
    if(T.getTokenType() == TokenType.STRING) {
      parseMember();
      while(T.getTokenType() == TokenType.COMMA) {
        next();
        parseMember();
      }
    }
    check(TokenType.RCURLY, "}");
  }

  // member = string ":" value.
  private void parseMember() {
    check(TokenType.STRING, "a string");
    check(TokenType.COLON, ":");
    parseValue();
  }

  // value = object | array | number | string | "true" | "false" | "null" .
  private void parseValue() {
    switch(T.getTokenType()) {
      case LCURLY:
        parseObject();
        break;
      case LSQUARE:
        parseArray();
        break;
      case NUMBER:
        next();
        break;
      case STRING:
        next();
        break;
      case TRUE:
        next();
        break;
      case FALSE:
        next();
        break;
      case NULL:
        next();
        break;
      default:
        addError();
    }
  }

  private void addError() {
    errors.add("[" + T.getLineNumber() + ":" + T.getColumn() + "] Unexpected token '" + T.getTokenValue() + "'");
    next();
  }

  private void check(TokenType type, String s) {
    if(T.getTokenType() != type) {
      errors.add("[" + T.getLineNumber() + ":" +  T.getColumn() + "] expecting '" + s + "', found '" + "'" + T.getTokenValue() + "'");
    }
    next();
  }

  public boolean isValue() {
    TokenType type = T.getTokenType();
    return type == TokenType.LCURLY || type == TokenType.LSQUARE ||
           type == TokenType.NUMBER || type == TokenType.STRING ||
           type == TokenType.TRUE || type == TokenType.FALSE ||
           type == TokenType.NULL;
  }

  public List<String> getErrors() {
    return errors;
  }
}
