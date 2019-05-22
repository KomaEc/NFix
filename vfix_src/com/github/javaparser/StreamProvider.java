package com.github.javaparser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class StreamProvider implements Provider {
   Reader _reader;

   public StreamProvider(Reader reader) {
      this._reader = reader;
   }

   public StreamProvider(InputStream stream) throws IOException {
      this._reader = new BufferedReader(new InputStreamReader(stream));
   }

   public StreamProvider(InputStream stream, String charsetName) throws IOException {
      this._reader = new BufferedReader(new InputStreamReader(stream, charsetName));
   }

   public int read(char[] buffer, int off, int len) throws IOException {
      int result = this._reader.read(buffer, off, len);
      if (result == 0 && off < buffer.length && len > 0) {
         result = -1;
      }

      return result;
   }

   public void close() throws IOException {
      this._reader.close();
   }
}
