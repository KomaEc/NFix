package org.codehaus.groovy.ast.stmt;

import org.codehaus.groovy.ast.GroovyCodeVisitor;

public class ContinueStatement extends Statement {
   private String label;

   public ContinueStatement() {
      this((String)null);
   }

   public ContinueStatement(String label) {
      this.label = label;
   }

   public String getLabel() {
      return this.label;
   }

   public void visit(GroovyCodeVisitor visitor) {
      visitor.visitContinueStatement(this);
   }
}
