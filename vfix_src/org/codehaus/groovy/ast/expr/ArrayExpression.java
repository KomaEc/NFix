package org.codehaus.groovy.ast.expr;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.GroovyCodeVisitor;

public class ArrayExpression extends Expression {
   private List<Expression> expressions;
   private List<Expression> sizeExpression;
   private ClassNode elementType;

   private static ClassNode makeArray(ClassNode base, List<Expression> sizeExpression) {
      ClassNode ret = base.makeArray();
      if (sizeExpression == null) {
         return ret;
      } else {
         int size = sizeExpression.size();

         for(int i = 1; i < size; ++i) {
            ret = ret.makeArray();
         }

         return ret;
      }
   }

   public ArrayExpression(ClassNode elementType, List<Expression> expressions, List<Expression> sizeExpression) {
      super.setType(makeArray(elementType, sizeExpression));
      if (expressions == null) {
         expressions = Collections.emptyList();
      }

      this.elementType = elementType;
      this.expressions = expressions;
      this.sizeExpression = sizeExpression;
      Iterator i$ = expressions.iterator();

      Object item;
      while(i$.hasNext()) {
         item = i$.next();
         if (item != null && !(item instanceof Expression)) {
            throw new ClassCastException("Item: " + item + " is not an Expression");
         }
      }

      if (sizeExpression != null) {
         i$ = sizeExpression.iterator();

         while(i$.hasNext()) {
            item = i$.next();
            if (!(item instanceof Expression)) {
               throw new ClassCastException("Item: " + item + " is not an Expression");
            }
         }
      }

   }

   public ArrayExpression(ClassNode elementType, List<Expression> expressions) {
      this(elementType, expressions, (List)null);
   }

   public void addExpression(Expression expression) {
      this.expressions.add(expression);
   }

   public List<Expression> getExpressions() {
      return this.expressions;
   }

   public void visit(GroovyCodeVisitor visitor) {
      visitor.visitArrayExpression(this);
   }

   public boolean isDynamic() {
      return false;
   }

   public Expression transformExpression(ExpressionTransformer transformer) {
      List<Expression> exprList = this.transformExpressions(this.expressions, transformer);
      List<Expression> sizes = null;
      if (this.sizeExpression != null) {
         sizes = this.transformExpressions(this.sizeExpression, transformer);
      }

      Expression ret = new ArrayExpression(this.elementType, exprList, sizes);
      ret.setSourcePosition(this);
      return ret;
   }

   public Expression getExpression(int i) {
      return (Expression)this.expressions.get(i);
   }

   public ClassNode getElementType() {
      return this.elementType;
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

   public List<Expression> getSizeExpression() {
      return this.sizeExpression;
   }

   public String toString() {
      return super.toString() + this.expressions;
   }
}
