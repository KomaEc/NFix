package org.codehaus.groovy.ast.expr;

import groovy.lang.MetaMethod;
import org.codehaus.groovy.ast.ClassHelper;
import org.codehaus.groovy.ast.GenericsType;
import org.codehaus.groovy.ast.GroovyCodeVisitor;

public class MethodCallExpression extends Expression {
   private Expression objectExpression;
   private Expression method;
   private Expression arguments;
   private boolean spreadSafe;
   private boolean safe;
   private boolean implicitThis;
   private GenericsType[] genericsTypes;
   private boolean usesGenerics;
   public static final Expression NO_ARGUMENTS = new TupleExpression();

   /** @deprecated */
   public MetaMethod getMetaMethod() {
      return null;
   }

   public MethodCallExpression(Expression objectExpression, String method, Expression arguments) {
      this(objectExpression, (Expression)(new ConstantExpression(method)), arguments);
   }

   public MethodCallExpression(Expression objectExpression, Expression method, Expression arguments) {
      this.spreadSafe = false;
      this.safe = false;
      this.genericsTypes = null;
      this.usesGenerics = false;
      this.objectExpression = objectExpression;
      this.method = method;
      if (!(arguments instanceof TupleExpression)) {
         this.arguments = new TupleExpression(arguments);
      } else {
         this.arguments = arguments;
      }

      this.setType(ClassHelper.DYNAMIC_TYPE);
      this.setImplicitThis(true);
   }

   public void visit(GroovyCodeVisitor visitor) {
      visitor.visitMethodCallExpression(this);
   }

   public Expression transformExpression(ExpressionTransformer transformer) {
      MethodCallExpression answer = new MethodCallExpression(transformer.transform(this.objectExpression), transformer.transform(this.method), transformer.transform(this.arguments));
      answer.setSafe(this.safe);
      answer.setSpreadSafe(this.spreadSafe);
      answer.setImplicitThis(this.implicitThis);
      answer.setSourcePosition(this);
      return answer;
   }

   public Expression getArguments() {
      return this.arguments;
   }

   public void setArguments(Expression arguments) {
      if (!(arguments instanceof TupleExpression)) {
         this.arguments = new TupleExpression(arguments);
      } else {
         this.arguments = arguments;
      }

   }

   public Expression getMethod() {
      return this.method;
   }

   public void setMethod(Expression method) {
      this.method = method;
   }

   public String getMethodAsString() {
      if (!(this.method instanceof ConstantExpression)) {
         return null;
      } else {
         ConstantExpression constant = (ConstantExpression)this.method;
         return constant.getText();
      }
   }

   public void setObjectExpression(Expression objectExpression) {
      this.objectExpression = objectExpression;
   }

   public Expression getObjectExpression() {
      return this.objectExpression;
   }

   public String getText() {
      return this.objectExpression.getText() + "." + this.method.getText() + this.arguments.getText();
   }

   public boolean isSafe() {
      return this.safe;
   }

   public void setSafe(boolean safe) {
      this.safe = safe;
   }

   public boolean isSpreadSafe() {
      return this.spreadSafe;
   }

   public void setSpreadSafe(boolean value) {
      this.spreadSafe = value;
   }

   public boolean isImplicitThis() {
      return this.implicitThis;
   }

   public void setImplicitThis(boolean implicitThis) {
      this.implicitThis = implicitThis;
   }

   public String toString() {
      return super.toString() + "[object: " + this.objectExpression + " method: " + this.method + " arguments: " + this.arguments + "]";
   }

   /** @deprecated */
   public void setMetaMethod(MetaMethod mmeth) {
   }

   public GenericsType[] getGenericsTypes() {
      return this.genericsTypes;
   }

   public void setGenericsTypes(GenericsType[] genericsTypes) {
      this.usesGenerics = this.usesGenerics || genericsTypes != null;
      this.genericsTypes = genericsTypes;
   }

   public boolean isUsingGenerics() {
      return this.usesGenerics;
   }
}
