package com.google.common.base;

final class CharMatcher$BreakingWhitespace extends CharMatcher {
   static final CharMatcher INSTANCE = new CharMatcher$BreakingWhitespace();

   private CharMatcher$BreakingWhitespace() {
   }

   public boolean matches(char c) {
      switch(c) {
      case '\t':
      case '\n':
      case '\u000b':
      case '\f':
      case '\r':
      case ' ':
      case '\u0085':
      case ' ':
      case '\u2028':
      case '\u2029':
      case ' ':
      case '　':
         return true;
      case ' ':
         return false;
      default:
         return c >= 8192 && c <= 8202;
      }
   }

   public String toString() {
      return "CharMatcher.breakingWhitespace()";
   }
}
