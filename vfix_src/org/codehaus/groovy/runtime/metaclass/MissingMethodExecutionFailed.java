package org.codehaus.groovy.runtime.metaclass;

public class MissingMethodExecutionFailed extends MissingMethodExceptionNoStack {
   private Throwable cause;

   public MissingMethodExecutionFailed(String method, Class type, Object[] arguments, boolean isStatic, Throwable cause) {
      super(method, type, arguments, isStatic);
      this.cause = cause;
   }

   public Throwable getCause() {
      return this.cause;
   }
}
