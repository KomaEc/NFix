package org.codehaus.groovy.ast.expr;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.codehaus.groovy.ast.ClassHelper;
import org.codehaus.groovy.ast.GroovyCodeVisitor;

public class ListExpression extends Expression {
   private List<Expression> expressions;
   private boolean wrapped;

   public ListExpression() {
      this(new ArrayList());
   }

   public ListExpression(List<Expression> expressions) {
      this.wrapped = false;
      this.expressions = expressions;
      this.setType(ClassHelper.LIST_TYPE);
   }

   public void addExpression(Expression expression) {
      this.expressions.add(expression);
   }

   public List<Expression> getExpressions() {
      return this.expressions;
   }

   public void setWrapped(boolean value) {
      this.wrapped = value;
   }

   public boolean isWrapped() {
      return this.wrapped;
   }

   public void visit(GroovyCodeVisitor visitor) {
      visitor.visitListExpression(this);
   }

   public Expression transformExpression(ExpressionTransformer transformer) {
      Expression ret = new ListExpression(this.transformExpressions(this.getExpressions(), transformer));
      ret.setSourcePosition(this);
      return ret;
   }

   public Expression getExpression(int i) {
      return (Expression)this.expressions.get(i);
   }

   public String getText() {
      StringBuffer buffer = new StringBuffer("[");
      boolean first = true;

      Expression expression;
      for(Iterator i$ = this.expressions.iterator(); i$.hasNext(); buffer.append(expression.getText())) {
         expression = (Expression)i$.next();
         if (first) {
            first = false;
         } else {
            buffer.append(", ");
         }
      }

      buffer.append("]");
      return buffer.toString();
   }

   public String toString() {
      return super.toString() + this.expressions;
   }
}
