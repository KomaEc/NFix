package com.github.javaparser;

import java.io.IOException;

public class StringProvider implements Provider {
   String _string;
   int _position = 0;
   int _size;

   public StringProvider(String string) {
      this._string = string;
      this._size = string.length();
   }

   public int read(char[] cbuf, int off, int len) throws IOException {
      int numCharsOutstandingInString = this._size - this._position;
      if (numCharsOutstandingInString == 0) {
         return -1;
      } else {
         int numBytesInBuffer = cbuf.length;
         int numBytesToRead = numBytesInBuffer - off;
         numBytesToRead = numBytesToRead > len ? len : numBytesToRead;
         if (numBytesToRead > numCharsOutstandingInString) {
            numBytesToRead = numCharsOutstandingInString;
         }

         this._string.getChars(this._position, this._position + numBytesToRead, cbuf, off);
         this._position += numBytesToRead;
         return numBytesToRead;
      }
   }

   public void close() throws IOException {
      this._string = null;
   }
}
