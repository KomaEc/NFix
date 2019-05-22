package org.codehaus.groovy.ast.expr;

import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.GroovyCodeVisitor;

public class CastExpression extends Expression {
   private final Expression expression;
   private boolean ignoreAutoboxing;
   private boolean coerce;

   public static CastExpression asExpression(ClassNode type, Expression expression) {
      CastExpression answer = new CastExpression(type, expression);
      answer.setCoerce(true);
      return answer;
   }

   public CastExpression(ClassNode type, Expression expression) {
      this(type, expression, false);
   }

   public CastExpression(ClassNode type, Expression expression, boolean ignoreAutoboxing) {
      this.ignoreAutoboxing = false;
      this.coerce = false;
      super.setType(type);
      this.expression = expression;
      this.ignoreAutoboxing = ignoreAutoboxing;
   }

   public boolean isIgnoringAutoboxing() {
      return this.ignoreAutoboxing;
   }

   public boolean isCoerce() {
      return this.coerce;
   }

   public void setCoerce(boolean coerce) {
      this.coerce = coerce;
   }

   public String toString() {
      return super.toString() + "[(" + this.getType().getName() + ") " + this.expression + "]";
   }

   public void visit(GroovyCodeVisitor visitor) {
      visitor.visitCastExpression(this);
   }

   public Expression transformExpression(ExpressionTransformer transformer) {
      CastExpression ret = new CastExpression(this.getType(), transformer.transform(this.expression));
      ret.setSourcePosition(this);
      ret.setCoerce(this.isCoerce());
      return ret;
   }

   public String getText() {
      return "(" + this.getType() + ") " + this.expression.getText();
   }

   public Expression getExpression() {
      return this.expression;
   }

   public void setType(ClassNode t) {
      super.setType(t);
   }
}
