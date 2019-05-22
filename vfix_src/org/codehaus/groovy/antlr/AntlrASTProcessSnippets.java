package org.codehaus.groovy.antlr;

import groovyjarjarantlr.collections.AST;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AntlrASTProcessSnippets implements AntlrASTProcessor {
   public AST process(AST t) {
      List l = new ArrayList();
      this.traverse((GroovySourceAST)t, l, (Iterator)null);
      Iterator itr = l.iterator();
      if (itr.hasNext()) {
         itr.next();
      }

      this.traverse((GroovySourceAST)t, (List)null, itr);
      return t;
   }

   private void traverse(GroovySourceAST t, List l, Iterator itr) {
      for(; t != null; t = (GroovySourceAST)t.getNextSibling()) {
         if (l != null) {
            l.add(new LineColumn(t.getLine(), t.getColumn()));
         }

         if (itr != null && itr.hasNext()) {
            LineColumn lc = (LineColumn)itr.next();
            if (t.getLineLast() == 0) {
               int nextLine = lc.getLine();
               int nextColumn = lc.getColumn();
               if (nextLine < t.getLine() || nextLine == t.getLine() && nextColumn < t.getColumn()) {
                  nextLine = t.getLine();
                  nextColumn = t.getColumn();
               }

               t.setLineLast(nextLine);
               t.setColumnLast(nextColumn);
            }
         }

         GroovySourceAST child = (GroovySourceAST)t.getFirstChild();
         if (child != null) {
            this.traverse(child, l, itr);
         }
      }

   }
}
