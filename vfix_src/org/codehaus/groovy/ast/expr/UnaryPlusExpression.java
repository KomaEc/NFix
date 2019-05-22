package org.codehaus.groovy.ast.expr;

import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.GroovyCodeVisitor;

public class UnaryPlusExpression extends Expression {
   private final Expression expression;

   public UnaryPlusExpression(Expression expression) {
      this.expression = expression;
   }

   public Expression getExpression() {
      return this.expression;
   }

   public void visit(GroovyCodeVisitor visitor) {
      visitor.visitUnaryPlusExpression(this);
   }

   public Expression transformExpression(ExpressionTransformer transformer) {
      Expression ret = new UnaryPlusExpression(transformer.transform(this.expression));
      ret.setSourcePosition(this);
      return ret;
   }

   public String getText() {
      return this.expression.getText();
   }

   public ClassNode getType() {
      return this.expression.getType();
   }

   public boolean isDynamic() {
      return false;
   }
}
