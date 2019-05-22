package com.google.common.base;

import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.VisibleForTesting;
import java.util.BitSet;

@VisibleForTesting
final class CharMatcher$Whitespace extends CharMatcher$NamedFastMatcher {
   static final String TABLE = " 　\r\u0085   　\u2029\u000b　   　 \t     \f 　 　　\u2028\n 　";
   static final int MULTIPLIER = 1682554634;
   static final int SHIFT = Integer.numberOfLeadingZeros(" 　\r\u0085   　\u2029\u000b　   　 \t     \f 　 　　\u2028\n 　".length() - 1);
   static final CharMatcher$Whitespace INSTANCE = new CharMatcher$Whitespace();

   CharMatcher$Whitespace() {
      super("CharMatcher.whitespace()");
   }

   public boolean matches(char c) {
      return " 　\r\u0085   　\u2029\u000b　   　 \t     \f 　 　　\u2028\n 　".charAt(1682554634 * c >>> SHIFT) == c;
   }

   @GwtIncompatible
   void setBits(BitSet table) {
      for(int i = 0; i < " 　\r\u0085   　\u2029\u000b　   　 \t     \f 　 　　\u2028\n 　".length(); ++i) {
         table.set(" 　\r\u0085   　\u2029\u000b　   　 \t     \f 　 　　\u2028\n 　".charAt(i));
      }

   }
}
