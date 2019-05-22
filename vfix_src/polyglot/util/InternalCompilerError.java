package polyglot.util;

public class InternalCompilerError extends RuntimeException {
   Position pos;

   public InternalCompilerError(String msg) {
      this(msg, (Position)null);
   }

   public InternalCompilerError(Throwable cause) {
      this(cause.getMessage(), cause);
   }

   public InternalCompilerError(String msg, Throwable cause) {
      this(msg, (Position)null, cause);
   }

   public InternalCompilerError(Position position, String msg) {
      this(msg, position);
   }

   public InternalCompilerError(String msg, Position position) {
      super(msg);
      this.pos = position;
   }

   public InternalCompilerError(String msg, Position position, Throwable cause) {
      super(msg, cause);
      this.pos = position;
   }

   public Position position() {
      return this.pos;
   }

   public String message() {
      return super.getMessage();
   }

   public String getMessage() {
      return this.pos == null ? this.message() : this.pos + ": " + this.message();
   }
}
