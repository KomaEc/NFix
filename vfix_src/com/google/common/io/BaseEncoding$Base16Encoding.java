package com.google.common.io;

import com.google.common.base.Preconditions;
import java.io.IOException;
import javax.annotation.Nullable;

final class BaseEncoding$Base16Encoding extends BaseEncoding.StandardBaseEncoding {
   final char[] encoding;

   BaseEncoding$Base16Encoding(String name, String alphabetChars) {
      this(new BaseEncoding.Alphabet(name, alphabetChars.toCharArray()));
   }

   private BaseEncoding$Base16Encoding(BaseEncoding.Alphabet alphabet) {
      super(alphabet, (Character)null);
      this.encoding = new char[512];
      Preconditions.checkArgument(BaseEncoding.Alphabet.access$000(alphabet).length == 16);

      for(int i = 0; i < 256; ++i) {
         this.encoding[i] = alphabet.encode(i >>> 4);
         this.encoding[i | 256] = alphabet.encode(i & 15);
      }

   }

   void encodeTo(Appendable target, byte[] bytes, int off, int len) throws IOException {
      Preconditions.checkNotNull(target);
      Preconditions.checkPositionIndexes(off, off + len, bytes.length);

      for(int i = 0; i < len; ++i) {
         int b = bytes[off + i] & 255;
         target.append(this.encoding[b]);
         target.append(this.encoding[b | 256]);
      }

   }

   int decodeTo(byte[] target, CharSequence chars) throws BaseEncoding.DecodingException {
      Preconditions.checkNotNull(target);
      if (chars.length() % 2 == 1) {
         throw new BaseEncoding.DecodingException("Invalid input length " + chars.length());
      } else {
         int bytesWritten = 0;

         for(int i = 0; i < chars.length(); i += 2) {
            int decoded = this.alphabet.decode(chars.charAt(i)) << 4 | this.alphabet.decode(chars.charAt(i + 1));
            target[bytesWritten++] = (byte)decoded;
         }

         return bytesWritten;
      }
   }

   BaseEncoding newInstance(BaseEncoding.Alphabet alphabet, @Nullable Character paddingChar) {
      return new BaseEncoding$Base16Encoding(alphabet);
   }
}
