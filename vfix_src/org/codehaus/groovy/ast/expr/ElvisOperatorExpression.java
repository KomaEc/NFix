package org.codehaus.groovy.ast.expr;

import org.codehaus.groovy.ast.GroovyCodeVisitor;

public class ElvisOperatorExpression extends TernaryExpression {
   public ElvisOperatorExpression(Expression base, Expression falseExpression) {
      super(getBool(base), base, falseExpression);
   }

   private static BooleanExpression getBool(Expression base) {
      BooleanExpression be = new BooleanExpression(base);
      be.setSourcePosition(base);
      return be;
   }

   public void visit(GroovyCodeVisitor visitor) {
      visitor.visitShortTernaryExpression(this);
   }

   public Expression transformExpression(ExpressionTransformer transformer) {
      Expression ret = new ElvisOperatorExpression(transformer.transform(this.getTrueExpression()), transformer.transform(this.getFalseExpression()));
      ret.setSourcePosition(this);
      return ret;
   }
}
