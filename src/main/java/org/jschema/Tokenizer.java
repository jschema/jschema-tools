package org.jschema;

public class Tokenizer {
  private String source;
  private int i;
  private int line;
  private int column;
  private char ch;

  public Tokenizer(String source) {
    this.source = source;
    i = 0;
    line = 1;
    column = 0;
    nextChar();
  }

  public Token next() {
    Token T;
    eatWhiteSpace();
    switch(ch) {
      case '"':
        T = consumeString();
        break;
      case '-':
      case '0':
      case '1':
      case '2':
      case '3':
      case '4':
      case '5':
      case '6':
      case '7':
      case '8':
      case '9':
        T = consumeNumber();
        break;
      case '{':
        T = newToken(TokenType.LCURLY, "{");
        nextChar();
        break;
      case '}':
        T = newToken(TokenType.RCURLY, "}");
        nextChar();
        break;
      case '[':
        T = newToken(TokenType.LSQUARE, "[");
        nextChar();
        break;
      case ']':
        T = newToken(TokenType.RSQUARE, "]");
        nextChar();
        break;
      case ',':
        T = newToken(TokenType.COMMA, ",");
        nextChar();
        break;
      case ':':
        T = newToken(TokenType.COLON, ":");
        nextChar();
        break;
      case 't':
      case 'f':
      case 'n':
        T = consumeConstant();
        break;
      case '\0':
        T = new Token(TokenType.EOF, "EOF", line, column, 0.0);
        source = "";
        break;
      default:
        T = newToken(TokenType.ERROR, String.valueOf(ch));
        nextChar();
    }
    return T;
  }

  /*
    string = '"' {char} '"'.
    char = unescaped | "\" ('"' | "\" | "/" | "b" | "f" | "n" | "r" | "t" | "u" hex hex hex hex).
    unescaped = any printable Unicode character except '"' or "\".
  */
  private Token consumeString() {
    StringBuilder sb = new StringBuilder();
    Token T;
    nextChar();
    while(moreChars() && ch != '"') {
      if(ch == '\\') {
        nextChar();
        switch(ch) {
          case '"':
          case '\\':
          case '/':
            sb.append(ch);
            nextChar();
            break;
          case 'b':
            sb.append('\b');
            nextChar();
            break;
          case 'f':
            sb.append('\f');
            nextChar();
            break;
          case 'n':
            sb.append('\n');
            nextChar();
            break;
          case 'r':
            sb.append('\r');
            nextChar();
            break;
          case 't':
            sb.append('\t');
            nextChar();
            break;
          case 'u':
            nextChar();
            int u = 0;
            for(int i = 0; i < 4; i++) {
              if(isHexDigit(ch)) {
                u = u * 16 + ch - '0';
                if(ch >= 'A') { // handle hex numbers: 'A' = 65, '0' = 48. 'A'-'0' = 17, 17 - 7 = 10
                  u = u - 7;
                }
              } else {
                nextChar();
                return newToken(TokenType.ERROR, sb.toString());
              }
              nextChar();
            }
            sb.append((char) u);
            break;
          default:
            return newToken(TokenType.ERROR, sb.toString());
        }
      } else {
        sb.append(ch);
        nextChar();
      }
    }
    if(ch == '"') {
      T = newToken(TokenType.STRING, sb.toString());
    } else {
      T = newToken(TokenType.ERROR, sb.toString());
    }
    nextChar();
    return T;
  }

  /*
    number = [ "-" ] int [ frac ] [ exp ].
    exp = ("e" | "E") [ "-" | "+" ] digit {digit}.
    frac = "." digit {digit}.
    int = "0" |  digit19 {digit}.
    digit = "0" | "1" | ... | "9".
    digit19 = "1" | ... | "9".
  */
  private Token consumeNumber() {
    StringBuilder sb = new StringBuilder();
    Token T;
    int num = 0;
    int frac = 0;
    int numFracDigit = 0;
    int exp = 0;
    boolean neg = false;
    if(ch == '-') {
      sb.append(ch);
      nextChar();
      neg = true;
    }
    if(ch != '0') {
      num = consumeDigits(sb);
      if(num == -1) {
        return newToken(TokenType.ERROR, sb.toString());
      }
    } else {
      sb.append(ch);
      nextChar();
    }
    if(ch == '.') {
      sb.append(ch);
      nextChar();
      numFracDigit = sb.length();
      frac = consumeDigits(sb);
      if(frac == -1) {
        return newToken(TokenType.ERROR, sb.toString());
      }
      numFracDigit = sb.length() - numFracDigit;
    }
    if(ch == 'E' || ch == 'e') {
      sb.append(ch);
      nextChar();
      boolean negExp = false;
      if(ch == '-') {
        sb.append(ch);
        nextChar();
        negExp = true;
      } else if(ch == '+') {
        sb.append(ch);
        nextChar();
      }
      exp = consumeDigits(sb);
      if(exp == -1) {
        return newToken(TokenType.ERROR, sb.toString());
      }
      if(negExp) {
        exp = -exp;
      }
    }
    double doubleValue = num;
    if(frac != 0) {
      doubleValue += (frac * Math.pow(10, -numFracDigit));
    }
    if(exp != 0) {
      doubleValue = doubleValue * Math.pow(10, exp);
    }
    if(neg) {
      doubleValue = -doubleValue;
    }
    T = newNumberToken(TokenType.NUMBER, sb.toString(), doubleValue);
    return T;
  }

  private int consumeDigits(StringBuilder sb) {
    int num = 0;
    if(isDigit(ch)) {
      while(moreChars() && isDigit(ch)) {
        sb.append(ch);
        num = num * 10 + ch - '0';
        nextChar();
      }
    } else {
      num = -1;
    }
    return num;
  }

  private boolean isDigit(char ch) {
    return ch >= '0' && ch <= '9';
  }

  private boolean isHexDigit(char ch) {
    return ch >= '0' && ch <= '9' || ch >= 'A' && ch <= 'F' || ch >= 'a' && ch <= 'f';
  }

  private Token consumeConstant() {
    StringBuilder sb = new StringBuilder();
    Token T;
    do {
      sb.append(ch);
      nextChar();
    } while(moreChars() && (ch >= 'a' && ch <= 'z' || ch >= 'A' && ch <= 'Z'));
    String str = sb.toString();
    TokenType type = Token.constants.get(str);
    if(type == null) {
      T = newToken(TokenType.ERROR, str);
    } else {
      T = newToken(type, str);
    }
    return T;
  }

  private Token newToken(TokenType type, String tokenValue) {
    return new Token(type, tokenValue, line, column, 0);
  }

  private Token newNumberToken(TokenType type, String tokenValue, double num) {
    return new Token(type, tokenValue, line, column, num);
  }

  private void eatWhiteSpace() {
    while(moreChars() && (ch == '\t' || ch == '\n' || ch == '\r' || ch == ' ')) {
      nextChar();
    }
  }

  private void nextChar() {
    if(i < source.length()) {
      ch = source.charAt(i);
      if(ch == '\n') {
        line++;
        column = 0;
      }
      i = i + 1;
      column = column + 1;
    } else {
      ch = '\0';
    }
  }

  private boolean moreChars() {
    return ch != '\0';
  }
}
