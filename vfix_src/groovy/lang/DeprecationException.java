package groovy.lang;

public class DeprecationException extends RuntimeException {
   public DeprecationException(String message) {
      super(message);
   }

   public DeprecationException(String message, Throwable cause) {
      super(message, cause);
   }
}
