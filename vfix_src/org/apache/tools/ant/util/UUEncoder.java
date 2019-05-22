package org.apache.tools.ant.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

public class UUEncoder {
   protected static final int DEFAULT_MODE = 644;
   private static final int MAX_CHARS_PER_LINE = 45;
   private OutputStream out;
   private String name;

   public UUEncoder(String name) {
      this.name = name;
   }

   public void encode(InputStream is, OutputStream out) throws IOException {
      this.out = out;
      this.encodeBegin();
      byte[] buffer = new byte[4500];

      int count;
      int num;
      while((count = is.read(buffer, 0, buffer.length)) != -1) {
         for(int pos = 0; count > 0; count -= num) {
            num = count > 45 ? 45 : count;
            this.encodeLine(buffer, pos, num, out);
            pos += num;
         }
      }

      out.flush();
      this.encodeEnd();
   }

   private void encodeString(String n) throws IOException {
      PrintStream writer = new PrintStream(this.out);
      writer.print(n);
      writer.flush();
   }

   private void encodeBegin() throws IOException {
      this.encodeString("begin 644 " + this.name + "\n");
   }

   private void encodeEnd() throws IOException {
      this.encodeString(" \nend\n");
   }

   private void encodeLine(byte[] data, int offset, int length, OutputStream out) throws IOException {
      out.write((byte)((length & 63) + 32));
      int i = 0;

      while(i < length) {
         byte b = 1;
         byte c = 1;
         byte a = data[offset + i++];
         if (i < length) {
            b = data[offset + i++];
            if (i < length) {
               c = data[offset + i++];
            }
         }

         byte d1 = (byte)((a >>> 2 & 63) + 32);
         byte d2 = (byte)((a << 4 & 48 | b >>> 4 & 15) + 32);
         byte d3 = (byte)((b << 2 & 60 | c >>> 6 & 3) + 32);
         byte d4 = (byte)((c & 63) + 32);
         out.write(d1);
         out.write(d2);
         out.write(d3);
         out.write(d4);
      }

      out.write(10);
   }
}
