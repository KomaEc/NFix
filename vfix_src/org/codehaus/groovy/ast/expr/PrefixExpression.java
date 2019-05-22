package org.codehaus.groovy.ast.expr;

import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.GroovyCodeVisitor;
import org.codehaus.groovy.syntax.Token;

public class PrefixExpression extends Expression {
   private Token operation;
   private Expression expression;

   public PrefixExpression(Token operation, Expression expression) {
      this.operation = operation;
      this.expression = expression;
   }

   public String toString() {
      return super.toString() + "[" + this.operation + this.expression + "]";
   }

   public void visit(GroovyCodeVisitor visitor) {
      visitor.visitPrefixExpression(this);
   }

   public Expression transformExpression(ExpressionTransformer transformer) {
      Expression ret = new PrefixExpression(this.operation, transformer.transform(this.expression));
      ret.setSourcePosition(this);
      return ret;
   }

   public void setExpression(Expression expression) {
      this.expression = expression;
   }

   public Token getOperation() {
      return this.operation;
   }

   public Expression getExpression() {
      return this.expression;
   }

   public String getText() {
      return "(" + this.operation.getText() + this.expression.getText() + ")";
   }

   public ClassNode getType() {
      return this.expression.getType();
   }
}
