package org.codehaus.groovy.ast.stmt;

import org.codehaus.groovy.ast.GroovyCodeVisitor;

public class EmptyStatement extends Statement {
   public static final EmptyStatement INSTANCE = new EmptyStatement();

   public void visit(GroovyCodeVisitor visitor) {
   }

   public boolean isEmpty() {
      return true;
   }
}
