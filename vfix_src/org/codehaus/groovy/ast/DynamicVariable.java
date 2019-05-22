package org.codehaus.groovy.ast;

import org.codehaus.groovy.ast.expr.Expression;

public class DynamicVariable implements Variable {
   private String name;
   private boolean closureShare = false;
   private boolean staticContext = false;

   public DynamicVariable(String name, boolean context) {
      this.name = name;
      this.staticContext = context;
   }

   public ClassNode getType() {
      return ClassHelper.DYNAMIC_TYPE;
   }

   public String getName() {
      return this.name;
   }

   public Expression getInitialExpression() {
      return null;
   }

   public boolean hasInitialExpression() {
      return false;
   }

   public boolean isInStaticContext() {
      return this.staticContext;
   }

   public boolean isDynamicTyped() {
      return true;
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
