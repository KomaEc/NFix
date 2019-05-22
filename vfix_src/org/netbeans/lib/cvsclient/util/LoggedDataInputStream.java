package org.netbeans.lib.cvsclient.util;

import java.io.EOFException;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;

public class LoggedDataInputStream extends FilterInputStream {
   private long counter;

   public LoggedDataInputStream(InputStream var1) {
      super(var1);
   }

   /** @deprecated */
   public String readLine() throws IOException {
      return this.readLineBytes().getStringFromBytes();
   }

   public ByteArray readLineBytes() throws IOException {
      boolean var1 = true;
      ByteArray var2 = new ByteArray();

      label29:
      while(true) {
         if (Thread.interrupted()) {
            Thread.currentThread().interrupt();
            break;
         }

         if (this.in.available() == 0) {
            try {
               Thread.sleep(100L);
            } catch (InterruptedException var5) {
               Thread.currentThread().interrupt();
               break;
            }
         } else {
            int var4 = this.in.read();
            ++this.counter;
            switch(var4) {
            case -1:
               if (var1) {
                  throw new EOFException();
               }
            case 10:
               break label29;
            default:
               var2.add((byte)var4);
               var1 = false;
            }
         }
      }

      byte[] var3 = var2.getBytes();
      Logger.logInput(var3);
      Logger.logInput('\n');
      return var2;
   }

   public byte[] readBytes(int var1) throws IOException {
      ByteArray var2 = new ByteArray();

      label29:
      while(var1 != 0) {
         if (Thread.interrupted()) {
            Thread.currentThread().interrupt();
            break;
         }

         if (this.in.available() == 0) {
            try {
               Thread.sleep(100L);
            } catch (InterruptedException var5) {
               Thread.currentThread().interrupt();
               break;
            }
         } else {
            int var4 = this.in.read();
            ++this.counter;
            switch(var4) {
            case -1:
               break label29;
            default:
               var2.add((byte)var4);
               --var1;
            }
         }
      }

      byte[] var3 = var2.getBytes();
      Logger.logInput(var3);
      return var3;
   }

   public void close() throws IOException {
      this.in.close();
   }

   public int read(byte[] var1) throws IOException {
      int var2 = this.in.read(var1);
      if (var2 != -1) {
         Logger.logInput(var1, 0, var2);
         this.counter += (long)var2;
      }

      return var2;
   }

   public int read(byte[] var1, int var2, int var3) throws IOException {
      int var4 = this.in.read(var1, var2, var3);
      if (var4 != -1) {
         Logger.logInput(var1, var2, var4);
         this.counter += (long)var4;
      }

      return var4;
   }

   public long skip(long var1) throws IOException {
      long var3 = super.skip(var1);
      if (var3 > 0L) {
         Logger.logInput((new String("<skipped " + var3 + " bytes>")).getBytes("utf8"));
         this.counter += var3;
      }

      return var3;
   }

   public int read() throws IOException {
      while(this.in.available() == 0) {
         try {
            Thread.sleep(100L);
         } catch (InterruptedException var2) {
            Thread.currentThread().interrupt();
            throw new InterruptedIOException();
         }
      }

      int var1 = super.read();
      if (var1 != -1) {
         Logger.logInput((char)var1);
         ++this.counter;
      }

      return var1;
   }

   public InputStream getUnderlyingStream() {
      return this.in;
   }

   public void setUnderlyingStream(InputStream var1) {
      this.in = var1;
   }

   public long getCounter() {
      return this.counter;
   }
}
