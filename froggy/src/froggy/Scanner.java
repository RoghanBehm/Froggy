package froggy.src.froggy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static froggy.src.froggy.TokenType.*;

class Scanner {
  private final String source;
  private final List<Token> tokens = new ArrayList<>();
  private int start = 0;
  private int current = 0;
  private int line = 1;

  Scanner(String source) {
    this.source = source;
  }

  List<Token> scanTokens() {
    while (!isAtEnd()) {
      start = current;
      scanToken();
    }

    tokens.add(new Token(EOF, "", null, line));
    return tokens;
  }

  private void scanToken() {
    char c = advance();
    switch (c) {
      case '/':
        if (peek() == '/') {
          while (peek() != '\n' && !isAtEnd())
            advance();
        } else {
          Froggy.error(line, "Unexpected character.");
        }
        break;
      case ' ':
      case '\r':
      case '\t':
        break;
      case '\n':
        line++;
        break;
      case '"':
        string();
        break;
      default:
        if (isDigit(c)) {
          number();
        } else if (isAlpha(c)) {
          identifier();
        } else {
          Froggy.error(line, "Unexpected character.");
        }
    }
  }

  private void number() {
    while (isDigit(peek()))
      advance();

    // Look for a fractional part.
    if (peek() == '.' && isDigit(peekNext())) {
      advance();

      while (isDigit(peek()))
        advance();
    }

    addToken(NUMBER,
        Double.parseDouble(source.substring(start, current)));
  }

  private void identifier() {
    while (isAlphaNumeric(peek()))
      advance();

    String text = source.substring(start, current);
    TokenType type = keywords.get(text);
    if (type == null)
      type = IDENTIFIER;
    addToken(type);

  }

  private void string() {
    while (peek() != '"' && !isAtEnd()) {
      if (peek() == '\n')
        line++;
      advance();
    }

    if (isAtEnd()) {
      Froggy.error(line, "Unterminated string.");
      return;
    }

    // The closing ".
    advance();

    // Trim the surrounding quotes.
    String value = source.substring(start + 1, current - 1);
    addToken(STRING, value);
  }

  private char peek() {
    if (isAtEnd())
      return '\0';
    return source.charAt(current);
  }

  private char peekNext() {
    if (current + 1 >= source.length())
      return '\0';
    return source.charAt(current + 1);
  }

  private boolean isAlpha(char c) {
    return (c >= 'a' && c <= 'z') ||
        (c >= 'A' && c <= 'Z') ||
        c == '_';
  }

  private boolean isAlphaNumeric(char c) {
    return isAlpha(c) || isDigit(c);
  }

  private boolean isDigit(char c) {
    return c >= '0' && c <= '9';
  }

  private boolean isAtEnd() {
    return current >= source.length();
  }

  private char advance() {
    return source.charAt(current++);
  }

  private void addToken(TokenType type) {
    addToken(type, null);
  }

  private void addToken(TokenType type, Object literal) {
    String text = source.substring(start, current);
    tokens.add(new Token(type, text, literal, line));
  }

  private static final Map<String, TokenType> keywords;

  static {
    keywords = new HashMap<>();
    // Stack operations
    keywords.put("RIBBIT", RIBBIT);
    keywords.put("CROAK", CROAK);
    keywords.put("PLOP", PLOP);
    keywords.put("SPLASH", SPLASH);
    keywords.put("GULP", GULP);
    keywords.put("BURP", BURP);

    // Control flow
    keywords.put("HOP", HOP);
    keywords.put("LEAP", LEAP);
    keywords.put("LILY", LILY);

    // Stack manipulation
    keywords.put("DUP", DUP);
    keywords.put("SWAP", SWAP);
    keywords.put("OVER", OVER);

    // Arithmetic
    keywords.put("ADD", ADD);
    keywords.put("SUB", SUB);
    keywords.put("MUL", MUL);
    keywords.put("DIV", DIV);

    // Comparison
    keywords.put("EQUALS", EQUALS);
    keywords.put("LESS", LESS_THAN);
  }
}