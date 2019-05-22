package org.codehaus.groovy.ast.expr;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.codehaus.groovy.ast.ClassHelper;
import org.codehaus.groovy.ast.GroovyCodeVisitor;

public class GStringExpression extends Expression {
   private String verbatimText;
   private List<ConstantExpression> strings = new ArrayList();
   private List<Expression> values = new ArrayList();

   public GStringExpression(String verbatimText) {
      this.verbatimText = verbatimText;
      super.setType(ClassHelper.GSTRING_TYPE);
   }

   public GStringExpression(String verbatimText, List<ConstantExpression> strings, List<Expression> values) {
      this.verbatimText = verbatimText;
      this.strings = strings;
      this.values = values;
      super.setType(ClassHelper.GSTRING_TYPE);
   }

   public void visit(GroovyCodeVisitor visitor) {
      visitor.visitGStringExpression(this);
   }

   public Expression transformExpression(ExpressionTransformer transformer) {
      Expression ret = new GStringExpression(this.verbatimText, this.transformExpressions(this.strings, transformer, ConstantExpression.class), this.transformExpressions(this.values, transformer));
      ret.setSourcePosition(this);
      return ret;
   }

   public String toString() {
      return super.toString() + "[strings: " + this.strings + " values: " + this.values + "]";
   }

   public String getText() {
      return this.verbatimText;
   }

   public List<ConstantExpression> getStrings() {
      return this.strings;
   }

   public List<Expression> getValues() {
      return this.values;
   }

   public void addString(ConstantExpression text) {
      if (text == null) {
         throw new NullPointerException("Cannot add a null text expression");
      } else {
         this.strings.add(text);
      }
   }

   public void addValue(Expression value) {
      if (this.strings.size() == 0) {
         this.strings.add(ConstantExpression.EMPTY_STRING);
      }

      this.values.add(value);
   }

   public Expression getValue(int idx) {
      return (Expression)this.values.get(idx);
   }

   public boolean isConstantString() {
      return this.values.isEmpty();
   }

   public Expression asConstantString() {
      StringBuffer buffer = new StringBuffer();
      Iterator i$ = this.strings.iterator();

      while(i$.hasNext()) {
         ConstantExpression expression = (ConstantExpression)i$.next();
         Object value = expression.getValue();
         if (value != null) {
            buffer.append(value);
         }
      }

      return new ConstantExpression(buffer.toString());
   }
}
