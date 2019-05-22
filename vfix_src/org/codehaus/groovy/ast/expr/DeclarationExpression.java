package org.codehaus.groovy.ast.expr;

import org.codehaus.groovy.GroovyBugError;
import org.codehaus.groovy.ast.GroovyCodeVisitor;
import org.codehaus.groovy.syntax.Token;

public class DeclarationExpression extends BinaryExpression {
   public DeclarationExpression(VariableExpression left, Token operation, Expression right) {
      super(left, operation, right);
   }

   public DeclarationExpression(Expression left, Token operation, Expression right) {
      super(left, operation, right);
      this.check(left, right);
   }

   private void check(Expression left, Expression right) {
      if (!(left instanceof VariableExpression)) {
         if (!(left instanceof TupleExpression)) {
            throw new GroovyBugError("illegal left expression for declaration: " + left);
         }

         TupleExpression tuple = (TupleExpression)left;
         if (tuple.getExpressions().size() == 0) {
            throw new GroovyBugError("one element required for left side");
         }
      }

   }

   public void visit(GroovyCodeVisitor visitor) {
      visitor.visitDeclarationExpression(this);
   }

   public VariableExpression getVariableExpression() {
      return (VariableExpression)this.getLeftExpression();
   }

   public void setLeftExpression(Expression leftExpression) {
      this.check(leftExpression, this.getRightExpression());
      super.setLeftExpression(leftExpression);
   }

   public void setRightExpression(Expression rightExpression) {
      this.check(this.getLeftExpression(), rightExpression);
      super.setRightExpression(rightExpression);
   }

   public Expression transformExpression(ExpressionTransformer transformer) {
      Expression left = this.getLeftExpression();
      Expression ret = new DeclarationExpression(transformer.transform(this.getLeftExpression()), this.getOperation(), transformer.transform(this.getRightExpression()));
      ret.setSourcePosition(this);
      return ret;
   }

   public boolean isMultipleAssignmentDeclaration() {
      return this.getLeftExpression() instanceof ArgumentListExpression;
   }
}
