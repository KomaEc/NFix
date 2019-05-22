package org.codehaus.groovy.runtime.typehandling;

public class GroovyCastException extends ClassCastException {
   public GroovyCastException(Object objectToCast, Class classToCastTo, Exception cause) {
      super(makeMessage(objectToCast, classToCastTo) + " due to: " + cause.getClass().getName() + (cause.getMessage() == null ? "" : ": " + cause.getMessage()));
   }

   public GroovyCastException(Object objectToCast, Class classToCastTo) {
      super(makeMessage(objectToCast, classToCastTo));
   }

   public GroovyCastException(String message) {
      super(message);
   }

   private static String makeMessage(Object objectToCast, Class classToCastTo) {
      String classToCastFrom;
      if (objectToCast != null) {
         classToCastFrom = objectToCast.getClass().getName();
      } else {
         objectToCast = "null";
         classToCastFrom = "null";
      }

      return "Cannot cast object '" + objectToCast + "' " + "with class '" + classToCastFrom + "' " + "to class '" + classToCastTo.getName() + "'";
   }
}
