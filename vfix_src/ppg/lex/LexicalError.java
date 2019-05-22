package ppg.lex;

import java.io.IOException;
import java.io.OutputStream;

public class LexicalError extends Exception implements LexerResult {
   private String filename;
   private int lineNumber;
   private String message;

   public LexicalError(String filename, int lineNumber, String message) {
      this.message = message;
      this.filename = filename;
      this.lineNumber = lineNumber;
   }

   public void unparse(OutputStream o) throws IOException {
      o.write(this.toString().getBytes());
   }

   public String toString() {
      return this.filename + "(" + this.lineNumber + ") : Lexical error : " + this.message;
   }

   public String filename() {
      return this.filename;
   }

   public String getMessage() {
      return this.toString();
   }

   public int lineNumber() {
      return this.lineNumber;
   }
}
