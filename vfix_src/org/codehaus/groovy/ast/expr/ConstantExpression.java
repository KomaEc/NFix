package org.codehaus.groovy.ast.expr;

import org.codehaus.groovy.ast.ClassHelper;
import org.codehaus.groovy.ast.GroovyCodeVisitor;

public class ConstantExpression extends Expression {
   public static final ConstantExpression NULL = new ConstantExpression((Object)null);
   public static final ConstantExpression TRUE;
   public static final ConstantExpression FALSE;
   public static final ConstantExpression EMPTY_STRING;
   public static final ConstantExpression VOID;
   public static final ConstantExpression EMPTY_EXPRESSION;
   private Object value;
   private String constantName;

   public ConstantExpression(Object value) {
      this.value = value;
      if (this.value != null) {
         this.setType(ClassHelper.make(value.getClass()));
      }

   }

   public String toString() {
      return "ConstantExpression[" + this.value + "]";
   }

   public void visit(GroovyCodeVisitor visitor) {
      visitor.visitConstantExpression(this);
   }

   public Expression transformExpression(ExpressionTransformer transformer) {
      return this;
   }

   public Object getValue() {
      return this.value;
   }

   public String getText() {
      return this.value == null ? "null" : this.value.toString();
   }

   public String getConstantName() {
      return this.constantName;
   }

   public void setConstantName(String constantName) {
      this.constantName = constantName;
   }

   public boolean isNullExpression() {
      return this.value == null;
   }

   public boolean isTrueExpression() {
      return Boolean.TRUE.equals(this.value);
   }

   public boolean isFalseExpression() {
      return Boolean.FALSE.equals(this.value);
   }

   public boolean isEmptyStringExpression() {
      return "".equals(this.value);
   }

   static {
      TRUE = new ConstantExpression(Boolean.TRUE);
      FALSE = new ConstantExpression(Boolean.FALSE);
      EMPTY_STRING = new ConstantExpression("");
      VOID = new ConstantExpression(Void.class);
      EMPTY_EXPRESSION = new ConstantExpression((Object)null);
   }
}
