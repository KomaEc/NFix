package fj.parser;

import fj.Digit;
import fj.F;
import fj.P;
import fj.P1;
import fj.Semigroup;
import fj.Unit;
import fj.data.List;
import fj.data.Stream;
import fj.data.Validation;

public final class Parser<I, A, E> {
   private final F<I, Validation<E, Result<I, A>>> f;

   private Parser(F<I, Validation<E, Result<I, A>>> f) {
      this.f = f;
   }

   public Validation<E, Result<I, A>> parse(I i) {
      return (Validation)this.f.f(i);
   }

   public <Z> Parser<Z, A, E> xmap(final F<I, Z> f, final F<Z, I> g) {
      return parser(new F<Z, Validation<E, Result<Z, A>>>() {
         public Validation<E, Result<Z, A>> f(Z z) {
            return Parser.this.parse(g.f(z)).map(new F<Result<I, A>, Result<Z, A>>() {
               public Result<Z, A> f(Result<I, A> r) {
                  return r.mapRest(f);
               }
            });
         }
      });
   }

   public <B> Parser<I, B, E> map(final F<A, B> f) {
      return parser(new F<I, Validation<E, Result<I, B>>>() {
         public Validation<E, Result<I, B>> f(I i) {
            return Parser.this.parse(i).map(new F<Result<I, A>, Result<I, B>>() {
               public Result<I, B> f(Result<I, A> r) {
                  return r.mapValue(f);
               }
            });
         }
      });
   }

   public Parser<I, A, E> filter(final F<A, Boolean> f, final E e) {
      return parser(new F<I, Validation<E, Result<I, A>>>() {
         public Validation<E, Result<I, A>> f(I i) {
            return Parser.this.parse(i).bind(new F<Result<I, A>, Validation<E, Result<I, A>>>() {
               public Validation<E, Result<I, A>> f(Result<I, A> r) {
                  A v = r.value();
                  return (Boolean)f.f(v) ? Validation.success(Result.result(r.rest(), v)) : Validation.fail(e);
               }
            });
         }
      });
   }

   public <B> Parser<I, B, E> bind(final F<A, Parser<I, B, E>> f) {
      return parser(new F<I, Validation<E, Result<I, B>>>() {
         public Validation<E, Result<I, B>> f(I i) {
            return Parser.this.parse(i).bind(new F<Result<I, A>, Validation<E, Result<I, B>>>() {
               public Validation<E, Result<I, B>> f(Result<I, A> r) {
                  return ((Parser)f.f(r.value())).parse(r.rest());
               }
            });
         }
      });
   }

   public <B, C> Parser<I, C, E> bind(Parser<I, B, E> pb, F<A, F<B, C>> f) {
      return pb.apply(this.map(f));
   }

   public <B, C, D> Parser<I, D, E> bind(Parser<I, B, E> pb, Parser<I, C, E> pc, F<A, F<B, F<C, D>>> f) {
      return pc.apply(this.bind(pb, f));
   }

   public <B, C, D, E$> Parser<I, E$, E> bind(Parser<I, B, E> pb, Parser<I, C, E> pc, Parser<I, D, E> pd, F<A, F<B, F<C, F<D, E$>>>> f) {
      return pd.apply(this.bind(pb, pc, f));
   }

   public <B, C, D, E$, F$> Parser<I, F$, E> bind(Parser<I, B, E> pb, Parser<I, C, E> pc, Parser<I, D, E> pd, Parser<I, E$, E> pe, F<A, F<B, F<C, F<D, F<E$, F$>>>>> f) {
      return pe.apply(this.bind(pb, pc, pd, f));
   }

   public <B, C, D, E$, F$, G> Parser<I, G, E> bind(Parser<I, B, E> pb, Parser<I, C, E> pc, Parser<I, D, E> pd, Parser<I, E$, E> pe, Parser<I, F$, E> pf, F<A, F<B, F<C, F<D, F<E$, F<F$, G>>>>>> f) {
      return pf.apply(this.bind(pb, pc, pd, pe, f));
   }

   public <B, C, D, E$, F$, G, H> Parser<I, H, E> bind(Parser<I, B, E> pb, Parser<I, C, E> pc, Parser<I, D, E> pd, Parser<I, E$, E> pe, Parser<I, F$, E> pf, Parser<I, G, E> pg, F<A, F<B, F<C, F<D, F<E$, F<F$, F<G, H>>>>>>> f) {
      return pg.apply(this.bind(pb, pc, pd, pe, pf, f));
   }

   public <B, C, D, E$, F$, G, H, I$> Parser<I, I$, E> bind(Parser<I, B, E> pb, Parser<I, C, E> pc, Parser<I, D, E> pd, Parser<I, E$, E> pe, Parser<I, F$, E> pf, Parser<I, G, E> pg, Parser<I, H, E> ph, F<A, F<B, F<C, F<D, F<E$, F<F$, F<G, F<H, I$>>>>>>>> f) {
      return ph.apply(this.bind(pb, pc, pd, pe, pf, pg, f));
   }

   public <B> Parser<I, B, E> sequence(final Parser<I, B, E> p) {
      return this.bind(new F<A, Parser<I, B, E>>() {
         public Parser<I, B, E> f(A a) {
            return p;
         }
      });
   }

   public <B> Parser<I, B, E> apply(Parser<I, F<A, B>, E> p) {
      return p.bind(new F<F<A, B>, Parser<I, B, E>>() {
         public Parser<I, B, E> f(F<A, B> f) {
            return Parser.this.map(f);
         }
      });
   }

   public Parser<I, A, E> or(final P1<Parser<I, A, E>> alt) {
      return parser(new F<I, Validation<E, Result<I, A>>>() {
         public Validation<E, Result<I, A>> f(I i) {
            return Parser.this.parse(i).f().sequence(((Parser)alt._1()).parse(i));
         }
      });
   }

   public Parser<I, A, E> or(Parser<I, A, E> alt) {
      return this.or(P.p(alt));
   }

   public Parser<I, A, E> or(final P1<Parser<I, A, E>> alt, final Semigroup<E> s) {
      return parser(new F<I, Validation<E, Result<I, A>>>() {
         public Validation<E, Result<I, A>> f(final I i) {
            return Parser.this.parse(i).f().bind(new F<E, Validation<E, Result<I, A>>>() {
               public Validation<E, Result<I, A>> f(E e) {
                  return ((Parser)alt._1()).parse(i).f().map(s.sum(e));
               }
            });
         }
      });
   }

   public Parser<I, A, E> or(Parser<I, A, E> alt, Semigroup<E> s) {
      return this.or(P.p(alt), s);
   }

   public Parser<I, Unit, E> not(final P1<E> e) {
      return parser(new F<I, Validation<E, Result<I, Unit>>>() {
         public Validation<E, Result<I, Unit>> f(I i) {
            return Parser.this.parse(i).isFail() ? Validation.success(Result.result(i, Unit.unit())) : Validation.fail(e._1());
         }
      });
   }

   public Parser<I, Unit, E> not(E e) {
      return this.not(P.p(e));
   }

   public Parser<I, Stream<A>, E> repeat() {
      return this.repeat1().or(new P1<Parser<I, Stream<A>, E>>() {
         public Parser<I, Stream<A>, E> _1() {
            return Parser.value(Stream.nil());
         }
      });
   }

   public Parser<I, Stream<A>, E> repeat1() {
      return this.bind(new F<A, Parser<I, Stream<A>, E>>() {
         public Parser<I, Stream<A>, E> f(final A a) {
            return Parser.this.repeat().map(new F<Stream<A>, Stream<A>>() {
               public Stream<A> f(Stream<A> as) {
                  return as.cons(a);
               }
            });
         }
      });
   }

   public <K> Parser<I, A, K> mapError(final F<E, K> f) {
      return parser(new F<I, Validation<K, Result<I, A>>>() {
         public Validation<K, Result<I, A>> f(I i) {
            return ((Validation)Parser.this.f.f(i)).f().map(f);
         }
      });
   }

   public static <I, A, E> Parser<I, A, E> parser(F<I, Validation<E, Result<I, A>>> f) {
      return new Parser(f);
   }

   public static <I, A, E> Parser<I, A, E> value(final A a) {
      return parser(new F<I, Validation<E, Result<I, A>>>() {
         public Validation<E, Result<I, A>> f(I i) {
            return Validation.success(Result.result(i, a));
         }
      });
   }

   public static <I, A, E> Parser<I, A, E> fail(final E e) {
      return parser(new F<I, Validation<E, Result<I, A>>>() {
         public Validation<E, Result<I, A>> f(I i) {
            return Validation.fail(e);
         }
      });
   }

   public static <I, A, E> Parser<I, List<A>, E> sequence(final List<Parser<I, A, E>> ps) {
      return ps.isEmpty() ? value(List.nil()) : ((Parser)ps.head()).bind(new F<A, Parser<I, List<A>, E>>() {
         public Parser<I, List<A>, E> f(A a) {
            return Parser.sequence(ps.tail()).map(List.cons_(a));
         }
      });
   }

   public static final class CharsParser {
      private CharsParser() {
      }

      public static <E> Parser<Stream<Character>, Character, E> character(P1<E> e) {
         return Parser.StreamParser.element(e);
      }

      public static <E> Parser<Stream<Character>, Character, E> character(E e) {
         return character(P.p(e));
      }

      public static <E> Parser<Stream<Character>, Character, E> character(P1<E> missing, F<Character, E> sat, final char c) {
         return Parser.StreamParser.satisfy(missing, sat, new F<Character, Boolean>() {
            public Boolean f(Character x) {
               return x == c;
            }
         });
      }

      public static <E> Parser<Stream<Character>, Character, E> character(E missing, F<Character, E> sat, char c) {
         return character(P.p(missing), sat, c);
      }

      public static <E> Parser<Stream<Character>, Stream<Character>, E> characters(P1<E> missing, int n) {
         return n <= 0 ? Parser.value(Stream.nil()) : character(missing).bind(characters(missing, n - 1), Stream.cons_());
      }

      public static <E> Parser<Stream<Character>, Stream<Character>, E> characters(E missing, int n) {
         return characters(P.p(missing), n);
      }

      public static <E> Parser<Stream<Character>, Stream<Character>, E> characters(P1<E> missing, F<Character, E> sat, Stream<Character> cs) {
         return cs.isEmpty() ? Parser.value(Stream.nil()) : character(missing, sat, (Character)cs.head()).bind(characters(missing, sat, (Stream)cs.tail()._1()), Stream.cons_());
      }

      public static <E> Parser<Stream<Character>, Stream<Character>, E> characters(E missing, F<Character, E> sat, Stream<Character> cs) {
         return characters(P.p(missing), sat, cs);
      }

      public static <E> Parser<Stream<Character>, String, E> string(P1<E> missing, F<Character, E> sat, String s) {
         return characters(missing, sat, List.fromString(s).toStream()).map(new F<Stream<Character>, String>() {
            public String f(Stream<Character> cs) {
               return List.asString(cs.toList());
            }
         });
      }

      public static <E> Parser<Stream<Character>, String, E> string(E missing, F<Character, E> sat, String s) {
         return string(P.p(missing), sat, s);
      }

      public static <E> Parser<Stream<Character>, Digit, E> digit(P1<E> missing, F<Character, E> sat) {
         return Parser.StreamParser.satisfy(missing, sat, new F<Character, Boolean>() {
            public Boolean f(Character c) {
               return Character.isDigit(c);
            }
         }).map(new F<Character, Digit>() {
            public Digit f(Character c) {
               return (Digit)Digit.fromChar(c).some();
            }
         });
      }

      public static <E> Parser<Stream<Character>, Digit, E> digit(E missing, F<Character, E> sat) {
         return digit(P.p(missing), sat);
      }

      public static <E> Parser<Stream<Character>, Character, E> lower(P1<E> missing, F<Character, E> sat) {
         return Parser.StreamParser.satisfy(missing, sat, new F<Character, Boolean>() {
            public Boolean f(Character c) {
               return Character.isLowerCase(c);
            }
         });
      }

      public static <E> Parser<Stream<Character>, Character, E> lower(E missing, F<Character, E> sat) {
         return lower(P.p(missing), sat);
      }

      public static <E> Parser<Stream<Character>, Character, E> upper(P1<E> missing, F<Character, E> sat) {
         return Parser.StreamParser.satisfy(missing, sat, new F<Character, Boolean>() {
            public Boolean f(Character c) {
               return Character.isUpperCase(c);
            }
         });
      }

      public static <E> Parser<Stream<Character>, Character, E> upper(E missing, F<Character, E> sat) {
         return upper(P.p(missing), sat);
      }

      public static <E> Parser<Stream<Character>, Character, E> defined(P1<E> missing, F<Character, E> sat) {
         return Parser.StreamParser.satisfy(missing, sat, new F<Character, Boolean>() {
            public Boolean f(Character c) {
               return Character.isDefined(c);
            }
         });
      }

      public static <E> Parser<Stream<Character>, Character, E> defined(E missing, F<Character, E> sat) {
         return defined(P.p(missing), sat);
      }

      public static <E> Parser<Stream<Character>, Character, E> highSurrogate(P1<E> missing, F<Character, E> sat) {
         return Parser.StreamParser.satisfy(missing, sat, new F<Character, Boolean>() {
            public Boolean f(Character c) {
               return Character.isHighSurrogate(c);
            }
         });
      }

      public static <E> Parser<Stream<Character>, Character, E> highSurrogate(E missing, F<Character, E> sat) {
         return highSurrogate(P.p(missing), sat);
      }

      public static <E> Parser<Stream<Character>, Character, E> identifierIgnorable(P1<E> missing, F<Character, E> sat) {
         return Parser.StreamParser.satisfy(missing, sat, new F<Character, Boolean>() {
            public Boolean f(Character c) {
               return Character.isIdentifierIgnorable(c);
            }
         });
      }

      public static <E> Parser<Stream<Character>, Character, E> identifierIgnorable(E missing, F<Character, E> sat) {
         return identifierIgnorable(P.p(missing), sat);
      }

      public static <E> Parser<Stream<Character>, Character, E> isoControl(P1<E> missing, F<Character, E> sat) {
         return Parser.StreamParser.satisfy(missing, sat, new F<Character, Boolean>() {
            public Boolean f(Character c) {
               return Character.isISOControl(c);
            }
         });
      }

      public static <E> Parser<Stream<Character>, Character, E> isoControl(E missing, F<Character, E> sat) {
         return isoControl(P.p(missing), sat);
      }

      public static <E> Parser<Stream<Character>, Character, E> javaIdentifierPart(P1<E> missing, F<Character, E> sat) {
         return Parser.StreamParser.satisfy(missing, sat, new F<Character, Boolean>() {
            public Boolean f(Character c) {
               return Character.isJavaIdentifierPart(c);
            }
         });
      }

      public static <E> Parser<Stream<Character>, Character, E> javaIdentifierPart(E missing, F<Character, E> sat) {
         return javaIdentifierPart(P.p(missing), sat);
      }

      public static <E> Parser<Stream<Character>, Character, E> javaIdentifierStart(P1<E> missing, F<Character, E> sat) {
         return Parser.StreamParser.satisfy(missing, sat, new F<Character, Boolean>() {
            public Boolean f(Character c) {
               return Character.isJavaIdentifierStart(c);
            }
         });
      }

      public static <E> Parser<Stream<Character>, Character, E> javaIdentifierStart(E missing, F<Character, E> sat) {
         return javaIdentifierStart(P.p(missing), sat);
      }

      public static <E> Parser<Stream<Character>, Character, E> alpha(P1<E> missing, F<Character, E> sat) {
         return Parser.StreamParser.satisfy(missing, sat, new F<Character, Boolean>() {
            public Boolean f(Character c) {
               return Character.isLetter(c);
            }
         });
      }

      public static <E> Parser<Stream<Character>, Character, E> alpha(E missing, F<Character, E> sat) {
         return alpha(P.p(missing), sat);
      }

      public static <E> Parser<Stream<Character>, Character, E> alphaNum(P1<E> missing, F<Character, E> sat) {
         return Parser.StreamParser.satisfy(missing, sat, new F<Character, Boolean>() {
            public Boolean f(Character c) {
               return Character.isLetterOrDigit(c);
            }
         });
      }

      public static <E> Parser<Stream<Character>, Character, E> alphaNum(E missing, F<Character, E> sat) {
         return alphaNum(P.p(missing), sat);
      }

      public static <E> Parser<Stream<Character>, Character, E> lowSurrogate(P1<E> missing, F<Character, E> sat) {
         return Parser.StreamParser.satisfy(missing, sat, new F<Character, Boolean>() {
            public Boolean f(Character c) {
               return Character.isLowSurrogate(c);
            }
         });
      }

      public static <E> Parser<Stream<Character>, Character, E> lowSurrogate(E missing, F<Character, E> sat) {
         return lowSurrogate(P.p(missing), sat);
      }

      public static <E> Parser<Stream<Character>, Character, E> mirrored(P1<E> missing, F<Character, E> sat) {
         return Parser.StreamParser.satisfy(missing, sat, new F<Character, Boolean>() {
            public Boolean f(Character c) {
               return Character.isMirrored(c);
            }
         });
      }

      public static <E> Parser<Stream<Character>, Character, E> mirrored(E missing, F<Character, E> sat) {
         return mirrored(P.p(missing), sat);
      }

      public static <E> Parser<Stream<Character>, Character, E> space(P1<E> missing, F<Character, E> sat) {
         return Parser.StreamParser.satisfy(missing, sat, new F<Character, Boolean>() {
            public Boolean f(Character c) {
               return Character.isSpaceChar(c);
            }
         });
      }

      public static <E> Parser<Stream<Character>, Character, E> space(E missing, F<Character, E> sat) {
         return space(P.p(missing), sat);
      }

      public static <E> Parser<Stream<Character>, Character, E> titleCase(P1<E> missing, F<Character, E> sat) {
         return Parser.StreamParser.satisfy(missing, sat, new F<Character, Boolean>() {
            public Boolean f(Character c) {
               return Character.isTitleCase(c);
            }
         });
      }

      public static <E> Parser<Stream<Character>, Character, E> titleCase(E missing, F<Character, E> sat) {
         return titleCase(P.p(missing), sat);
      }

      public static <E> Parser<Stream<Character>, Character, E> unicodeIdentiferPart(P1<E> missing, F<Character, E> sat) {
         return Parser.StreamParser.satisfy(missing, sat, new F<Character, Boolean>() {
            public Boolean f(Character c) {
               return Character.isUnicodeIdentifierPart(c);
            }
         });
      }

      public static <E> Parser<Stream<Character>, Character, E> unicodeIdentiferPart(E missing, F<Character, E> sat) {
         return unicodeIdentiferPart(P.p(missing), sat);
      }

      public static <E> Parser<Stream<Character>, Character, E> unicodeIdentiferStart(P1<E> missing, F<Character, E> sat) {
         return Parser.StreamParser.satisfy(missing, sat, new F<Character, Boolean>() {
            public Boolean f(Character c) {
               return Character.isUnicodeIdentifierStart(c);
            }
         });
      }

      public static <E> Parser<Stream<Character>, Character, E> unicodeIdentiferStart(E missing, F<Character, E> sat) {
         return unicodeIdentiferStart(P.p(missing), sat);
      }

      public static <E> Parser<Stream<Character>, Character, E> whitespace(P1<E> missing, F<Character, E> sat) {
         return Parser.StreamParser.satisfy(missing, sat, new F<Character, Boolean>() {
            public Boolean f(Character c) {
               return Character.isWhitespace(c);
            }
         });
      }

      public static <E> Parser<Stream<Character>, Character, E> whitespace(E missing, F<Character, E> sat) {
         return whitespace(P.p(missing), sat);
      }
   }

   public static final class StreamParser {
      private StreamParser() {
      }

      public static <I, E> Parser<Stream<I>, I, E> element(final P1<E> e) {
         return Parser.parser(new F<Stream<I>, Validation<E, Result<Stream<I>, I>>>() {
            public Validation<E, Result<Stream<I>, I>> f(Stream<I> is) {
               return is.isEmpty() ? Validation.fail(e._1()) : Validation.success(Result.result(is.tail()._1(), is.head()));
            }
         });
      }

      public static <I, E> Parser<Stream<I>, I, E> element(E e) {
         return element(P.p(e));
      }

      public static <I, E> Parser<Stream<I>, I, E> satisfy(P1<E> missing, final F<I, E> sat, final F<I, Boolean> f) {
         return element(missing).bind(new F<I, Parser<Stream<I>, I, E>>() {
            public Parser<Stream<I>, I, E> f(I x) {
               return (Boolean)f.f(x) ? Parser.value(x) : Parser.fail(sat.f(x));
            }
         });
      }

      public static <I, E> Parser<Stream<I>, I, E> satisfy(E missing, F<I, E> sat, F<I, Boolean> f) {
         return satisfy(P.p(missing), sat, f);
      }
   }
}
