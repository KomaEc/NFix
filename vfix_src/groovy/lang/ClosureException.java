package groovy.lang;

public class ClosureException extends RuntimeException {
   private final Closure closure;

   public ClosureException(Closure closure, Throwable cause) {
      super("Exception thrown by call to closure: " + closure + " reaason: " + cause, cause);
      this.closure = closure;
   }

   public Closure getClosure() {
      return this.closure;
   }
}
