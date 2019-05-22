package org.codehaus.groovy.ast.expr;

import groovy.lang.MetaMethod;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.GroovyCodeVisitor;

public class StaticMethodCallExpression extends Expression {
   private ClassNode ownerType;
   private String method;
   private Expression arguments;
   private MetaMethod metaMethod = null;

   public StaticMethodCallExpression(ClassNode type, String method, Expression arguments) {
      this.ownerType = type;
      this.method = method;
      this.arguments = arguments;
   }

   public void visit(GroovyCodeVisitor visitor) {
      visitor.visitStaticMethodCallExpression(this);
   }

   public Expression transformExpression(ExpressionTransformer transformer) {
      Expression ret = new StaticMethodCallExpression(this.getOwnerType(), this.method, transformer.transform(this.arguments));
      ret.setSourcePosition(this);
      return ret;
   }

   public Expression getArguments() {
      return this.arguments;
   }

   public String getMethod() {
      return this.method;
   }

   public String getText() {
      return this.getOwnerType().getName() + "." + this.method + this.arguments.getText();
   }

   public String toString() {
      return super.toString() + "[" + this.getOwnerType().getName() + "#" + this.method + " arguments: " + this.arguments + "]";
   }

   public ClassNode getOwnerType() {
      return this.ownerType;
   }

   public void setOwnerType(ClassNode ownerType) {
      this.ownerType = ownerType;
   }

   public void setMetaMethod(MetaMethod metaMethod) {
      this.metaMethod = metaMethod;
   }

   public MetaMethod getMetaMethod() {
      return this.metaMethod;
   }
}
