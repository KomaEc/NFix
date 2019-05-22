package org.jf.dexlib2.dexbacked;

import javax.annotation.Nonnull;
import org.jf.util.ExceptionWithContext;

public class BaseDexBuffer {
   @Nonnull
   final byte[] buf;
   final int baseOffset;

   public BaseDexBuffer(@Nonnull byte[] buf) {
      this(buf, 0);
   }

   public BaseDexBuffer(@Nonnull byte[] buf, int offset) {
      this.buf = buf;
      this.baseOffset = offset;
   }

   public int readSmallUint(int offset) {
      byte[] buf = this.buf;
      offset += this.baseOffset;
      int result = buf[offset] & 255 | (buf[offset + 1] & 255) << 8 | (buf[offset + 2] & 255) << 16 | buf[offset + 3] << 24;
      if (result < 0) {
         throw new ExceptionWithContext("Encountered small uint that is out of range at offset 0x%x", new Object[]{offset});
      } else {
         return result;
      }
   }

   public int readOptionalUint(int offset) {
      byte[] buf = this.buf;
      offset += this.baseOffset;
      int result = buf[offset] & 255 | (buf[offset + 1] & 255) << 8 | (buf[offset + 2] & 255) << 16 | buf[offset + 3] << 24;
      if (result < -1) {
         throw new ExceptionWithContext("Encountered optional uint that is out of range at offset 0x%x", new Object[]{offset});
      } else {
         return result;
      }
   }

   public int readUshort(int offset) {
      byte[] buf = this.buf;
      offset += this.baseOffset;
      return buf[offset] & 255 | (buf[offset + 1] & 255) << 8;
   }

   public int readUbyte(int offset) {
      return this.buf[offset + this.baseOffset] & 255;
   }

   public long readLong(int offset) {
      byte[] buf = this.buf;
      offset += this.baseOffset;
      return (long)(buf[offset] & 255 | (buf[offset + 1] & 255) << 8 | (buf[offset + 2] & 255) << 16) | ((long)buf[offset + 3] & 255L) << 24 | ((long)buf[offset + 4] & 255L) << 32 | ((long)buf[offset + 5] & 255L) << 40 | ((long)buf[offset + 6] & 255L) << 48 | (long)buf[offset + 7] << 56;
   }

   public int readLongAsSmallUint(int offset) {
      byte[] buf = this.buf;
      offset += this.baseOffset;
      long result = (long)(buf[offset] & 255 | (buf[offset + 1] & 255) << 8 | (buf[offset + 2] & 255) << 16) | ((long)buf[offset + 3] & 255L) << 24 | ((long)buf[offset + 4] & 255L) << 32 | ((long)buf[offset + 5] & 255L) << 40 | ((long)buf[offset + 6] & 255L) << 48 | (long)buf[offset + 7] << 56;
      if (result >= 0L && result <= 2147483647L) {
         return (int)result;
      } else {
         throw new ExceptionWithContext("Encountered out-of-range ulong at offset 0x%x", new Object[]{offset});
      }
   }

   public int readInt(int offset) {
      byte[] buf = this.buf;
      offset += this.baseOffset;
      return buf[offset] & 255 | (buf[offset + 1] & 255) << 8 | (buf[offset + 2] & 255) << 16 | buf[offset + 3] << 24;
   }

   public int readShort(int offset) {
      byte[] buf = this.buf;
      offset += this.baseOffset;
      return buf[offset] & 255 | buf[offset + 1] << 8;
   }

   public int readByte(int offset) {
      return this.buf[this.baseOffset + offset];
   }

   @Nonnull
   public BaseDexReader readerAt(int offset) {
      return new BaseDexReader(this, offset);
   }

   @Nonnull
   protected byte[] getBuf() {
      return this.buf;
   }

   protected int getBaseOffset() {
      return this.baseOffset;
   }
}
