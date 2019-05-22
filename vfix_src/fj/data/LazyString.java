package fj.data;

import fj.Equal;
import fj.F;
import fj.F2;
import fj.Function;
import fj.P;
import fj.P1;
import fj.P2;
import fj.function.Booleans;
import fj.function.Characters;
import java.util.regex.Pattern;

public final class LazyString implements CharSequence {
   private final Stream<Character> s;
   public static final LazyString empty = str("");
   public static final F<LazyString, Stream<Character>> toStream = new F<LazyString, Stream<Character>>() {
      public Stream<Character> f(LazyString string) {
         return string.toStream();
      }
   };
   public static final F<LazyString, String> toString = new F<LazyString, String>() {
      public String f(LazyString string) {
         return string.toString();
      }
   };
   public static final F<Stream<Character>, LazyString> fromStream = new F<Stream<Character>, LazyString>() {
      public LazyString f(Stream<Character> s) {
         return LazyString.fromStream(s);
      }
   };
   private static final Equal<Stream<Character>> eqS;

   private LazyString(Stream<Character> s) {
      this.s = s;
   }

   public static LazyString str(String s) {
      return new LazyString(Stream.unfold(new F<P2<String, Integer>, Option<P2<Character, P2<String, Integer>>>>() {
         public Option<P2<Character, P2<String, Integer>>> f(P2<String, Integer> o) {
            String s = (String)o._1();
            int n = (Integer)o._2();
            Option<P2<Character, P2<String, Integer>>> none = Option.none();
            return s.length() <= n ? none : Option.some(P.p(s.charAt(n), P.p(s, n + 1)));
         }
      }, P.p(s, 0)));
   }

   public static LazyString fromStream(Stream<Character> s) {
      return new LazyString(s);
   }

   public Stream<Character> toStream() {
      return this.s;
   }

   public int length() {
      return this.s.length();
   }

   public char charAt(int index) {
      return (Character)this.s.index(index);
   }

   public CharSequence subSequence(int start, int end) {
      return fromStream(this.s.drop(start).take(end - start));
   }

   public String toString() {
      return (new StringBuilder(this)).toString();
   }

   public LazyString append(LazyString cs) {
      return fromStream(this.s.append(cs.s));
   }

   public LazyString append(String s) {
      return this.append(str(s));
   }

   public boolean contains(LazyString cs) {
      return Booleans.or(this.s.tails().map(Function.compose((F)startsWith().f(cs), fromStream)));
   }

   public boolean endsWith(LazyString cs) {
      return this.reverse().startsWith(cs.reverse());
   }

   public boolean startsWith(LazyString cs) {
      return cs.isEmpty() || !this.isEmpty() && Equal.charEqual.eq(this.head(), cs.head()) && this.tail().startsWith(cs.tail());
   }

   public static F<LazyString, F<LazyString, Boolean>> startsWith() {
      return Function.curry(new F2<LazyString, LazyString, Boolean>() {
         public Boolean f(LazyString needle, LazyString haystack) {
            return haystack.startsWith(needle);
         }
      });
   }

   public char head() {
      return (Character)this.s.head();
   }

   public LazyString tail() {
      return fromStream((Stream)this.s.tail()._1());
   }

   public boolean isEmpty() {
      return this.s.isEmpty();
   }

   public LazyString reverse() {
      return fromStream(this.s.reverse());
   }

   public Option<Integer> indexOf(char c) {
      return this.s.indexOf(Equal.charEqual.eq(c));
   }

   public Option<Integer> indexOf(LazyString cs) {
      return this.s.substreams().indexOf(eqS.eq(cs.s));
   }

   public boolean matches(String regex) {
      return Pattern.matches(regex, this);
   }

   public Stream<LazyString> split(final F<Character, Boolean> p) {
      Stream<Character> findIt = this.s.dropWhile(p);
      final P2<Stream<Character>, Stream<Character>> ws = findIt.split(p);
      return findIt.isEmpty() ? Stream.nil() : Stream.cons(fromStream((Stream)ws._1()), new P1<Stream<LazyString>>() {
         public Stream<LazyString> _1() {
            return LazyString.fromStream((Stream)ws._2()).split(p);
         }
      });
   }

   public Stream<LazyString> split(char c) {
      return this.split(Equal.charEqual.eq(c));
   }

   public Stream<LazyString> words() {
      return this.split(Characters.isSpaceChar);
   }

   public Stream<LazyString> lines() {
      return this.split('\n');
   }

   public static LazyString unlines(Stream<LazyString> str) {
      return fromStream(Stream.join(str.intersperse(str("\n")).map(toStream)));
   }

   public static LazyString unwords(Stream<LazyString> str) {
      return fromStream(Stream.join(str.intersperse(str(" ")).map(toStream)));
   }

   static {
      eqS = Equal.streamEqual(Equal.charEqual);
   }
}
