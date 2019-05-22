package org.apache.tools.zip;

public final class ZipShort implements Cloneable {
   private int value;

   public ZipShort(int value) {
      this.value = value;
   }

   public ZipShort(byte[] bytes) {
      this(bytes, 0);
   }

   public ZipShort(byte[] bytes, int offset) {
      this.value = getValue(bytes, offset);
   }

   public byte[] getBytes() {
      byte[] result = new byte[]{(byte)(this.value & 255), (byte)((this.value & '\uff00') >> 8)};
      return result;
   }

   public int getValue() {
      return this.value;
   }

   public static byte[] getBytes(int value) {
      byte[] result = new byte[]{(byte)(value & 255), (byte)((value & '\uff00') >> 8)};
      return result;
   }

   public static int getValue(byte[] bytes, int offset) {
      int value = bytes[offset + 1] << 8 & '\uff00';
      value += bytes[offset] & 255;
      return value;
   }

   public static int getValue(byte[] bytes) {
      return getValue(bytes, 0);
   }

   public boolean equals(Object o) {
      if (o != null && o instanceof ZipShort) {
         return this.value == ((ZipShort)o).getValue();
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.value;
   }
}
