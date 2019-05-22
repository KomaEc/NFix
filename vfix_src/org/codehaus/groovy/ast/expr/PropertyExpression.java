package org.codehaus.groovy.ast.expr;

import org.codehaus.groovy.ast.GroovyCodeVisitor;

public class PropertyExpression extends Expression {
   private Expression objectExpression;
   private Expression property;
   private boolean spreadSafe;
   private boolean safe;
   private boolean isStatic;
   private boolean implicitThis;

   public boolean isStatic() {
      return this.isStatic;
   }

   public PropertyExpression(Expression objectExpression, String property) {
      this(objectExpression, new ConstantExpression(property), false);
   }

   public PropertyExpression(Expression objectExpression, Expression property) {
      this(objectExpression, property, false);
   }

   public PropertyExpression(Expression objectExpression, Expression property, boolean safe) {
      this.spreadSafe = false;
      this.safe = false;
      this.isStatic = false;
      this.implicitThis = false;
      this.objectExpression = objectExpression;
      this.property = property;
      this.safe = safe;
   }

   public void visit(GroovyCodeVisitor visitor) {
      visitor.visitPropertyExpression(this);
   }

   public boolean isDynamic() {
      return true;
   }

   public Expression transformExpression(ExpressionTransformer transformer) {
      PropertyExpression ret = new PropertyExpression(transformer.transform(this.objectExpression), transformer.transform(this.property), this.safe);
      ret.setSpreadSafe(this.spreadSafe);
      ret.setStatic(this.isStatic);
      ret.setImplicitThis(this.implicitThis);
      ret.setSourcePosition(this);
      return ret;
   }

   public Expression getObjectExpression() {
      return this.objectExpression;
   }

   public void setObjectExpression(Expression exp) {
      this.objectExpression = exp;
   }

   public Expression getProperty() {
      return this.property;
   }

   public String getPropertyAsString() {
      if (this.property == null) {
         return null;
      } else if (!(this.property instanceof ConstantExpression)) {
         return null;
      } else {
         ConstantExpression constant = (ConstantExpression)this.property;
         return constant.getText();
      }
   }

   public String getText() {
      return this.objectExpression.getText() + "." + this.property.getText();
   }

   public boolean isSafe() {
      return this.safe;
   }

   public boolean isSpreadSafe() {
      return this.spreadSafe;
   }

   public void setSpreadSafe(boolean value) {
      this.spreadSafe = value;
   }

   public String toString() {
      return super.toString() + "[object: " + this.objectExpression + " property: " + this.property + "]";
   }

   public void setStatic(boolean aStatic) {
      this.isStatic = aStatic;
   }

   public boolean isImplicitThis() {
      return this.implicitThis;
   }

   public void setImplicitThis(boolean it) {
      this.implicitThis = it;
   }
}
