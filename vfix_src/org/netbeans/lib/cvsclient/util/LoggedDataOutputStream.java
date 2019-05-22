package org.netbeans.lib.cvsclient.util;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class LoggedDataOutputStream extends FilterOutputStream {
   private long counter;

   public LoggedDataOutputStream(OutputStream var1) {
      super(var1);
   }

   /** @deprecated */
   public void writeChars(String var1) throws IOException {
      this.writeBytes(var1);
   }

   public void writeBytes(String var1) throws IOException {
      byte[] var2 = var1.getBytes();
      this.out.write(var2);
      Logger.logOutput(var2);
      this.counter += (long)var2.length;
   }

   public void writeBytes(String var1, String var2) throws IOException {
      byte[] var3 = var1.getBytes(var2);
      this.out.write(var3);
      Logger.logOutput(var3);
      this.counter += (long)var3.length;
   }

   public void write(int var1) throws IOException {
      super.write(var1);
      ++this.counter;
   }

   public void write(byte[] var1) throws IOException {
      super.write(var1);
      this.counter += (long)var1.length;
   }

   public void write(byte[] var1, int var2, int var3) throws IOException {
      super.write(var1, var2, var3);
      this.counter += (long)var3;
   }

   public void close() throws IOException {
      this.out.close();
   }

   public OutputStream getUnderlyingStream() {
      return this.out;
   }

   public void setUnderlyingStream(OutputStream var1) {
      this.out = var1;
   }

   public long getCounter() {
      return this.counter;
   }
}
