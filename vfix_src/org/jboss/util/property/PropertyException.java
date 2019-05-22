package org.jboss.util.property;

import org.jboss.util.NestedRuntimeException;

public class PropertyException extends NestedRuntimeException {
   private static final long serialVersionUID = 452085442436956822L;

   public PropertyException(String msg) {
      super(msg);
   }

   public PropertyException(String msg, Throwable nested) {
      super(msg, nested);
   }

   public PropertyException(Throwable nested) {
      super(nested);
   }

   public PropertyException() {
   }
}
