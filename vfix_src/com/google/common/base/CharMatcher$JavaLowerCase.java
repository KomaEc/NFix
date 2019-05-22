package com.google.common.base;

final class CharMatcher$JavaLowerCase extends CharMatcher {
   static final CharMatcher$JavaLowerCase INSTANCE = new CharMatcher$JavaLowerCase();

   private CharMatcher$JavaLowerCase() {
   }

   public boolean matches(char c) {
      return Character.isLowerCase(c);
   }

   public String toString() {
      return "CharMatcher.javaLowerCase()";
   }
}
