package polyglot.util;

import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.PrintStream;
import java.io.Reader;
import java.util.StringTokenizer;

public class StdErrorQueue extends AbstractErrorQueue {
   private PrintStream err;

   public StdErrorQueue(PrintStream err, int limit, String name) {
      super(limit, name);
      this.err = err;
   }

   public void displayError(ErrorInfo e) {
      String message = e.getErrorKind() != 0 ? e.getMessage() : e.getErrorString() + " -- " + e.getMessage();
      Position position = e.getPosition();
      String prefix = position != null ? position.nameAndLineString() : this.name;
      int width = 0;
      this.err.print(prefix + ":");
      int width = width + prefix.length() + 1;
      int lmargin = 4;
      int rmargin = 78;
      StringTokenizer lines = new StringTokenizer(message, "\n", true);
      boolean needNewline = false;

      while(true) {
         while(lines.hasMoreTokens()) {
            String line = lines.nextToken();
            if (line.indexOf("\n") >= 0) {
               this.err.println();
               needNewline = false;
            } else {
               StringTokenizer st = new StringTokenizer(line, " ");

               while(true) {
                  if (!st.hasMoreTokens()) {
                     needNewline = true;
                     break;
                  }

                  String s = st.nextToken();
                  if (width + s.length() + 1 <= rmargin) {
                     this.err.print(" ");
                     ++width;
                  } else {
                     this.err.println();

                     for(int i = 0; i < lmargin; ++i) {
                        this.err.print(" ");
                     }

                     width = lmargin;
                  }

                  this.err.print(s);
                  width += s.length();
               }
            }

            width = lmargin;
            if (lines.hasMoreTokens()) {
               for(int i = 0; i < lmargin; ++i) {
                  this.err.print(" ");
               }
            } else if (needNewline) {
               this.err.println();
            }
         }

         if (position != null) {
            this.showError(position);
         }

         return;
      }
   }

   protected void tooManyErrors(ErrorInfo lastError) {
      Position position = lastError.getPosition();
      String prefix = position != null ? position.file() + ": " : "";
      this.err.println(prefix + "Too many errors.  Aborting compilation.");
   }

   protected Reader reader(Position pos) throws IOException {
      return pos.file() != null && pos.line() != -1 ? new FileReader(pos.file()) : null;
   }

   private void showError(Position pos) {
      try {
         Reader r = this.reader(pos);
         if (r == null) {
            return;
         }

         LineNumberReader reader = new LineNumberReader(r);

         String s;
         for(s = null; reader.getLineNumber() < pos.line(); s = reader.readLine()) {
         }

         if (s != null) {
            this.err.println(s);
            this.showErrorIndicator(pos, reader.getLineNumber(), s);
            if (pos.endLine() != pos.line() && pos.endLine() != -1 && pos.endLine() != -2) {
               if (pos.endLine() - pos.line() > 1) {
                  for(int j = 0; j < s.length() && Character.isWhitespace(s.charAt(j)); ++j) {
                     this.err.print(s.charAt(j));
                  }

                  this.err.println("...");
               }

               while(reader.getLineNumber() < pos.endLine()) {
                  s = reader.readLine();
               }

               if (s != null) {
                  this.err.println(s);
                  this.showErrorIndicator(pos, reader.getLineNumber(), s);
               }
            }
         }

         reader.close();
         this.err.println();
      } catch (IOException var6) {
      }

   }

   protected void showErrorIndicator(Position pos, int lineNum, String s) {
      if (pos.column() != -1) {
         int i = 0;

         while(i < s.length() && Character.isWhitespace(s.charAt(i))) {
            this.err.print(s.charAt(i++));
         }

         int startIndAt = i;
         int stopIndAt = s.length() - 1;
         if (pos.line() == lineNum) {
            startIndAt = pos.column();
         }

         if (pos.endLine() == lineNum) {
            stopIndAt = pos.endColumn() - 1;
         }

         if (stopIndAt < startIndAt) {
            stopIndAt = startIndAt;
         }

         if (pos.endColumn() == -1 || pos.endColumn() == -2) {
            stopIndAt = startIndAt;
         }

         while(i <= stopIndAt) {
            char c = '-';
            if (i < startIndAt) {
               c = ' ';
            }

            if (i < s.length() && s.charAt(i) == '\t') {
               c = '\t';
            }

            if (i == startIndAt && pos.line() == lineNum) {
               c = '^';
            }

            if (i == stopIndAt && pos.endLine() == lineNum) {
               c = '^';
            }

            this.err.print(c);
            ++i;
         }

         this.err.println();
      }
   }

   public void flush() {
      if (!this.flushed && this.errorCount() > 0) {
         this.err.println(this.errorCount() + " error" + (this.errorCount() > 1 ? "s." : "."));
      }

      super.flush();
   }
}
