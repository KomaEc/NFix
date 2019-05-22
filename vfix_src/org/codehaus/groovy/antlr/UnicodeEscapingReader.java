package org.codehaus.groovy.antlr;

import groovyjarjarantlr.CharScanner;
import java.io.IOException;
import java.io.Reader;

public class UnicodeEscapingReader extends Reader {
   private final Reader reader;
   private CharScanner lexer;
   private boolean hasNextChar = false;
   private int nextChar;
   private final SourceBuffer sourceBuffer;

   public UnicodeEscapingReader(Reader reader, SourceBuffer sourceBuffer) {
      this.reader = reader;
      this.sourceBuffer = sourceBuffer;
   }

   public void setLexer(CharScanner lexer) {
      this.lexer = lexer;
   }

   public int read(char[] cbuf, int off, int len) throws IOException {
      int c = 0;

      int count;
      for(count = 0; count < len && (c = this.read()) != -1; ++count) {
         cbuf[off + count] = (char)c;
      }

      return count == 0 && c == -1 ? -1 : count;
   }

   public int read() throws IOException {
      if (this.hasNextChar) {
         this.hasNextChar = false;
         this.write(this.nextChar);
         return this.nextChar;
      } else {
         int c = this.reader.read();
         if (c != 92) {
            this.write(c);
            return c;
         } else {
            c = this.reader.read();
            if (c != 117) {
               this.hasNextChar = true;
               this.nextChar = c;
               this.write(92);
               return 92;
            } else {
               do {
                  c = this.reader.read();
               } while(c == 117);

               this.checkHexDigit(c);
               StringBuffer charNum = new StringBuffer();
               charNum.append((char)c);

               int rv;
               for(rv = 0; rv < 3; ++rv) {
                  c = this.reader.read();
                  this.checkHexDigit(c);
                  charNum.append((char)c);
               }

               rv = Integer.parseInt(charNum.toString(), 16);
               this.write(rv);
               return rv;
            }
         }
      }
   }

   private void write(int c) {
      if (this.sourceBuffer != null) {
         this.sourceBuffer.write(c);
      }

   }

   private void checkHexDigit(int c) throws IOException {
      if (c < 48 || c > 57) {
         if (c < 97 || c > 102) {
            if (c < 65 || c > 70) {
               this.hasNextChar = true;
               this.nextChar = c;
               throw new IOException("Did not find four digit hex character code. line: " + this.lexer.getLine() + " col:" + this.lexer.getColumn());
            }
         }
      }
   }

   public void close() throws IOException {
      this.reader.close();
   }
}
