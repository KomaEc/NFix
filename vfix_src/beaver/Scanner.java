package beaver;

import java.io.IOException;

public abstract class Scanner {
   public abstract Symbol nextToken() throws IOException, Scanner.Exception;

   public static class Exception extends java.lang.Exception {
      public final int line;
      public final int column;

      public Exception(String msg) {
         this(0, 0, msg);
      }

      public Exception(int line, int column, String msg) {
         super(msg);
         this.line = line;
         this.column = column;
      }
   }
}
