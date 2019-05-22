package com.gzoltar.shaded.org.pitest.util;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class SafeDataOutputStream {
   private final DataOutputStream dos;

   public SafeDataOutputStream(OutputStream os) {
      this.dos = new DataOutputStream(os);
   }

   public void writeInt(int value) {
      try {
         this.dos.writeInt(value);
      } catch (IOException var3) {
         throw Unchecked.translateCheckedException(var3);
      }
   }

   public void writeString(String str) {
      try {
         byte[] data = str.getBytes("UTF-8");
         this.dos.writeInt(data.length);
         this.dos.write(data);
      } catch (IOException var3) {
         throw Unchecked.translateCheckedException(var3);
      }
   }

   public <T> void write(T value) {
      this.writeString(IsolationUtils.toXml(value));
   }

   public void flush() {
      try {
         this.dos.flush();
      } catch (IOException var2) {
         throw Unchecked.translateCheckedException(var2);
      }
   }

   public void close() {
      try {
         this.dos.close();
      } catch (IOException var2) {
         throw Unchecked.translateCheckedException(var2);
      }
   }

   public void writeByte(byte b) {
      try {
         this.dos.writeByte(b);
      } catch (IOException var3) {
         throw Unchecked.translateCheckedException(var3);
      }
   }

   public void writeBoolean(boolean b) {
      try {
         this.dos.writeBoolean(b);
      } catch (IOException var3) {
         throw Unchecked.translateCheckedException(var3);
      }
   }

   public void writeLong(long l) {
      try {
         this.dos.writeLong(l);
      } catch (IOException var4) {
         throw Unchecked.translateCheckedException(var4);
      }
   }
}
