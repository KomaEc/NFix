package org.codehaus.groovy.ast.expr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.codehaus.groovy.ast.GroovyCodeVisitor;

public class TupleExpression extends Expression {
   private List<Expression> expressions;

   public TupleExpression() {
      this(0);
   }

   public TupleExpression(Expression expr) {
      this(1);
      this.addExpression(expr);
   }

   public TupleExpression(Expression expr1, Expression expr2) {
      this(2);
      this.addExpression(expr1);
      this.addExpression(expr2);
   }

   public TupleExpression(Expression expr1, Expression expr2, Expression expr3) {
      this(3);
      this.addExpression(expr1);
      this.addExpression(expr2);
      this.addExpression(expr3);
   }

   public TupleExpression(int length) {
      this.expressions = new ArrayList(length);
   }

   public TupleExpression(List<Expression> expressions) {
      this.expressions = expressions;
   }

   public TupleExpression(Expression[] expressionArray) {
      this();
      this.expressions.addAll(Arrays.asList(expressionArray));
   }

   public TupleExpression addExpression(Expression expression) {
      this.expressions.add(expression);
      return this;
   }

   public List<Expression> getExpressions() {
      return this.expressions;
   }

   public void visit(GroovyCodeVisitor visitor) {
      visitor.visitTupleExpression(this);
   }

   public Expression transformExpression(ExpressionTransformer transformer) {
      Expression ret = new TupleExpression(this.transformExpressions(this.getExpressions(), transformer));
      ret.setSourcePosition(this);
      return ret;
   }

   public Expression getExpression(int i) {
      return (Expression)this.expressions.get(i);
   }

   public String getText() {
      StringBuffer buffer = new StringBuffer("(");
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

      buffer.append(")");
      return buffer.toString();
   }

   public String toString() {
      return super.toString() + this.expressions;
   }
}
