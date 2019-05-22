package org.apache.maven.surefire.util.internal;

public class ByteBuffer {
   private final byte[] data;
   private int position;
   private static final byte comma = 44;
   private static final char[] digits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};

   public ByteBuffer(int length) {
      this.data = new byte[length];
   }

   public ByteBuffer(byte[] buf, int off, int len) {
      this.data = new byte[len];
      this.append(buf, off, len);
   }

   public void append(char chararcter) {
      this.data[this.position++] = (byte)chararcter;
   }

   public void append(byte chararcter) {
      this.data[this.position++] = chararcter;
   }

   public void comma() {
      this.data[this.position++] = 44;
   }

   public void advance(int i) {
      this.position += i;
   }

   public void append(Integer integer) {
      this.toHex(integer);
   }

   private void toHex(int i) {
      byte[] buf = new byte[32];
      int charPos = 32;
      int radix = 16;
      int mask = radix - 1;

      do {
         --charPos;
         buf[charPos] = (byte)digits[i & mask];
         i >>>= 4;
      } while(i != 0);

      this.append(buf, charPos, 32 - charPos);
   }

   public byte[] getData() {
      return this.data;
   }

   public int getlength() {
      return this.position;
   }

   public String toString() {
      return new String(this.data, 0, this.position);
   }

   public static byte[] copy(byte[] src1, int off1, int len1) {
      byte[] combined = new byte[len1];
      int pos = 0;

      for(int i = off1; i < off1 + len1; ++i) {
         combined[pos++] = src1[i];
      }

      return combined;
   }

   void append(byte[] src1, int off1, int len1) {
      for(int i = off1; i < off1 + len1; ++i) {
         this.data[this.position++] = src1[i];
      }

   }

   public static byte[] join(byte[] src1, int off1, int len1, byte[] src2, int off2, int len2) {
      byte[] combined = new byte[len1 + len2];
      int pos = 0;

      int i;
      for(i = off1; i < off1 + len1; ++i) {
         combined[pos++] = src1[i];
      }

      for(i = off2; i < off2 + len2; ++i) {
         combined[pos++] = src2[i];
      }

      return combined;
   }
}
