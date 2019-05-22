package org.jboss.util.property;

import org.jboss.util.NestedError;

public class PropertyError extends NestedError {
   private static final long serialVersionUID = -4956548740655706282L;

   public PropertyError(String msg) {
      super(msg);
   }

   public PropertyError(String msg, Throwable nested) {
      super(msg, nested);
   }

   public PropertyError(Throwable nested) {
      super(nested);
   }

   public PropertyError() {
   }
}
