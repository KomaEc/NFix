package org.codehaus.groovy.ast.stmt;

import org.codehaus.groovy.ast.GroovyCodeVisitor;

public class BreakStatement extends Statement {
   private String label;

   public BreakStatement() {
      this((String)null);
   }

   public BreakStatement(String label) {
      this.label = label;
   }

   public String getLabel() {
      return this.label;
   }

   public void visit(GroovyCodeVisitor visitor) {
      visitor.visitBreakStatement(this);
   }
}
