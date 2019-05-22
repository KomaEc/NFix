package org.apache.maven.doxia.util;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import org.apache.maven.doxia.parser.ParseException;

public class ByLineReaderSource implements ByLineSource {
   private LineNumberReader reader;
   private int lineNumber;
   private String lastLine;
   private boolean ungetted = false;

   public ByLineReaderSource(Reader in) {
      this.reader = new LineNumberReader(in);
      this.lineNumber = -1;
   }

   public final String getNextLine() throws ParseException {
      if (this.reader == null) {
         return null;
      } else if (this.ungetted) {
         this.ungetted = false;
         return this.lastLine;
      } else {
         String line;
         try {
            line = this.reader.readLine();
            if (line == null) {
               this.reader.close();
               this.reader = null;
            } else {
               this.lineNumber = this.reader.getLineNumber();
            }
         } catch (IOException var3) {
            throw new ParseException(var3);
         }

         this.lastLine = line;
         return line;
      }
   }

   public final String getName() {
      return "";
   }

   public final int getLineNumber() {
      return this.lineNumber;
   }

   public final void close() {
      if (this.reader != null) {
         try {
            this.reader.close();
         } catch (IOException var2) {
         }
      }

      this.reader = null;
   }

   public final void ungetLine() throws IllegalStateException {
      if (this.ungetted) {
         throw new IllegalStateException("we support only one level of ungetLine()");
      } else {
         this.ungetted = true;
      }
   }

   public final void unget(String s) throws IllegalStateException {
      if (s == null) {
         throw new IllegalArgumentException("argument can't be null");
      } else {
         if (s.length() != 0) {
            this.ungetLine();
            this.lastLine = s;
         }

      }
   }
}
