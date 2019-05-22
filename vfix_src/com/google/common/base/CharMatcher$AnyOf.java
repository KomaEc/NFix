package com.google.common.base;

import com.google.common.annotations.GwtIncompatible;
import java.util.Arrays;
import java.util.BitSet;

final class CharMatcher$AnyOf extends CharMatcher {
   private final char[] chars;

   public CharMatcher$AnyOf(CharSequence chars) {
      this.chars = chars.toString().toCharArray();
      Arrays.sort(this.chars);
   }

   public boolean matches(char c) {
      return Arrays.binarySearch(this.chars, c) >= 0;
   }

   @GwtIncompatible
   void setBits(BitSet table) {
      char[] var2 = this.chars;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         char c = var2[var4];
         table.set(c);
      }

   }

   public String toString() {
      StringBuilder description = new StringBuilder("CharMatcher.anyOf(\"");
      char[] var2 = this.chars;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         char c = var2[var4];
         description.append(CharMatcher.access$100(c));
      }

      description.append("\")");
      return description.toString();
   }
}
