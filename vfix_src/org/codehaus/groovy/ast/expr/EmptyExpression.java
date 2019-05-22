package org.codehaus.groovy.ast.expr;

import org.codehaus.groovy.ast.GroovyCodeVisitor;

public class EmptyExpression extends Expression {
   public static final EmptyExpression INSTANCE = new EmptyExpression();

   public Expression transformExpression(ExpressionTransformer transformer) {
      return this;
   }

   public void visit(GroovyCodeVisitor visitor) {
   }
}
