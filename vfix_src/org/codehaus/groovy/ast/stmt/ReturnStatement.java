package org.codehaus.groovy.ast.stmt;

import org.codehaus.groovy.ast.GroovyCodeVisitor;
import org.codehaus.groovy.ast.expr.ConstantExpression;
import org.codehaus.groovy.ast.expr.Expression;

public class ReturnStatement extends Statement {
   public static final ReturnStatement RETURN_NULL_OR_VOID;
   private Expression expression;

   public ReturnStatement(ExpressionStatement statement) {
      this(statement.getExpression());
      this.setStatementLabel(statement.getStatementLabel());
   }

   public ReturnStatement(Expression expression) {
      this.expression = expression;
   }

   public void visit(GroovyCodeVisitor visitor) {
      visitor.visitReturnStatement(this);
   }

   public Expression getExpression() {
      return this.expression;
   }

   public String getText() {
      return "return " + this.expression.getText();
   }

   public void setExpression(Expression expression) {
      this.expression = expression;
   }

   public boolean isReturningNullOrVoid() {
      return this.expression instanceof ConstantExpression && ((ConstantExpression)this.expression).isNullExpression();
   }

   static {
      RETURN_NULL_OR_VOID = new ReturnStatement(ConstantExpression.NULL);
   }
}
