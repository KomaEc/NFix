package polyglot.util;

public class ErrorLimitError extends RuntimeException {
   public ErrorLimitError(String msg) {
      super(msg);
   }

   public ErrorLimitError() {
   }
}
