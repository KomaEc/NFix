package org.codehaus.groovy.ast.expr;

import org.codehaus.groovy.ast.ClassHelper;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.GroovyCodeVisitor;

public class TernaryExpression extends Expression {
   private BooleanExpression booleanExpression;
   private Expression trueExpression;
   private Expression falseExpression;

   public TernaryExpression(BooleanExpression booleanExpression, Expression trueExpression, Expression falseExpression) {
      this.booleanExpression = booleanExpression;
      this.trueExpression = trueExpression;
      this.falseExpression = falseExpression;
   }

   public void visit(GroovyCodeVisitor visitor) {
      visitor.visitTernaryExpression(this);
   }

   public Expression transformExpression(ExpressionTransformer transformer) {
      Expression ret = new TernaryExpression((BooleanExpression)transformer.transform(this.booleanExpression), transformer.transform(this.trueExpression), transformer.transform(this.falseExpression));
      ret.setSourcePosition(this);
      return ret;
   }

   public String toString() {
      return super.toString() + "[" + this.booleanExpression + " ? " + this.trueExpression + " : " + this.falseExpression + "]";
   }

   public BooleanExpression getBooleanExpression() {
      return this.booleanExpression;
   }

   public Expression getFalseExpression() {
      return this.falseExpression;
   }

   public Expression getTrueExpression() {
      return this.trueExpression;
   }

   public String getText() {
      return "(" + this.booleanExpression.getText() + ") ? " + this.trueExpression.getText() + " : " + this.falseExpression.getText();
   }

   public ClassNode getType() {
      return ClassHelper.OBJECT_TYPE;
   }
}
