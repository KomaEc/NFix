package org.codehaus.groovy.syntax;

import org.codehaus.groovy.GroovyBugError;

public class Token extends CSTNode {
   public static final Token NULL = new Token();
   public static final Token EOF = new Token(-1, "", -1, -1);
   private int type = 0;
   private int meaning = 0;
   private String text = "";
   private int startLine = -1;
   private int startColumn = -1;

   public Token(int type, String text, int startLine, int startColumn) {
      this.type = type;
      this.meaning = type;
      this.text = text;
      this.startLine = startLine;
      this.startColumn = startColumn;
   }

   private Token() {
   }

   public Token dup() {
      Token token = new Token(this.type, this.text, this.startLine, this.startColumn);
      token.setMeaning(this.meaning);
      return token;
   }

   public int getMeaning() {
      return this.meaning;
   }

   public CSTNode setMeaning(int meaning) {
      this.meaning = meaning;
      return this;
   }

   public int getType() {
      return this.type;
   }

   public int size() {
      return 1;
   }

   public CSTNode get(int index) {
      if (index > 0) {
         throw new GroovyBugError("attempt to access Token element other than root");
      } else {
         return this;
      }
   }

   public Token getRoot() {
      return this;
   }

   public String getRootText() {
      return this.text;
   }

   public String getText() {
      return this.text;
   }

   public void setText(String text) {
      this.text = text;
   }

   public int getStartLine() {
      return this.startLine;
   }

   public int getStartColumn() {
      return this.startColumn;
   }

   public Reduction asReduction() {
      return new Reduction(this);
   }

   public Reduction asReduction(CSTNode second) {
      Reduction created = this.asReduction();
      created.add(second);
      return created;
   }

   public Reduction asReduction(CSTNode second, CSTNode third) {
      Reduction created = this.asReduction(second);
      created.add(third);
      return created;
   }

   public Reduction asReduction(CSTNode second, CSTNode third, CSTNode fourth) {
      Reduction created = this.asReduction(second, third);
      created.add(fourth);
      return created;
   }

   public static Token newKeyword(String text, int startLine, int startColumn) {
      int type = Types.lookupKeyword(text);
      return type != 0 ? new Token(type, text, startLine, startColumn) : null;
   }

   public static Token newString(String text, int startLine, int startColumn) {
      return new Token(400, text, startLine, startColumn);
   }

   public static Token newIdentifier(String text, int startLine, int startColumn) {
      return new Token(440, text, startLine, startColumn);
   }

   public static Token newInteger(String text, int startLine, int startColumn) {
      return new Token(450, text, startLine, startColumn);
   }

   public static Token newDecimal(String text, int startLine, int startColumn) {
      return new Token(451, text, startLine, startColumn);
   }

   public static Token newSymbol(int type, int startLine, int startColumn) {
      return new Token(type, Types.getText(type), startLine, startColumn);
   }

   public static Token newSymbol(String type, int startLine, int startColumn) {
      return new Token(Types.lookupSymbol(type), type, startLine, startColumn);
   }

   public static Token newPlaceholder(int type) {
      Token token = new Token(0, "", -1, -1);
      token.setMeaning(type);
      return token;
   }
}
