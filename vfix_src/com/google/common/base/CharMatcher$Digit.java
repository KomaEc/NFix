package com.google.common.base;

final class CharMatcher$Digit extends CharMatcher.RangesMatcher {
   private static final String ZEROES = "0٠۰߀०০੦૦୦௦౦೦൦\u0de6๐໐༠၀႐០᠐᥆᧐᪀᪐᭐᮰᱀᱐꘠꣐꤀꧐\ua9f0꩐꯰０";
   static final CharMatcher$Digit INSTANCE = new CharMatcher$Digit();

   private static char[] zeroes() {
      return "0٠۰߀०০੦૦୦௦౦೦൦\u0de6๐໐༠၀႐០᠐᥆᧐᪀᪐᭐᮰᱀᱐꘠꣐꤀꧐\ua9f0꩐꯰０".toCharArray();
   }

   private static char[] nines() {
      char[] nines = new char["0٠۰߀०০੦૦୦௦౦೦൦\u0de6๐໐༠၀႐០᠐᥆᧐᪀᪐᭐᮰᱀᱐꘠꣐꤀꧐\ua9f0꩐꯰０".length()];

      for(int i = 0; i < "0٠۰߀०০੦૦୦௦౦೦൦\u0de6๐໐༠၀႐០᠐᥆᧐᪀᪐᭐᮰᱀᱐꘠꣐꤀꧐\ua9f0꩐꯰０".length(); ++i) {
         nines[i] = (char)("0٠۰߀०০੦૦୦௦౦೦൦\u0de6๐໐༠၀႐០᠐᥆᧐᪀᪐᭐᮰᱀᱐꘠꣐꤀꧐\ua9f0꩐꯰０".charAt(i) + 9);
      }

      return nines;
   }

   private CharMatcher$Digit() {
      super("CharMatcher.digit()", zeroes(), nines());
   }
}
