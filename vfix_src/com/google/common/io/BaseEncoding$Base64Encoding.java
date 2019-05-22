package com.google.common.io;

import com.google.common.base.Preconditions;
import java.io.IOException;
import javax.annotation.Nullable;

final class BaseEncoding$Base64Encoding extends BaseEncoding.StandardBaseEncoding {
   BaseEncoding$Base64Encoding(String name, String alphabetChars, @Nullable Character paddingChar) {
      this(new BaseEncoding.Alphabet(name, alphabetChars.toCharArray()), paddingChar);
   }

   private BaseEncoding$Base64Encoding(BaseEncoding.Alphabet alphabet, @Nullable Character paddingChar) {
      super(alphabet, paddingChar);
      Preconditions.checkArgument(BaseEncoding.Alphabet.access$000(alphabet).length == 64);
   }

   void encodeTo(Appendable target, byte[] bytes, int off, int len) throws IOException {
      Preconditions.checkNotNull(target);
      Preconditions.checkPositionIndexes(off, off + len, bytes.length);
      int i = off;

      for(int remaining = len; remaining >= 3; remaining -= 3) {
         int chunk = (bytes[i++] & 255) << 16 | (bytes[i++] & 255) << 8 | bytes[i++] & 255;
         target.append(this.alphabet.encode(chunk >>> 18));
         target.append(this.alphabet.encode(chunk >>> 12 & 63));
         target.append(this.alphabet.encode(chunk >>> 6 & 63));
         target.append(this.alphabet.encode(chunk & 63));
      }

      if (i < off + len) {
         this.encodeChunkTo(target, bytes, i, off + len - i);
      }

   }

   int decodeTo(byte[] target, CharSequence chars) throws BaseEncoding.DecodingException {
      Preconditions.checkNotNull(target);
      chars = this.trimTrailingPadding(chars);
      if (!this.alphabet.isValidPaddingStartPosition(chars.length())) {
         throw new BaseEncoding.DecodingException("Invalid input length " + chars.length());
      } else {
         int bytesWritten = 0;
         int i = 0;

         while(i < chars.length()) {
            int chunk = this.alphabet.decode(chars.charAt(i++)) << 18;
            chunk |= this.alphabet.decode(chars.charAt(i++)) << 12;
            target[bytesWritten++] = (byte)(chunk >>> 16);
            if (i < chars.length()) {
               chunk |= this.alphabet.decode(chars.charAt(i++)) << 6;
               target[bytesWritten++] = (byte)(chunk >>> 8 & 255);
               if (i < chars.length()) {
                  chunk |= this.alphabet.decode(chars.charAt(i++));
                  target[bytesWritten++] = (byte)(chunk & 255);
               }
            }
         }

         return bytesWritten;
      }
   }

   BaseEncoding newInstance(BaseEncoding.Alphabet alphabet, @Nullable Character paddingChar) {
      return new BaseEncoding$Base64Encoding(alphabet, paddingChar);
   }
}
