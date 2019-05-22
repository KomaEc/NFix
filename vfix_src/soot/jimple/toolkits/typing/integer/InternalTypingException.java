package soot.jimple.toolkits.typing.integer;

import soot.Type;

class InternalTypingException extends RuntimeException {
   private static final long serialVersionUID = 1874994601632508834L;
   private final Type unexpectedType;

   public InternalTypingException() {
      this.unexpectedType = null;
   }

   public InternalTypingException(Type unexpectedType) {
      this.unexpectedType = unexpectedType;
   }

   public Type getUnexpectedType() {
      return this.unexpectedType;
   }

   public String toString() {
      return "Unexpected type " + this.unexpectedType;
   }
}
