package org.codehaus.groovy.binding;

import groovy.lang.Closure;

public class ClosureSourceBinding implements SourceBinding {
   Closure closure;
   Object[] arguments;

   public ClosureSourceBinding(Closure closure) {
      this(closure, new Object[0]);
   }

   public ClosureSourceBinding(Closure closure, Object[] arguments) {
      this.closure = closure;
      this.arguments = arguments;
   }

   public Closure getClosure() {
      return this.closure;
   }

   public void setClosure(Closure closure) {
      this.closure = closure;
   }

   public Object getSourceValue() {
      return this.closure.call(this.arguments);
   }

   public void setClosureArguments(Object[] arguments) {
      this.arguments = arguments;
   }

   public void setClosureArgument(Object argument) {
      this.arguments = new Object[]{argument};
   }
}
