package soot.jimple.toolkits.typing;

public class TypeException extends Exception {
   private static final long serialVersionUID = -2484942383485179989L;

   public TypeException(String message) {
      super(message);
      if (message == null) {
         throw new InternalTypingException();
      }
   }
}
