package org.netbeans.lib.cvsclient.util;

public class ByteArray {
   private byte[] bytesBuffer = new byte[50];
   private int length = 0;

   public void add(byte var1) {
      if (this.bytesBuffer.length <= this.length) {
         byte[] var2 = new byte[this.length + this.length / 2];
         System.arraycopy(this.bytesBuffer, 0, var2, 0, this.bytesBuffer.length);
         this.bytesBuffer = var2;
      }

      this.bytesBuffer[this.length++] = var1;
   }

   public byte[] getBytes() {
      byte[] var1 = new byte[this.length];
      System.arraycopy(this.bytesBuffer, 0, var1, 0, this.length);
      return var1;
   }

   public String getStringFromBytes() {
      return new String(this.bytesBuffer, 0, this.length);
   }

   public void reset() {
      this.length = 0;
   }
}
