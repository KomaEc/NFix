package org.codehaus.groovy.ast.expr;

import org.codehaus.groovy.ast.ClassHelper;
import org.codehaus.groovy.ast.GroovyCodeVisitor;

public class BooleanExpression extends Expression {
   private final Expression expression;

   public BooleanExpression(Expression expression) {
      this.expression = expression;
      this.setType(ClassHelper.boolean_TYPE);
   }

   public Expression getExpression() {
      return this.expression;
   }

   public void visit(GroovyCodeVisitor visitor) {
      visitor.visitBooleanExpression(this);
   }

   public Expression transformExpression(ExpressionTransformer transformer) {
      Expression ret = new BooleanExpression(transformer.transform(this.expression));
      ret.setSourcePosition(this);
      return ret;
   }

   public String getText() {
      return this.expression.getText();
   }
}
