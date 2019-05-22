package polyglot.frontend;

public class CyclicDependencyException extends Exception {
   public CyclicDependencyException() {
   }

   public CyclicDependencyException(String m) {
      super(m);
   }
}
