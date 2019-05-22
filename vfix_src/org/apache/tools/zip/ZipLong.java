package org.apache.tools.zip;

public final class ZipLong implements Cloneable {
   private long value;

   public ZipLong(long value) {
      this.value = value;
   }

   public ZipLong(byte[] bytes) {
      this(bytes, 0);
   }

   public ZipLong(byte[] bytes, int offset) {
      this.value = getValue(bytes, offset);
   }

   public byte[] getBytes() {
      return getBytes(this.value);
   }

   public long getValue() {
      return this.value;
   }

   public static byte[] getBytes(long value) {
      byte[] result = new byte[]{(byte)((int)(value & 255L)), (byte)((int)((value & 65280L) >> 8)), (byte)((int)((value & 16711680L) >> 16)), (byte)((int)((value & 4278190080L) >> 24))};
      return result;
   }

   public static long getValue(byte[] bytes, int offset) {
      long value = (long)(bytes[offset + 3] << 24) & 4278190080L;
      value += (long)(bytes[offset + 2] << 16 & 16711680);
      value += (long)(bytes[offset + 1] << 8 & '\uff00');
      value += (long)(bytes[offset] & 255);
      return value;
   }

   public static long getValue(byte[] bytes) {
      return getValue(bytes, 0);
   }

   public boolean equals(Object o) {
      if (o != null && o instanceof ZipLong) {
         return this.value == ((ZipLong)o).getValue();
      } else {
         return false;
      }
   }

   public int hashCode() {
      return (int)this.value;
   }
}
