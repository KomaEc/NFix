package scm;

public class SchemeError extends RuntimeException {
   public SchemeError() {
   }

   public SchemeError(String s) {
      super(s);
   }
}
