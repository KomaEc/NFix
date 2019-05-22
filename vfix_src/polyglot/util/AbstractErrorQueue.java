package polyglot.util;

public abstract class AbstractErrorQueue implements ErrorQueue {
   protected boolean flushed;
   protected int errorCount = 0;
   protected final int limit;
   protected final String name;

   public AbstractErrorQueue(int limit, String name) {
      this.limit = limit;
      this.name = name;
      this.flushed = true;
   }

   public final void enqueue(int type, String message) {
      this.enqueue(type, message, (Position)null);
   }

   public final void enqueue(int type, String message, Position position) {
      this.enqueue(new ErrorInfo(type, message, position));
   }

   public final void enqueue(ErrorInfo e) {
      if (e.getErrorKind() != 0) {
         ++this.errorCount;
      }

      this.flushed = false;
      this.displayError(e);
      if (this.errorCount >= this.limit) {
         this.tooManyErrors(e);
         this.flush();
         throw new ErrorLimitError();
      }
   }

   protected abstract void displayError(ErrorInfo var1);

   protected void tooManyErrors(ErrorInfo lastError) {
   }

   public void flush() {
      this.flushed = true;
   }

   public final boolean hasErrors() {
      return this.errorCount > 0;
   }

   public final int errorCount() {
      return this.errorCount;
   }
}
