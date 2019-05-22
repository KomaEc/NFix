package org.codehaus.groovy.antlr;

import groovyjarjarantlr.Token;

public class GroovySourceToken extends Token implements SourceInfo {
   protected int line;
   protected String text = "";
   protected int col;
   protected int lineLast;
   protected int colLast;

   public GroovySourceToken(int t) {
      super(t);
   }

   public int getLine() {
      return this.line;
   }

   public String getText() {
      return this.text;
   }

   public void setLine(int l) {
      this.line = l;
   }

   public void setText(String s) {
      this.text = s;
   }

   public String toString() {
      return "[\"" + this.getText() + "\",<" + this.type + ">," + "line=" + this.line + ",col=" + this.col + ",lineLast=" + this.lineLast + ",colLast=" + this.colLast + "]";
   }

   public int getColumn() {
      return this.col;
   }

   public void setColumn(int c) {
      this.col = c;
   }

   public int getLineLast() {
      return this.lineLast;
   }

   public void setLineLast(int lineLast) {
      this.lineLast = lineLast;
   }

   public int getColumnLast() {
      return this.colLast;
   }

   public void setColumnLast(int colLast) {
      this.colLast = colLast;
   }
}
