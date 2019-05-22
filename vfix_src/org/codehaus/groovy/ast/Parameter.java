package org.codehaus.groovy.ast;

import org.codehaus.groovy.ast.expr.Expression;

public class Parameter extends AnnotatedNode implements Variable {
   public static final Parameter[] EMPTY_ARRAY = new Parameter[0];
   private ClassNode type;
   private final String name;
   private boolean dynamicTyped;
   private Expression defaultValue;
   private boolean hasDefaultValue;
   private boolean inStaticContext;
   private boolean closureShare;

   public Parameter(ClassNode type, String name) {
      this.closureShare = false;
      this.name = name;
      this.setType(type);
      this.hasDefaultValue = false;
   }

   public Parameter(ClassNode type, String name, Expression defaultValue) {
      this(type, name);
      this.defaultValue = defaultValue;
      this.hasDefaultValue = defaultValue != null;
   }

   public String toString() {
      return super.toString() + "[name:" + this.name + (this.type == null ? "" : " type: " + this.type.getName()) + ", hasDefaultValue: " + this.hasInitialExpression() + "]";
   }

   public String getName() {
      return this.name;
   }

   public ClassNode getType() {
      return this.type;
   }

   public void setType(ClassNode type) {
      this.type = type;
      this.dynamicTyped |= type == ClassHelper.DYNAMIC_TYPE;
   }

   public boolean hasInitialExpression() {
      return this.hasDefaultValue;
   }

   public Expression getInitialExpression() {
      return this.defaultValue;
   }

   public void setInitialExpression(Expression init) {
      this.defaultValue = init;
      this.hasDefaultValue = this.defaultValue != null;
   }

   public boolean isInStaticContext() {
      return this.inStaticContext;
   }

   public void setInStaticContext(boolean inStaticContext) {
      this.inStaticContext = inStaticContext;
   }

   public boolean isDynamicTyped() {
      return this.dynamicTyped;
   }

   public boolean isClosureSharedVariable() {
      return this.closureShare;
   }

   public void setClosureSharedVariable(boolean inClosure) {
      this.closureShare = inClosure;
   }

   public ClassNode getOriginType() {
      return this.getType();
   }
}
