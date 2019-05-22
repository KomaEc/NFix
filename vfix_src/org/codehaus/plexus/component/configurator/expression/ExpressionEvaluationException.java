package org.codehaus.plexus.component.configurator.expression;

public class ExpressionEvaluationException extends Exception {
   public ExpressionEvaluationException(String message) {
      super(message);
   }

   public ExpressionEvaluationException(String message, Throwable cause) {
      super(message, cause);
   }
}
