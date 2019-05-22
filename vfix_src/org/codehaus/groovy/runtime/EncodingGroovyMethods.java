package org.codehaus.groovy.runtime;

import groovy.lang.StringWriterIOException;
import groovy.lang.Writable;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class EncodingGroovyMethods {
   private static final char[] T_TABLE = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=".toCharArray();
   private static final String CHUNK_SEPARATOR = "\r\n";

   public static Writable encodeBase64(Byte[] data, boolean chunked) {
      return encodeBase64(DefaultTypeTransformation.convertToByteArray(data), chunked);
   }

   public static Writable encodeBase64(Byte[] data) {
      return encodeBase64(DefaultTypeTransformation.convertToByteArray(data), false);
   }

   public static Writable encodeBase64(final byte[] data, final boolean chunked) {
      return new Writable() {
         public Writer writeTo(Writer writer) throws IOException {
            int charCount = 0;
            int dLimit = data.length / 3 * 3;

            int d;
            for(d = 0; d != dLimit; d += 3) {
               int dx = (data[d] & 255) << 16 | (data[d + 1] & 255) << 8 | data[d + 2] & 255;
               writer.write(EncodingGroovyMethods.T_TABLE[dx >> 18]);
               writer.write(EncodingGroovyMethods.T_TABLE[dx >> 12 & 63]);
               writer.write(EncodingGroovyMethods.T_TABLE[dx >> 6 & 63]);
               writer.write(EncodingGroovyMethods.T_TABLE[dx & 63]);
               if (chunked) {
                  ++charCount;
                  if (charCount == 19) {
                     writer.write("\r\n");
                     charCount = 0;
                  }
               }
            }

            if (dLimit != data.length) {
               d = (data[dLimit] & 255) << 16;
               if (dLimit + 1 != data.length) {
                  d |= (data[dLimit + 1] & 255) << 8;
               }

               writer.write(EncodingGroovyMethods.T_TABLE[d >> 18]);
               writer.write(EncodingGroovyMethods.T_TABLE[d >> 12 & 63]);
               writer.write(dLimit + 1 < data.length ? EncodingGroovyMethods.T_TABLE[d >> 6 & 63] : 61);
               writer.write(61);
               if (chunked && charCount != 0) {
                  writer.write("\r\n");
               }
            }

            return writer;
         }

         public String toString() {
            StringWriter buffer = new StringWriter();

            try {
               this.writeTo(buffer);
            } catch (IOException var3) {
               throw new StringWriterIOException(var3);
            }

            return buffer.toString();
         }
      };
   }

   public static Writable encodeBase64(byte[] data) {
      return encodeBase64(data, false);
   }

   public static byte[] decodeBase64(String value) {
      int byteShift = 4;
      int tmp = 0;
      boolean done = false;
      StringBuilder buffer = new StringBuilder();

      for(int i = 0; i != value.length(); ++i) {
         char c = value.charAt(i);
         int sixBit = c < '{' ? EncodingGroovyMethodsSupport.TRANSLATE_TABLE[c] : 66;
         if (sixBit < 64) {
            if (done) {
               throw new RuntimeException("= character not at end of base64 value");
            }

            tmp = tmp << 6 | sixBit;
            if (byteShift-- != 4) {
               buffer.append((char)(tmp >> byteShift * 2 & 255));
            }
         } else if (sixBit == 64) {
            --byteShift;
            done = true;
         } else if (sixBit == 66) {
            throw new RuntimeException("bad character in base64 value");
         }

         if (byteShift == 0) {
            byteShift = 4;
         }
      }

      try {
         return buffer.toString().getBytes("ISO-8859-1");
      } catch (UnsupportedEncodingException var8) {
         throw new RuntimeException("Base 64 decode produced byte values > 255");
      }
   }
}
