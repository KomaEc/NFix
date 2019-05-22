package fj;

import fj.data.Option;

public enum Digit {
   _0,
   _1,
   _2,
   _3,
   _4,
   _5,
   _6,
   _7,
   _8,
   _9;

   public static final F<Digit, Long> toLong = new F<Digit, Long>() {
      public Long f(Digit d) {
         return d.toLong();
      }
   };
   public static final F<Long, Digit> fromLong = new F<Long, Digit>() {
      public Digit f(Long i) {
         return Digit.fromLong(i);
      }
   };
   public static final F<Digit, Character> toChar = new F<Digit, Character>() {
      public Character f(Digit d) {
         return d.toChar();
      }
   };
   public static final F<Character, Option<Digit>> fromChar = new F<Character, Option<Digit>>() {
      public Option<Digit> f(Character c) {
         return Digit.fromChar(c);
      }
   };

   public long toLong() {
      switch(this) {
      case _0:
         return 0L;
      case _1:
         return 1L;
      case _2:
         return 2L;
      case _3:
         return 3L;
      case _4:
         return 4L;
      case _5:
         return 5L;
      case _6:
         return 6L;
      case _7:
         return 7L;
      case _8:
         return 8L;
      default:
         return 9L;
      }
   }

   public char toChar() {
      switch(this) {
      case _0:
         return '0';
      case _1:
         return '1';
      case _2:
         return '2';
      case _3:
         return '3';
      case _4:
         return '4';
      case _5:
         return '5';
      case _6:
         return '6';
      case _7:
         return '7';
      case _8:
         return '8';
      default:
         return '9';
      }
   }

   public static Digit fromLong(long i) {
      long x = Math.abs(i) % 10L;
      return x == 0L ? _0 : (x == 1L ? _1 : (x == 2L ? _2 : (x == 3L ? _3 : (x == 4L ? _4 : (x == 5L ? _5 : (x == 6L ? _6 : (x == 7L ? _7 : (x == 8L ? _8 : _9))))))));
   }

   public static Option<Digit> fromChar(char c) {
      switch(c) {
      case '0':
         return Option.some(_0);
      case '1':
         return Option.some(_1);
      case '2':
         return Option.some(_2);
      case '3':
         return Option.some(_3);
      case '4':
         return Option.some(_4);
      case '5':
         return Option.some(_5);
      case '6':
         return Option.some(_6);
      case '7':
         return Option.some(_7);
      case '8':
         return Option.some(_8);
      case '9':
         return Option.some(_9);
      default:
         return Option.none();
      }
   }
}
