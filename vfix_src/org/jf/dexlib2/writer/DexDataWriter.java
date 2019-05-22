package org.jf.dexlib2.writer;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.annotation.Nonnull;
import org.jf.util.ExceptionWithContext;

public class DexDataWriter extends BufferedOutputStream {
   private int filePosition;
   private byte[] tempBuf;
   private byte[] zeroBuf;

   public DexDataWriter(@Nonnull OutputStream output, int filePosition) {
      this(output, filePosition, 262144);
   }

   public DexDataWriter(@Nonnull OutputStream output, int filePosition, int bufferSize) {
      super(output, bufferSize);
      this.tempBuf = new byte[8];
      this.zeroBuf = new byte[3];
      this.filePosition = filePosition;
   }

   public void write(int b) throws IOException {
      ++this.filePosition;
      super.write(b);
   }

   public void write(byte[] b) throws IOException {
      this.write(b, 0, b.length);
   }

   public void write(byte[] b, int off, int len) throws IOException {
      this.filePosition += len;
      super.write(b, off, len);
   }

   public void writeLong(long value) throws IOException {
      this.writeInt((int)value);
      this.writeInt((int)(value >> 32));
   }

   public static void writeInt(OutputStream out, int value) throws IOException {
      out.write(value);
      out.write(value >> 8);
      out.write(value >> 16);
      out.write(value >> 24);
   }

   public void writeInt(int value) throws IOException {
      writeInt(this, value);
   }

   public void writeShort(int value) throws IOException {
      if (value >= -32768 && value <= 32767) {
         this.write(value);
         this.write(value >> 8);
      } else {
         throw new ExceptionWithContext("Short value out of range: %d", new Object[]{value});
      }
   }

   public void writeUshort(int value) throws IOException {
      if (value >= 0 && value <= 65535) {
         this.write(value);
         this.write(value >> 8);
      } else {
         throw new ExceptionWithContext("Unsigned short value out of range: %d", new Object[]{value});
      }
   }

   public void writeUbyte(int value) throws IOException {
      if (value >= 0 && value <= 255) {
         this.write(value);
      } else {
         throw new ExceptionWithContext("Unsigned byte value out of range: %d", new Object[]{value});
      }
   }

   public static void writeUleb128(OutputStream out, int value) throws IOException {
      while(((long)value & 4294967295L) > 127L) {
         out.write(value & 127 | 128);
         value >>>= 7;
      }

      out.write(value);
   }

   public void writeUleb128(int value) throws IOException {
      writeUleb128(this, value);
   }

   public static void writeSleb128(OutputStream out, int value) throws IOException {
      if (value >= 0) {
         while(true) {
            if (value <= 63) {
               out.write(value & 127);
               break;
            }

            out.write(value & 127 | 128);
            value >>>= 7;
         }
      } else {
         while(value < -64) {
            out.write(value & 127 | 128);
            value >>= 7;
         }

         out.write(value & 127);
      }

   }

   public void writeSleb128(int value) throws IOException {
      writeSleb128(this, value);
   }

   public void writeEncodedValueHeader(int valueType, int valueArg) throws IOException {
      this.write(valueType | valueArg << 5);
   }

   public void writeEncodedInt(int valueType, int value) throws IOException {
      int index = 0;
      if (value >= 0) {
         while(value > 127) {
            this.tempBuf[index++] = (byte)value;
            value >>= 8;
         }
      } else {
         while(value < -128) {
            this.tempBuf[index++] = (byte)value;
            value >>= 8;
         }
      }

      this.tempBuf[index++] = (byte)value;
      this.writeEncodedValueHeader(valueType, index - 1);
      this.write(this.tempBuf, 0, index);
   }

   public void writeEncodedLong(int valueType, long value) throws IOException {
      int index = 0;
      if (value >= 0L) {
         while(value > 127L) {
            this.tempBuf[index++] = (byte)((int)value);
            value >>= 8;
         }
      } else {
         while(value < -128L) {
            this.tempBuf[index++] = (byte)((int)value);
            value >>= 8;
         }
      }

      this.tempBuf[index++] = (byte)((int)value);
      this.writeEncodedValueHeader(valueType, index - 1);
      this.write(this.tempBuf, 0, index);
   }

   public void writeEncodedUint(int valueType, int value) throws IOException {
      int index = 0;

      do {
         this.tempBuf[index++] = (byte)value;
         value >>>= 8;
      } while(value != 0);

      this.writeEncodedValueHeader(valueType, index - 1);
      this.write(this.tempBuf, 0, index);
   }

   public void writeEncodedFloat(int valueType, float value) throws IOException {
      this.writeRightZeroExtendedInt(valueType, Float.floatToRawIntBits(value));
   }

   protected void writeRightZeroExtendedInt(int valueType, int value) throws IOException {
      int index = 3;

      do {
         this.tempBuf[index--] = (byte)((value & -16777216) >>> 24);
         value <<= 8;
      } while(value != 0);

      int firstElement = index + 1;
      int encodedLength = 4 - firstElement;
      this.writeEncodedValueHeader(valueType, encodedLength - 1);
      this.write(this.tempBuf, firstElement, encodedLength);
   }

   public void writeEncodedDouble(int valueType, double value) throws IOException {
      this.writeRightZeroExtendedLong(valueType, Double.doubleToRawLongBits(value));
   }

   protected void writeRightZeroExtendedLong(int valueType, long value) throws IOException {
      int index = 7;

      do {
         this.tempBuf[index--] = (byte)((int)((value & -72057594037927936L) >>> 56));
         value <<= 8;
      } while(value != 0L);

      int firstElement = index + 1;
      int encodedLength = 8 - firstElement;
      this.writeEncodedValueHeader(valueType, encodedLength - 1);
      this.write(this.tempBuf, firstElement, encodedLength);
   }

   public void writeString(String string) throws IOException {
      int len = string.length();
      if (this.tempBuf.length <= string.length() * 3) {
         this.tempBuf = new byte[string.length() * 3];
      }

      byte[] buf = this.tempBuf;
      int bufPos = 0;

      for(int i = 0; i < len; ++i) {
         char c = string.charAt(i);
         if (c != 0 && c < 128) {
            buf[bufPos++] = (byte)c;
         } else if (c < 2048) {
            buf[bufPos++] = (byte)(c >> 6 & 31 | 192);
            buf[bufPos++] = (byte)(c & 63 | 128);
         } else {
            buf[bufPos++] = (byte)(c >> 12 & 15 | 224);
            buf[bufPos++] = (byte)(c >> 6 & 63 | 128);
            buf[bufPos++] = (byte)(c & 63 | 128);
         }
      }

      this.write(buf, 0, bufPos);
   }

   public void align() throws IOException {
      int zeros = -this.getPosition() & 3;
      if (zeros > 0) {
         this.write(this.zeroBuf, 0, zeros);
      }

   }

   public int getPosition() {
      return this.filePosition;
   }
}
