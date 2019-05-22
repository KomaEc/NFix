package org.codehaus.groovy.ast.expr;

import groovy.lang.Closure;
import org.codehaus.groovy.ast.ClassHelper;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.GroovyCodeVisitor;

public class MethodPointerExpression extends Expression {
   private Expression expression;
   private Expression methodName;

   public MethodPointerExpression(Expression expression, Expression methodName) {
      this.expression = expression;
      this.methodName = methodName;
   }

   public Expression getExpression() {
      return (Expression)(this.expression == null ? VariableExpression.THIS_EXPRESSION : this.expression);
   }

   public Expression getMethodName() {
      return this.methodName;
   }

   public void visit(GroovyCodeVisitor visitor) {
      visitor.visitMethodPointerExpression(this);
   }

   public Expression transformExpression(ExpressionTransformer transformer) {
      Expression mname = transformer.transform(this.methodName);
      MethodPointerExpression ret;
      if (this.expression == null) {
         ret = new MethodPointerExpression(VariableExpression.THIS_EXPRESSION, mname);
      } else {
         ret = new MethodPointerExpression(transformer.transform(this.expression), mname);
      }

      ret.setSourcePosition(this);
      return ret;
   }

   public String getText() {
      return this.expression == null ? "&" + this.methodName : this.expression.getText() + ".&" + this.methodName.getText();
   }

   public ClassNode getType() {
      return ClassHelper.CLOSURE_TYPE;
   }

   public boolean isDynamic() {
      return false;
   }

   public Class getTypeClass() {
      return Closure.class;
   }
}
