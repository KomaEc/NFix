package polyglot.types;

import polyglot.util.Position;

public class BadSerializationException extends SemanticException {
   private String className;

   private static String message(String className) {
      className = className.replace('/', '.');
      return "Could not decode Polyglot type information for \"" + className + "\". The most likely cause is " + "that the compiler has " + "been modified since the class file was created.  " + "Please delete " + "the class file for \"" + className + "\", and recompile from source.";
   }

   public BadSerializationException(String className) {
      super(message(className));
      this.className = className;
   }

   public BadSerializationException(String className, Position position) {
      super(message(className), position);
      this.className = className;
   }

   public String getClassName() {
      return this.className;
   }
}
