package org.codehaus.groovy.runtime;

import java.io.IOException;
import java.io.Writer;

public class StringBufferWriter extends Writer {
   private StringBuffer buffer;

   public StringBufferWriter(StringBuffer buffer) {
      this.buffer = buffer;
   }

   public void write(int c) {
      this.buffer.append((char)c);
   }

   public void write(char[] text, int offset, int length) {
      if (offset >= 0 && offset <= text.length && length >= 0 && offset + length <= text.length && offset + length >= 0) {
         if (length != 0) {
            this.buffer.append(text, offset, length);
         }
      } else {
         throw new IndexOutOfBoundsException();
      }
   }

   public void write(String text) {
      this.buffer.append(text);
   }

   public void write(String text, int offset, int length) {
      this.buffer.append(text.substring(offset, offset + length));
   }

   public String toString() {
      return this.buffer.toString();
   }

   public void flush() {
   }

   public void close() throws IOException {
   }
}
