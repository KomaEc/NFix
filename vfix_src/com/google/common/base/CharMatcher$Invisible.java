package com.google.common.base;

final class CharMatcher$Invisible extends CharMatcher.RangesMatcher {
   private static final String RANGE_STARTS = "\u0000\u007f\u00ad\u0600\u061c\u06dd\u070f\u08e2 ᠎ \u2028 \u2066　\ud800\ufeff\ufff9";
   private static final String RANGE_ENDS = "  \u00ad\u0605\u061c\u06dd\u070f\u08e2 ᠎\u200f \u2064\u206f　\uf8ff\ufeff\ufffb";
   static final CharMatcher$Invisible INSTANCE = new CharMatcher$Invisible();

   private CharMatcher$Invisible() {
      super("CharMatcher.invisible()", "\u0000\u007f\u00ad\u0600\u061c\u06dd\u070f\u08e2 ᠎ \u2028 \u2066　\ud800\ufeff\ufff9".toCharArray(), "  \u00ad\u0605\u061c\u06dd\u070f\u08e2 ᠎\u200f \u2064\u206f　\uf8ff\ufeff\ufffb".toCharArray());
   }
}
