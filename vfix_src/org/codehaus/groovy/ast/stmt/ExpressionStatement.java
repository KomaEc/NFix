package org.codehaus.groovy.ast.stmt;

import org.codehaus.groovy.ast.GroovyCodeVisitor;
import org.codehaus.groovy.ast.expr.Expression;

public class ExpressionStatement extends Statement {
   private Expression expression;

   public ExpressionStatement(Expression expression) {
      if (expression == null) {
         throw new IllegalArgumentException("expression cannot be null");
      } else {
         this.expression = expression;
      }
   }

   public void visit(GroovyCodeVisitor visitor) {
      visitor.visitExpressionStatement(this);
   }

   public Expression getExpression() {
      return this.expression;
   }

   public void setExpression(Expression expression) {
      this.expression = expression;
   }

   public String getText() {
      return this.toString();
   }

   public String toString() {
      return super.toString() + "[expression:" + this.expression + "]";
   }
}
