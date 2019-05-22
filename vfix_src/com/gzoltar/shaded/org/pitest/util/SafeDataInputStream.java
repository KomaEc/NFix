package com.gzoltar.shaded.org.pitest.util;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class SafeDataInputStream {
   private final DataInputStream dis;

   public SafeDataInputStream(InputStream is) {
      this.dis = new DataInputStream(is);
   }

   public int readInt() {
      try {
         return this.dis.readInt();
      } catch (IOException var2) {
         throw Unchecked.translateCheckedException(var2);
      }
   }

   public String readString() {
      try {
         int length = this.dis.readInt();
         byte[] data = new byte[length];
         this.dis.readFully(data);
         return new String(data, "UTF-8");
      } catch (IOException var3) {
         throw Unchecked.translateCheckedException(var3);
      }
   }

   public <T> T read(Class<T> type) {
      return IsolationUtils.fromXml(this.readString());
   }

   public void close() {
      try {
         this.dis.close();
      } catch (IOException var2) {
         throw Unchecked.translateCheckedException(var2);
      }
   }

   public byte readByte() {
      try {
         return this.dis.readByte();
      } catch (IOException var2) {
         throw Unchecked.translateCheckedException(var2);
      }
   }

   public boolean readBoolean() {
      try {
         return this.dis.readBoolean();
      } catch (IOException var2) {
         throw Unchecked.translateCheckedException(var2);
      }
   }

   public long readLong() {
      try {
         return this.dis.readLong();
      } catch (IOException var2) {
         throw Unchecked.translateCheckedException(var2);
      }
   }
}
