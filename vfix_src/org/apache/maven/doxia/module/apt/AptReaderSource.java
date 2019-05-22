package org.apache.maven.doxia.module.apt;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;

public class AptReaderSource implements AptSource {
   private LineNumberReader reader;
   private int lineNumber;

   public AptReaderSource(Reader in) {
      this.reader = new LineNumberReader(in);
      this.lineNumber = -1;
   }

   public String getNextLine() throws AptParseException {
      if (this.reader == null) {
         return null;
      } else {
         try {
            String line = this.reader.readLine();
            if (line == null) {
               this.reader.close();
               this.reader = null;
            } else {
               this.lineNumber = this.reader.getLineNumber();
            }

            return line;
         } catch (IOException var3) {
            throw new AptParseException(var3);
         }
      }
   }

   public String getName() {
      return "";
   }

   public int getLineNumber() {
      return this.lineNumber;
   }

   public void close() {
      if (this.reader != null) {
         try {
            this.reader.close();
         } catch (IOException var2) {
         }
      }

      this.reader = null;
   }
}
