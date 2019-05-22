package com.google.common.base;

final class CharMatcher$JavaDigit extends CharMatcher {
   static final CharMatcher$JavaDigit INSTANCE = new CharMatcher$JavaDigit();

   private CharMatcher$JavaDigit() {
   }

   public boolean matches(char c) {
      return Character.isDigit(c);
   }

   public String toString() {
      return "CharMatcher.javaDigit()";
   }
}
