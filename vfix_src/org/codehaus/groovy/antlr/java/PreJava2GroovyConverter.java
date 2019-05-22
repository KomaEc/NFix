package org.codehaus.groovy.antlr.java;

import groovyjarjarantlr.collections.AST;
import java.util.Stack;
import org.codehaus.groovy.antlr.GroovySourceAST;
import org.codehaus.groovy.antlr.treewalker.VisitorAdapter;

public class PreJava2GroovyConverter extends VisitorAdapter {
   private String[] tokenNames;
   private Stack stack;

   public PreJava2GroovyConverter(String[] tokenNames) {
      this.tokenNames = tokenNames;
      this.stack = new Stack();
   }

   public void visitDefault(GroovySourceAST t, int visit) {
      if (visit == 1) {
         if (t.getType() == 114) {
            this.visitJavaLiteralDo(t);
         } else if (t.getType() == 28) {
            this.visitJavaArrayInit(t);
         }
      }

   }

   private void visitJavaLiteralDo(GroovySourceAST t) {
      this.swapTwoChildren(t);
   }

   private void visitJavaArrayInit(GroovySourceAST t) {
      if (this.stack.size() > 2) {
         GroovySourceAST grandParent = this.getGrandParentNode();
         if (grandParent.getType() == 27) {
            grandParent.setType(28);
            grandParent.setFirstChild(t);
            t.setType(33);
         }
      }

   }

   public void swapTwoChildren(GroovySourceAST t) {
      GroovySourceAST a = (GroovySourceAST)t.getFirstChild();
      GroovySourceAST b = (GroovySourceAST)a.getNextSibling();
      t.setFirstChild(b);
      a.setNextSibling((AST)null);
      b.setNextSibling(a);
   }

   public void push(GroovySourceAST t) {
      this.stack.push(t);
   }

   public GroovySourceAST pop() {
      return !this.stack.empty() ? (GroovySourceAST)this.stack.pop() : null;
   }

   private GroovySourceAST getParentNode() {
      Object currentNode = this.stack.pop();
      Object parentNode = this.stack.peek();
      this.stack.push(currentNode);
      return (GroovySourceAST)parentNode;
   }

   private GroovySourceAST getGrandParentNode() {
      Object currentNode = this.stack.pop();
      Object parentNode = this.stack.pop();
      Object grandParentNode = this.stack.peek();
      this.stack.push(parentNode);
      this.stack.push(currentNode);
      return (GroovySourceAST)grandParentNode;
   }
}
