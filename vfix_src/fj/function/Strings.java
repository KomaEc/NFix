package fj.function;

import fj.F;
import fj.F2;
import fj.Function;
import fj.data.Stream;

public final class Strings {
   public static final F<String, Boolean> isNotNullOrBlank = new F<String, Boolean>() {
      public Boolean f(String a) {
         return (Boolean)Strings.isNotNullOrEmpty.f(a) && Stream.fromString(a).find(Booleans.not(Characters.isWhitespace)).isSome();
      }
   };
   public static final F<String, Boolean> isNullOrBlank = new F<String, Boolean>() {
      public Boolean f(String a) {
         return (Boolean)Strings.isNullOrEmpty.f(a) || Stream.fromString(a).find(Booleans.not(Characters.isWhitespace)).isNone();
      }
   };
   public static final F<String, Boolean> isNotNullOrEmpty = new F<String, Boolean>() {
      public Boolean f(String a) {
         return a != null && a.length() > 0;
      }
   };
   public static final F<String, Boolean> isNullOrEmpty = new F<String, Boolean>() {
      public Boolean f(String a) {
         return a == null || a.length() == 0;
      }
   };
   public static final F<String, Boolean> isEmpty = new F<String, Boolean>() {
      public Boolean f(String s) {
         return s.length() == 0;
      }
   };
   public static final F<String, Integer> length = new F<String, Integer>() {
      public Integer f(String s) {
         return s.length();
      }
   };
   public static final F<String, F<String, Boolean>> contains = Function.curry(new F2<String, String, Boolean>() {
      public Boolean f(String s1, String s2) {
         return s2.contains(s1);
      }
   });
   public static final F<String, F<String, Boolean>> matches = Function.curry(new F2<String, String, Boolean>() {
      public Boolean f(String s1, String s2) {
         return s2.matches(s1);
      }
   });

   private Strings() {
      throw new UnsupportedOperationException();
   }
}
