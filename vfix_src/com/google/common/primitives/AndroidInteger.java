package com.google.common.primitives;

import com.google.common.base.Preconditions;
import javax.annotation.CheckForNull;

final class AndroidInteger {
   @CheckForNull
   static Integer tryParse(String string) {
      return tryParse(string, 10);
   }

   @CheckForNull
   static Integer tryParse(String string, int radix) {
      Preconditions.checkNotNull(string);
      Preconditions.checkArgument(radix >= 2, "Invalid radix %s, min radix is %s", radix, 2);
      Preconditions.checkArgument(radix <= 36, "Invalid radix %s, max radix is %s", radix, 36);
      int length = string.length();
      int i = 0;
      if (length == 0) {
         return null;
      } else {
         boolean negative = string.charAt(i) == '-';
         if (negative) {
            ++i;
            if (i == length) {
               return null;
            }
         }

         return tryParse(string, i, radix, negative);
      }
   }

   @CheckForNull
   private static Integer tryParse(String string, int offset, int radix, boolean negative) {
      int max = Integer.MIN_VALUE / radix;
      int result = 0;

      int next;
      for(int length = string.length(); offset < length; result = next) {
         int digit = Character.digit(string.charAt(offset++), radix);
         if (digit == -1) {
            return null;
         }

         if (max > result) {
            return null;
         }

         next = result * radix - digit;
         if (next > result) {
            return null;
         }
      }

      if (!negative) {
         result = -result;
         if (result < 0) {
            return null;
         }
      }

      if (result <= Integer.MAX_VALUE && result >= Integer.MIN_VALUE) {
         return result;
      } else {
         return null;
      }
   }

   private AndroidInteger() {
   }
}
