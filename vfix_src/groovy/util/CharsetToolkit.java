package groovy.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.nio.charset.Charset;
import java.util.Collection;

public class CharsetToolkit {
   private byte[] buffer;
   private Charset defaultCharset;
   private Charset charset;
   private boolean enforce8Bit = true;
   private final File file;
   private static final byte[] EMPTY_BYTE_ARRAY = new byte[0];

   public CharsetToolkit(File file) throws IOException {
      this.file = file;
      this.defaultCharset = getDefaultSystemCharset();
      this.charset = null;
      FileInputStream input = new FileInputStream(file);

      try {
         byte[] bytes = new byte[4096];
         int bytesRead = input.read(bytes);
         if (bytesRead == -1) {
            this.buffer = EMPTY_BYTE_ARRAY;
         } else if (bytesRead < 4096) {
            byte[] bytesToGuess = new byte[bytesRead];
            System.arraycopy(bytes, 0, bytesToGuess, 0, bytesRead);
            this.buffer = bytesToGuess;
         } else {
            this.buffer = bytes;
         }
      } finally {
         try {
            input.close();
         } catch (IOException var11) {
         }

      }

   }

   public void setDefaultCharset(Charset defaultCharset) {
      if (defaultCharset != null) {
         this.defaultCharset = defaultCharset;
      } else {
         this.defaultCharset = getDefaultSystemCharset();
      }

   }

   public Charset getCharset() {
      if (this.charset == null) {
         this.charset = this.guessEncoding();
      }

      return this.charset;
   }

   public void setEnforce8Bit(boolean enforce) {
      this.enforce8Bit = enforce;
   }

   public boolean getEnforce8Bit() {
      return this.enforce8Bit;
   }

   public Charset getDefaultCharset() {
      return this.defaultCharset;
   }

   private Charset guessEncoding() {
      if (this.hasUTF8Bom()) {
         return Charset.forName("UTF-8");
      } else if (this.hasUTF16LEBom()) {
         return Charset.forName("UTF-16LE");
      } else if (this.hasUTF16BEBom()) {
         return Charset.forName("UTF-16BE");
      } else {
         boolean highOrderBit = false;
         boolean validU8Char = true;
         int length = this.buffer.length;

         for(int i = 0; i < length - 6; ++i) {
            byte b0 = this.buffer[i];
            byte b1 = this.buffer[i + 1];
            byte b2 = this.buffer[i + 2];
            byte b3 = this.buffer[i + 3];
            byte b4 = this.buffer[i + 4];
            byte b5 = this.buffer[i + 5];
            if (b0 < 0) {
               highOrderBit = true;
               if (isTwoBytesSequence(b0)) {
                  if (!isContinuationChar(b1)) {
                     validU8Char = false;
                  } else {
                     ++i;
                  }
               } else if (isThreeBytesSequence(b0)) {
                  if (isContinuationChar(b1) && isContinuationChar(b2)) {
                     i += 2;
                  } else {
                     validU8Char = false;
                  }
               } else if (isFourBytesSequence(b0)) {
                  if (isContinuationChar(b1) && isContinuationChar(b2) && isContinuationChar(b3)) {
                     i += 3;
                  } else {
                     validU8Char = false;
                  }
               } else if (isFiveBytesSequence(b0)) {
                  if (isContinuationChar(b1) && isContinuationChar(b2) && isContinuationChar(b3) && isContinuationChar(b4)) {
                     i += 4;
                  } else {
                     validU8Char = false;
                  }
               } else if (isSixBytesSequence(b0)) {
                  if (isContinuationChar(b1) && isContinuationChar(b2) && isContinuationChar(b3) && isContinuationChar(b4) && isContinuationChar(b5)) {
                     i += 5;
                  } else {
                     validU8Char = false;
                  }
               } else {
                  validU8Char = false;
               }
            }

            if (!validU8Char) {
               break;
            }
         }

         if (!highOrderBit) {
            return this.enforce8Bit ? this.defaultCharset : Charset.forName("US-ASCII");
         } else {
            return validU8Char ? Charset.forName("UTF-8") : this.defaultCharset;
         }
      }
   }

   private static boolean isContinuationChar(byte b) {
      return -128 <= b && b <= -65;
   }

   private static boolean isTwoBytesSequence(byte b) {
      return -64 <= b && b <= -33;
   }

   private static boolean isThreeBytesSequence(byte b) {
      return -32 <= b && b <= -17;
   }

   private static boolean isFourBytesSequence(byte b) {
      return -16 <= b && b <= -9;
   }

   private static boolean isFiveBytesSequence(byte b) {
      return -8 <= b && b <= -5;
   }

   private static boolean isSixBytesSequence(byte b) {
      return -4 <= b && b <= -3;
   }

   public static Charset getDefaultSystemCharset() {
      return Charset.forName(System.getProperty("file.encoding"));
   }

   public boolean hasUTF8Bom() {
      if (this.buffer.length < 3) {
         return false;
      } else {
         return this.buffer[0] == -17 && this.buffer[1] == -69 && this.buffer[2] == -65;
      }
   }

   public boolean hasUTF16LEBom() {
      if (this.buffer.length < 2) {
         return false;
      } else {
         return this.buffer[0] == -1 && this.buffer[1] == -2;
      }
   }

   public boolean hasUTF16BEBom() {
      if (this.buffer.length < 2) {
         return false;
      } else {
         return this.buffer[0] == -2 && this.buffer[1] == -1;
      }
   }

   public BufferedReader getReader() throws FileNotFoundException {
      LineNumberReader reader = new LineNumberReader(new InputStreamReader(new FileInputStream(this.file), this.getCharset()));
      if (this.hasUTF8Bom() || this.hasUTF16LEBom() || this.hasUTF16BEBom()) {
         try {
            reader.read();
         } catch (IOException var3) {
         }
      }

      return reader;
   }

   public static Charset[] getAvailableCharsets() {
      Collection collection = Charset.availableCharsets().values();
      return (Charset[])((Charset[])collection.toArray(new Charset[collection.size()]));
   }
}
