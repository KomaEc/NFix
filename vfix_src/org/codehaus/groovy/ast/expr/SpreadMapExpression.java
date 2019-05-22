package org.codehaus.groovy.ast.expr;

import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.GroovyCodeVisitor;

public class SpreadMapExpression extends Expression {
   private Expression expression;

   public SpreadMapExpression(Expression expression) {
      this.expression = expression;
   }

   public Expression getExpression() {
      return this.expression;
   }

   public void visit(GroovyCodeVisitor visitor) {
      visitor.visitSpreadMapExpression(this);
   }

   public Expression transformExpression(ExpressionTransformer transformer) {
      Expression ret = new SpreadMapExpression(transformer.transform(this.expression));
      ret.setSourcePosition(this);
      return ret;
   }

   public String getText() {
      return "*:" + this.expression.getText();
   }

   public ClassNode getType() {
      return this.expression.getType();
   }
}
