package soot.jimple.toolkits.typing.integer;

public class TypeException extends Exception {
   private static final long serialVersionUID = -602930090190087993L;

   public TypeException(String message) {
      super(message);
      if (message == null) {
         throw new InternalTypingException();
      }
   }
}
