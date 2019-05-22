package org.apache.maven.doxia.macro.manager;

public class MacroNotFoundException extends Exception {
   public MacroNotFoundException(String message) {
      super(message);
   }

   public MacroNotFoundException(Throwable cause) {
      super(cause);
   }

   public MacroNotFoundException(String message, Throwable cause) {
      super(message, cause);
   }
}
