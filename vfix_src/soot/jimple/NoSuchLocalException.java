package soot.jimple;

public class NoSuchLocalException extends RuntimeException {
   public NoSuchLocalException(String s) {
      super(s);
   }

   public NoSuchLocalException() {
   }
}
