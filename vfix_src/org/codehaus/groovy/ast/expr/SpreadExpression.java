package org.codehaus.groovy.ast.expr;

import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.GroovyCodeVisitor;

public class SpreadExpression extends Expression {
   private final Expression expression;

   public SpreadExpression(Expression expression) {
      this.expression = expression;
   }

   public Expression getExpression() {
      return this.expression;
   }

   public void visit(GroovyCodeVisitor visitor) {
      visitor.visitSpreadExpression(this);
   }

   public Expression transformExpression(ExpressionTransformer transformer) {
      Expression ret = new SpreadExpression(transformer.transform(this.expression));
      ret.setSourcePosition(this);
      return ret;
   }

   public String getText() {
      return "*" + this.expression.getText();
   }

   public ClassNode getType() {
      return this.expression.getType();
   }
}
