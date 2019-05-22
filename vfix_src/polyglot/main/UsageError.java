package polyglot.main;

public class UsageError extends Exception {
   final int exitCode;

   public UsageError(String s) {
      this(s, 1);
   }

   public UsageError(String s, int exitCode) {
      super(s);
      this.exitCode = exitCode;
   }
}
