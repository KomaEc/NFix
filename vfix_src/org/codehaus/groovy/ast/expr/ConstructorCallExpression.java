package org.codehaus.groovy.ast.expr;

import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.GroovyCodeVisitor;

public class ConstructorCallExpression extends Expression {
   private final Expression arguments;
   private boolean usesAnonymousInnerClass;

   public ConstructorCallExpression(ClassNode type, Expression arguments) {
      super.setType(type);
      if (!(arguments instanceof TupleExpression)) {
         this.arguments = new TupleExpression(arguments);
      } else {
         this.arguments = arguments;
      }

   }

   public void visit(GroovyCodeVisitor visitor) {
      visitor.visitConstructorCallExpression(this);
   }

   public Expression transformExpression(ExpressionTransformer transformer) {
      Expression args = transformer.transform(this.arguments);
      ConstructorCallExpression ret = new ConstructorCallExpression(this.getType(), args);
      ret.setSourcePosition(this);
      ret.setUsingAnonymousInnerClass(this.isUsingAnonymousInnerClass());
      return ret;
   }

   public Expression getArguments() {
      return this.arguments;
   }

   public String getText() {
      String text = null;
      if (this.isSuperCall()) {
         text = "super ";
      } else if (this.isThisCall()) {
         text = "this ";
      } else {
         text = "new " + this.getType().getName();
      }

      return text + this.arguments.getText();
   }

   public String toString() {
      return super.toString() + "[type: " + this.getType() + " arguments: " + this.arguments + "]";
   }

   public boolean isSuperCall() {
      return this.getType() == ClassNode.SUPER;
   }

   public boolean isSpecialCall() {
      return this.isThisCall() || this.isSuperCall();
   }

   public boolean isThisCall() {
      return this.getType() == ClassNode.THIS;
   }

   public void setUsingAnonymousInnerClass(boolean usage) {
      this.usesAnonymousInnerClass = usage;
   }

   public boolean isUsingAnonymousInnerClass() {
      return this.usesAnonymousInnerClass;
   }

   /** @deprecated */
   @Deprecated
   public boolean isUsingAnnonymousInnerClass() {
      return this.isUsingAnonymousInnerClass();
   }
}
