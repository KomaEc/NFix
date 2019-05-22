package org.codehaus.groovy.ast.stmt;

import org.codehaus.groovy.ast.ASTNode;

public class Statement extends ASTNode {
   private String statementLabel = null;

   public String getStatementLabel() {
      return this.statementLabel;
   }

   public void setStatementLabel(String label) {
      this.statementLabel = label;
   }

   public boolean isEmpty() {
      return false;
   }
}
