package org.codehaus.groovy.ast.expr;

import java.util.List;
import org.codehaus.groovy.ast.GroovyCodeVisitor;
import org.codehaus.groovy.ast.Parameter;

public class ArgumentListExpression extends TupleExpression {
   public static final Object[] EMPTY_ARRAY = new Object[0];
   public static final ArgumentListExpression EMPTY_ARGUMENTS = new ArgumentListExpression();

   public ArgumentListExpression() {
   }

   public ArgumentListExpression(List<Expression> expressions) {
      super(expressions);
   }

   public ArgumentListExpression(Expression[] expressions) {
      super(expressions);
   }

   public ArgumentListExpression(Parameter[] parameters) {
      for(int i = 0; i < parameters.length; ++i) {
         Parameter parameter = parameters[i];
         this.addExpression(new VariableExpression(parameter.getName()));
      }

   }

   public ArgumentListExpression(Expression expr) {
      super(expr);
   }

   public ArgumentListExpression(Expression expr1, Expression expr2) {
      super(expr1, expr2);
   }

   public ArgumentListExpression(Expression expr1, Expression expr2, Expression expr3) {
      super(expr1, expr2, expr3);
   }

   public Expression transformExpression(ExpressionTransformer transformer) {
      Expression ret = new ArgumentListExpression(this.transformExpressions(this.getExpressions(), transformer));
      ret.setSourcePosition(this);
      return ret;
   }

   public void visit(GroovyCodeVisitor visitor) {
      visitor.visitArgumentlistExpression(this);
   }
}
