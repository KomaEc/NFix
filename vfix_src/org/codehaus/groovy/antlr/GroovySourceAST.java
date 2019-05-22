package org.codehaus.groovy.antlr;

import groovyjarjarantlr.CommonAST;
import groovyjarjarantlr.Token;
import groovyjarjarantlr.collections.AST;
import java.util.ArrayList;
import java.util.List;

public class GroovySourceAST extends CommonAST implements Comparable, SourceInfo {
   private int line;
   private int col;
   private int lineLast;
   private int colLast;
   private String snippet;

   public GroovySourceAST() {
   }

   public GroovySourceAST(Token t) {
      super(t);
   }

   public void initialize(AST ast) {
      super.initialize(ast);
      this.line = ast.getLine();
      this.col = ast.getColumn();
      if (ast instanceof GroovySourceAST) {
         GroovySourceAST node = (GroovySourceAST)ast;
         this.lineLast = node.getLineLast();
         this.colLast = node.getColumnLast();
      }

   }

   public void initialize(Token t) {
      super.initialize(t);
      this.line = t.getLine();
      this.col = t.getColumn();
      if (t instanceof SourceInfo) {
         SourceInfo info = (SourceInfo)t;
         this.lineLast = info.getLineLast();
         this.colLast = info.getColumnLast();
      }

   }

   public void setLast(Token last) {
      this.lineLast = last.getLine();
      this.colLast = last.getColumn();
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

   public void setLine(int line) {
      this.line = line;
   }

   public int getLine() {
      return this.line;
   }

   public void setColumn(int column) {
      this.col = column;
   }

   public int getColumn() {
      return this.col;
   }

   public void setSnippet(String snippet) {
      this.snippet = snippet;
   }

   public String getSnippet() {
      return this.snippet;
   }

   public int compareTo(Object object) {
      if (object == null) {
         return 0;
      } else if (!(object instanceof AST)) {
         return 0;
      } else {
         AST that = (AST)object;
         if (this.getLine() < that.getLine()) {
            return -1;
         } else if (this.getLine() > that.getLine()) {
            return 1;
         } else if (this.getColumn() < that.getColumn()) {
            return -1;
         } else {
            return this.getColumn() > that.getColumn() ? 1 : 0;
         }
      }
   }

   public GroovySourceAST childAt(int position) {
      List list = new ArrayList();

      for(AST child = this.getFirstChild(); child != null; child = child.getNextSibling()) {
         list.add(child);
      }

      try {
         return (GroovySourceAST)list.get(position);
      } catch (IndexOutOfBoundsException var5) {
         return null;
      }
   }

   public GroovySourceAST childOfType(int type) {
      for(AST child = this.getFirstChild(); child != null; child = child.getNextSibling()) {
         if (child.getType() == type) {
            return (GroovySourceAST)child;
         }
      }

      return null;
   }

   public List<GroovySourceAST> childrenOfType(int type) {
      List<GroovySourceAST> result = new ArrayList();

      for(AST child = this.getFirstChild(); child != null; child = child.getNextSibling()) {
         if (child.getType() == type) {
            result.add((GroovySourceAST)child);
         }
      }

      return result;
   }
}
