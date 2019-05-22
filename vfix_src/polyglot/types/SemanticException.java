package polyglot.types;

import polyglot.main.Report;
import polyglot.util.Position;

public class SemanticException extends Exception {
   protected Position position;

   public SemanticException() {
      trace(this, 5);
   }

   public SemanticException(Throwable cause) {
      super(cause);
      trace(this, 5);
   }

   public SemanticException(Position position) {
      this.position = position;
      trace(this, 5);
   }

   public SemanticException(String m) {
      super(m);
      trace(this, 5);
   }

   public SemanticException(String m, Throwable cause) {
      super(m, cause);
      trace(this, 5);
   }

   public SemanticException(String m, Position position) {
      super(m);
      this.position = position;
      trace(this, 5);
   }

   public Position position() {
      return this.position;
   }

   static void trace(Exception e, int level) {
      if (Report.should_report("errors", level)) {
         e.printStackTrace();
      }

   }
}
