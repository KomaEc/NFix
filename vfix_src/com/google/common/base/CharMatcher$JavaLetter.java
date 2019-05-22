package com.google.common.base;

final class CharMatcher$JavaLetter extends CharMatcher {
   static final CharMatcher$JavaLetter INSTANCE = new CharMatcher$JavaLetter();

   private CharMatcher$JavaLetter() {
   }

   public boolean matches(char c) {
      return Character.isLetter(c);
   }

   public String toString() {
      return "CharMatcher.javaLetter()";
   }
}
