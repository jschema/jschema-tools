package org.jschema;

import java.util.ArrayList;
import java.util.HashMap;
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
  public Object parse() {
    Object val = null;
    if(isValue()) {
      val = parseValue();
    }
    else {
      addError();
    }
    return val;
  }

  // array = "[" [ value { "," value } ] "]".
  private Object parseArray() {
    ArrayList arr = new ArrayList();
    next();
    if(isValue()) {
      arr.add(parseValue());
      while(T.getTokenType() == TokenType.COMMA) {
        next();
        arr.add(parseValue());
      }
    }
    check(TokenType.RSQUARE, "]");
    return arr;
  }

  // object = "{" [ member { "," member } ] "}".
  private Object parseObject() {
    HashMap map = new HashMap();
    next();
    if(T.getTokenType() == TokenType.STRING) {
      parseMember(map);
      while(T.getTokenType() == TokenType.COMMA) {
        next();
        parseMember(map);
      }
    }
    check(TokenType.RCURLY, "}");
    return map;
  }

  // member = string ":" value.
  private void parseMember(HashMap map) {
    String key = T.getTokenValue();
    check(TokenType.STRING, "a string");
    check(TokenType.COLON, ":");
    Object val = parseValue();
    map.put(key, val);
  }

  // value = object | array | number | string | "true" | "false" | "null" .
  private Object parseValue() {
    Object val;
    switch(T.getTokenType()) {
      case LCURLY:
        val = parseObject();
        break;
      case LSQUARE:
        val = parseArray();
        break;
      case NUMBER:
        val = T.getTokenNumberValue();
        next();
        break;
      case STRING:
        val = T.getTokenValue();
        next();
        break;
      case TRUE:
        val = true;
        next();
        break;
      case FALSE:
        val = false;
        next();
        break;
      case NULL:
        val = null;
        next();
        break;
      default:
        val = null;
        addError();
    }
    return val;
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
