package org.jf.dexlib2.dexbacked;

import javax.annotation.Nonnull;
import org.jf.util.ExceptionWithContext;
import org.jf.util.Utf8Utils;

public class BaseDexReader<T extends BaseDexBuffer> {
   @Nonnull
   public final T dexBuf;
   private int offset;

   public BaseDexReader(@Nonnull T dexBuf, int offset) {
      this.dexBuf = dexBuf;
      this.offset = offset;
   }

   public int getOffset() {
      return this.offset;
   }

   public void setOffset(int offset) {
      this.offset = offset;
   }

   public int readSleb128() {
      int end = this.dexBuf.baseOffset + this.offset;
      byte[] buf = this.dexBuf.buf;
      int result = buf[end++] & 255;
      if (result <= 127) {
         result = result << 25 >> 25;
      } else {
         int currentByteValue = buf[end++] & 255;
         result = result & 127 | (currentByteValue & 127) << 7;
         if (currentByteValue <= 127) {
            result = result << 18 >> 18;
         } else {
            currentByteValue = buf[end++] & 255;
            result |= (currentByteValue & 127) << 14;
            if (currentByteValue <= 127) {
               result = result << 11 >> 11;
            } else {
               currentByteValue = buf[end++] & 255;
               result |= (currentByteValue & 127) << 21;
               if (currentByteValue <= 127) {
                  result = result << 4 >> 4;
               } else {
                  currentByteValue = buf[end++] & 255;
                  if (currentByteValue > 127) {
                     throw new ExceptionWithContext("Invalid sleb128 integer encountered at offset 0x%x", new Object[]{this.offset});
                  }

                  result |= currentByteValue << 28;
               }
            }
         }
      }

      this.offset = end - this.dexBuf.baseOffset;
      return result;
   }

   public int peekSleb128Size() {
      int end = this.dexBuf.baseOffset + this.offset;
      byte[] buf = this.dexBuf.buf;
      int result = buf[end++] & 255;
      if (result > 127) {
         int currentByteValue = buf[end++] & 255;
         if (currentByteValue > 127) {
            currentByteValue = buf[end++] & 255;
            if (currentByteValue > 127) {
               currentByteValue = buf[end++] & 255;
               if (currentByteValue > 127) {
                  currentByteValue = buf[end++] & 255;
                  if (currentByteValue > 127) {
                     throw new ExceptionWithContext("Invalid sleb128 integer encountered at offset 0x%x", new Object[]{this.offset});
                  }
               }
            }
         }
      }

      return end - (this.dexBuf.baseOffset + this.offset);
   }

   public int readSmallUleb128() {
      return this.readUleb128(false);
   }

   public int peekSmallUleb128Size() {
      return this.peekUleb128Size(false);
   }

   private int readUleb128(boolean allowLarge) {
      int end = this.dexBuf.baseOffset + this.offset;
      byte[] buf = this.dexBuf.buf;
      int result = buf[end++] & 255;
      if (result > 127) {
         int currentByteValue = buf[end++] & 255;
         result = result & 127 | (currentByteValue & 127) << 7;
         if (currentByteValue > 127) {
            currentByteValue = buf[end++] & 255;
            result |= (currentByteValue & 127) << 14;
            if (currentByteValue > 127) {
               currentByteValue = buf[end++] & 255;
               result |= (currentByteValue & 127) << 21;
               if (currentByteValue > 127) {
                  int currentByteValue = buf[end++];
                  if (currentByteValue < 0) {
                     throw new ExceptionWithContext("Invalid uleb128 integer encountered at offset 0x%x", new Object[]{this.offset});
                  }

                  if ((currentByteValue & 15) > 7 && !allowLarge) {
                     throw new ExceptionWithContext("Encountered valid uleb128 that is out of range at offset 0x%x", new Object[]{this.offset});
                  }

                  result |= currentByteValue << 28;
               }
            }
         }
      }

      this.offset = end - this.dexBuf.baseOffset;
      return result;
   }

   private int peekUleb128Size(boolean allowLarge) {
      int end = this.dexBuf.baseOffset + this.offset;
      byte[] buf = this.dexBuf.buf;
      int result = buf[end++] & 255;
      if (result > 127) {
         int currentByteValue = buf[end++] & 255;
         if (currentByteValue > 127) {
            currentByteValue = buf[end++] & 255;
            if (currentByteValue > 127) {
               currentByteValue = buf[end++] & 255;
               if (currentByteValue > 127) {
                  int currentByteValue = buf[end++];
                  if (currentByteValue < 0) {
                     throw new ExceptionWithContext("Invalid uleb128 integer encountered at offset 0x%x", new Object[]{this.offset});
                  }

                  if ((currentByteValue & 15) > 7 && !allowLarge) {
                     throw new ExceptionWithContext("Encountered valid uleb128 that is out of range at offset 0x%x", new Object[]{this.offset});
                  }
               }
            }
         }
      }

      return end - (this.dexBuf.baseOffset + this.offset);
   }

   public int readLargeUleb128() {
      return this.readUleb128(true);
   }

   public int readBigUleb128() {
      int end = this.dexBuf.baseOffset + this.offset;
      byte[] buf = this.dexBuf.buf;
      int result = buf[end++] & 255;
      if (result > 127) {
         int currentByteValue = buf[end++] & 255;
         result = result & 127 | (currentByteValue & 127) << 7;
         if (currentByteValue > 127) {
            currentByteValue = buf[end++] & 255;
            result |= (currentByteValue & 127) << 14;
            if (currentByteValue > 127) {
               currentByteValue = buf[end++] & 255;
               result |= (currentByteValue & 127) << 21;
               if (currentByteValue > 127) {
                  int currentByteValue = buf[end++];
                  if (currentByteValue < 0) {
                     throw new ExceptionWithContext("Invalid uleb128 integer encountered at offset 0x%x", new Object[]{this.offset});
                  }

                  result |= currentByteValue << 28;
               }
            }
         }
      }

      this.offset = end - this.dexBuf.baseOffset;
      return result;
   }

   public int peekBigUleb128Size() {
      int end = this.dexBuf.baseOffset + this.offset;
      byte[] buf = this.dexBuf.buf;
      int result = buf[end++] & 255;
      if (result > 127) {
         int currentByteValue = buf[end++] & 255;
         if (currentByteValue > 127) {
            currentByteValue = buf[end++] & 255;
            if (currentByteValue > 127) {
               currentByteValue = buf[end++] & 255;
               if (currentByteValue > 127) {
                  int currentByteValue = buf[end++];
                  if (currentByteValue < 0) {
                     throw new ExceptionWithContext("Invalid uleb128 integer encountered at offset 0x%x", new Object[]{this.offset});
                  }
               }
            }
         }
      }

      return end - (this.dexBuf.baseOffset + this.offset);
   }

   public void skipUleb128() {
      int end = this.dexBuf.baseOffset + this.offset;
      byte[] buf = this.dexBuf.buf;
      byte currentByteValue = buf[end++];
      if (currentByteValue < 0) {
         currentByteValue = buf[end++];
         if (currentByteValue < 0) {
            currentByteValue = buf[end++];
            if (currentByteValue < 0) {
               currentByteValue = buf[end++];
               if (currentByteValue < 0) {
                  currentByteValue = buf[end++];
                  if (currentByteValue < 0) {
                     throw new ExceptionWithContext("Invalid uleb128 integer encountered at offset 0x%x", new Object[]{this.offset});
                  }
               }
            }
         }
      }

      this.offset = end - this.dexBuf.baseOffset;
   }

   public int readSmallUint() {
      int o = this.offset;
      int result = this.dexBuf.readSmallUint(o);
      this.offset = o + 4;
      return result;
   }

   public int readOptionalUint() {
      int o = this.offset;
      int result = this.dexBuf.readOptionalUint(o);
      this.offset = o + 4;
      return result;
   }

   public int peekUshort() {
      return this.dexBuf.readUshort(this.offset);
   }

   public int readUshort() {
      int o = this.offset;
      int result = this.dexBuf.readUshort(this.offset);
      this.offset = o + 2;
      return result;
   }

   public int peekUbyte() {
      return this.dexBuf.readUbyte(this.offset);
   }

   public int readUbyte() {
      int o = this.offset;
      int result = this.dexBuf.readUbyte(this.offset);
      this.offset = o + 1;
      return result;
   }

   public long readLong() {
      int o = this.offset;
      long result = this.dexBuf.readLong(this.offset);
      this.offset = o + 8;
      return result;
   }

   public int readInt() {
      int o = this.offset;
      int result = this.dexBuf.readInt(this.offset);
      this.offset = o + 4;
      return result;
   }

   public int readShort() {
      int o = this.offset;
      int result = this.dexBuf.readShort(this.offset);
      this.offset = o + 2;
      return result;
   }

   public int readByte() {
      int o = this.offset;
      int result = this.dexBuf.readByte(this.offset);
      this.offset = o + 1;
      return result;
   }

   public void skipByte() {
      ++this.offset;
   }

   public void moveRelative(int i) {
      this.offset += i;
   }

   public int readSmallUint(int offset) {
      return this.dexBuf.readSmallUint(offset);
   }

   public int readUshort(int offset) {
      return this.dexBuf.readUshort(offset);
   }

   public int readUbyte(int offset) {
      return this.dexBuf.readUbyte(offset);
   }

   public long readLong(int offset) {
      return this.dexBuf.readLong(offset);
   }

   public int readInt(int offset) {
      return this.dexBuf.readInt(offset);
   }

   public int readShort(int offset) {
      return this.dexBuf.readShort(offset);
   }

   public int readByte(int offset) {
      return this.dexBuf.readByte(offset);
   }

   public int readSizedInt(int bytes) {
      int o = this.dexBuf.baseOffset + this.offset;
      byte[] buf = this.dexBuf.buf;
      int result;
      switch(bytes) {
      case 1:
         result = buf[o];
         break;
      case 2:
         result = buf[o] & 255 | buf[o + 1] << 8;
         break;
      case 3:
         result = buf[o] & 255 | (buf[o + 1] & 255) << 8 | buf[o + 2] << 16;
         break;
      case 4:
         result = buf[o] & 255 | (buf[o + 1] & 255) << 8 | (buf[o + 2] & 255) << 16 | buf[o + 3] << 24;
         break;
      default:
         throw new ExceptionWithContext("Invalid size %d for sized int at offset 0x%x", new Object[]{bytes, this.offset});
      }

      this.offset = o + bytes - this.dexBuf.baseOffset;
      return result;
   }

   public int readSizedSmallUint(int bytes) {
      int o = this.dexBuf.baseOffset + this.offset;
      byte[] buf = this.dexBuf.buf;
      int result = 0;
      switch(bytes) {
      case 4:
         int b = buf[o + 3];
         if (b < 0) {
            throw new ExceptionWithContext("Encountered valid sized uint that is out of range at offset 0x%x", new Object[]{this.offset});
         } else {
            result = b << 24;
         }
      case 3:
         result |= (buf[o + 2] & 255) << 16;
      case 2:
         result |= (buf[o + 1] & 255) << 8;
      case 1:
         result |= buf[o] & 255;
         this.offset = o + bytes - this.dexBuf.baseOffset;
         return result;
      default:
         throw new ExceptionWithContext("Invalid size %d for sized uint at offset 0x%x", new Object[]{bytes, this.offset});
      }
   }

   public int readSizedRightExtendedInt(int bytes) {
      int o = this.dexBuf.baseOffset + this.offset;
      byte[] buf = this.dexBuf.buf;
      int result;
      switch(bytes) {
      case 1:
         result = buf[o] << 24;
         break;
      case 2:
         result = (buf[o] & 255) << 16 | buf[o + 1] << 24;
         break;
      case 3:
         result = (buf[o] & 255) << 8 | (buf[o + 1] & 255) << 16 | buf[o + 2] << 24;
         break;
      case 4:
         result = buf[o] & 255 | (buf[o + 1] & 255) << 8 | (buf[o + 2] & 255) << 16 | buf[o + 3] << 24;
         break;
      default:
         throw new ExceptionWithContext("Invalid size %d for sized, right extended int at offset 0x%x", new Object[]{bytes, this.offset});
      }

      this.offset = o + bytes - this.dexBuf.baseOffset;
      return result;
   }

   public long readSizedRightExtendedLong(int bytes) {
      int o = this.dexBuf.baseOffset + this.offset;
      byte[] buf = this.dexBuf.buf;
      long result;
      switch(bytes) {
      case 1:
         result = (long)buf[o] << 56;
         break;
      case 2:
         result = ((long)buf[o] & 255L) << 48 | (long)buf[o + 1] << 56;
         break;
      case 3:
         result = ((long)buf[o] & 255L) << 40 | ((long)buf[o + 1] & 255L) << 48 | (long)buf[o + 2] << 56;
         break;
      case 4:
         result = ((long)buf[o] & 255L) << 32 | ((long)buf[o + 1] & 255L) << 40 | ((long)buf[o + 2] & 255L) << 48 | (long)buf[o + 3] << 56;
         break;
      case 5:
         result = ((long)buf[o] & 255L) << 24 | ((long)buf[o + 1] & 255L) << 32 | ((long)buf[o + 2] & 255L) << 40 | ((long)buf[o + 3] & 255L) << 48 | (long)buf[o + 4] << 56;
         break;
      case 6:
         result = (long)((buf[o] & 255) << 16) | ((long)buf[o + 1] & 255L) << 24 | ((long)buf[o + 2] & 255L) << 32 | ((long)buf[o + 3] & 255L) << 40 | ((long)buf[o + 4] & 255L) << 48 | (long)buf[o + 5] << 56;
         break;
      case 7:
         result = (long)((buf[o] & 255) << 8 | (buf[o + 1] & 255) << 16) | ((long)buf[o + 2] & 255L) << 24 | ((long)buf[o + 3] & 255L) << 32 | ((long)buf[o + 4] & 255L) << 40 | ((long)buf[o + 5] & 255L) << 48 | (long)buf[o + 6] << 56;
         break;
      case 8:
         result = (long)(buf[o] & 255 | (buf[o + 1] & 255) << 8 | (buf[o + 2] & 255) << 16) | ((long)buf[o + 3] & 255L) << 24 | ((long)buf[o + 4] & 255L) << 32 | ((long)buf[o + 5] & 255L) << 40 | ((long)buf[o + 6] & 255L) << 48 | (long)buf[o + 7] << 56;
         break;
      default:
         throw new ExceptionWithContext("Invalid size %d for sized, right extended long at offset 0x%x", new Object[]{bytes, this.offset});
      }

      this.offset = o + bytes - this.dexBuf.baseOffset;
      return result;
   }

   public long readSizedLong(int bytes) {
      int o = this.dexBuf.baseOffset + this.offset;
      byte[] buf = this.dexBuf.buf;
      long result;
      switch(bytes) {
      case 1:
         result = (long)buf[o];
         break;
      case 2:
         result = (long)(buf[o] & 255 | buf[o + 1] << 8);
         break;
      case 3:
         result = (long)(buf[o] & 255 | (buf[o + 1] & 255) << 8 | buf[o + 2] << 16);
         break;
      case 4:
         result = (long)(buf[o] & 255 | (buf[o + 1] & 255) << 8 | (buf[o + 2] & 255) << 16) | (long)buf[o + 3] << 24;
         break;
      case 5:
         result = (long)(buf[o] & 255 | (buf[o + 1] & 255) << 8 | (buf[o + 2] & 255) << 16) | ((long)buf[o + 3] & 255L) << 24 | (long)buf[o + 4] << 32;
         break;
      case 6:
         result = (long)(buf[o] & 255 | (buf[o + 1] & 255) << 8 | (buf[o + 2] & 255) << 16) | ((long)buf[o + 3] & 255L) << 24 | ((long)buf[o + 4] & 255L) << 32 | (long)buf[o + 5] << 40;
         break;
      case 7:
         result = (long)(buf[o] & 255 | (buf[o + 1] & 255) << 8 | (buf[o + 2] & 255) << 16) | ((long)buf[o + 3] & 255L) << 24 | ((long)buf[o + 4] & 255L) << 32 | ((long)buf[o + 5] & 255L) << 40 | (long)buf[o + 6] << 48;
         break;
      case 8:
         result = (long)(buf[o] & 255 | (buf[o + 1] & 255) << 8 | (buf[o + 2] & 255) << 16) | ((long)buf[o + 3] & 255L) << 24 | ((long)buf[o + 4] & 255L) << 32 | ((long)buf[o + 5] & 255L) << 40 | ((long)buf[o + 6] & 255L) << 48 | (long)buf[o + 7] << 56;
         break;
      default:
         throw new ExceptionWithContext("Invalid size %d for sized long at offset 0x%x", new Object[]{bytes, this.offset});
      }

      this.offset = o + bytes - this.dexBuf.baseOffset;
      return result;
   }

   public String readString(int utf16Length) {
      int[] ret = new int[1];
      String value = Utf8Utils.utf8BytesWithUtf16LengthToString(this.dexBuf.buf, this.dexBuf.baseOffset + this.offset, utf16Length, ret);
      this.offset += ret[0];
      return value;
   }

   public int peekStringLength(int utf16Length) {
      int[] ret = new int[1];
      Utf8Utils.utf8BytesWithUtf16LengthToString(this.dexBuf.buf, this.dexBuf.baseOffset + this.offset, utf16Length, ret);
      return ret[0];
   }
}
