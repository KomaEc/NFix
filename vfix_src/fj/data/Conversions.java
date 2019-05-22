package fj.data;

import fj.Effect;
import fj.F;
import fj.P1;
import fj.Try;
import fj.TryEffect;
import fj.Unit;
import fj.function.Effect0;
import fj.function.Effect1;
import fj.function.Try0;
import fj.function.Try1;
import fj.function.TryEffect0;
import java.io.IOException;

public final class Conversions {
   public static final F<List<Character>, String> List_String = new F<List<Character>, String>() {
      public String f(List<Character> cs) {
         return List.asString(cs);
      }
   };
   public static final F<List<Character>, StringBuffer> List_StringBuffer = new F<List<Character>, StringBuffer>() {
      public StringBuffer f(List<Character> cs) {
         return new StringBuffer(List.asString(cs));
      }
   };
   public static final F<List<Character>, StringBuilder> List_StringBuilder = new F<List<Character>, StringBuilder>() {
      public StringBuilder f(List<Character> cs) {
         return new StringBuilder(List.asString(cs));
      }
   };
   public static final F<Array<Character>, String> Array_String = new F<Array<Character>, String>() {
      public String f(Array<Character> cs) {
         final StringBuilder sb = new StringBuilder();
         cs.foreachDoEffect(new Effect1<Character>() {
            public void f(Character c) {
               sb.append(c);
            }
         });
         return sb.toString();
      }
   };
   public static final F<Array<Character>, StringBuffer> Array_StringBuffer = new F<Array<Character>, StringBuffer>() {
      public StringBuffer f(Array<Character> cs) {
         final StringBuffer sb = new StringBuffer();
         cs.foreachDoEffect(new Effect1<Character>() {
            public void f(Character c) {
               sb.append(c);
            }
         });
         return sb;
      }
   };
   public static final F<Array<Character>, StringBuilder> Array_StringBuilder = new F<Array<Character>, StringBuilder>() {
      public StringBuilder f(Array<Character> cs) {
         StringBuilder sb = new StringBuilder();
         cs.foreachDoEffect(Conversions$14$$Lambda$1.lambdaFactory$(sb));
         return sb;
      }

      // $FF: synthetic method
      private static void lambda$f$88(StringBuilder var0, Character c) {
         var0.append(c);
      }

      // $FF: synthetic method
      static void access$lambda$0(StringBuilder var0, Character var1) {
         lambda$f$88(var0, var1);
      }
   };
   public static final F<Stream<Character>, String> Stream_String = new F<Stream<Character>, String>() {
      public String f(Stream<Character> cs) {
         StringBuilder sb = new StringBuilder();
         cs.foreachDoEffect(Conversions$19$$Lambda$1.lambdaFactory$(sb));
         return sb.toString();
      }

      // $FF: synthetic method
      private static void lambda$f$89(StringBuilder var0, Character c) {
         var0.append(c);
      }

      // $FF: synthetic method
      static void access$lambda$0(StringBuilder var0, Character var1) {
         lambda$f$89(var0, var1);
      }
   };
   public static final F<Stream<Character>, StringBuffer> Stream_StringBuffer = new F<Stream<Character>, StringBuffer>() {
      public StringBuffer f(Stream<Character> cs) {
         StringBuffer sb = new StringBuffer();
         cs.foreachDoEffect(Conversions$20$$Lambda$1.lambdaFactory$(sb));
         return sb;
      }

      // $FF: synthetic method
      private static void lambda$f$90(StringBuffer var0, Character c) {
         var0.append(c);
      }

      // $FF: synthetic method
      static void access$lambda$0(StringBuffer var0, Character var1) {
         lambda$f$90(var0, var1);
      }
   };
   public static final F<Stream<Character>, StringBuilder> Stream_StringBuilder = new F<Stream<Character>, StringBuilder>() {
      public StringBuilder f(Stream<Character> cs) {
         StringBuilder sb = new StringBuilder();
         cs.foreachDoEffect(Conversions$21$$Lambda$1.lambdaFactory$(sb));
         return sb;
      }

      // $FF: synthetic method
      private static void lambda$f$91(StringBuilder var0, Character c) {
         var0.append(c);
      }

      // $FF: synthetic method
      static void access$lambda$0(StringBuilder var0, Character var1) {
         lambda$f$91(var0, var1);
      }
   };
   public static final F<Option<Character>, String> Option_String = new F<Option<Character>, String>() {
      public String f(Option<Character> o) {
         return List.asString(o.toList());
      }
   };
   public static final F<Option<Character>, StringBuffer> Option_StringBuffer = new F<Option<Character>, StringBuffer>() {
      public StringBuffer f(Option<Character> o) {
         return new StringBuffer(List.asString(o.toList()));
      }
   };
   public static final F<Option<Character>, StringBuilder> Option_StringBuilder = new F<Option<Character>, StringBuilder>() {
      public StringBuilder f(Option<Character> o) {
         return new StringBuilder(List.asString(o.toList()));
      }
   };
   public static final F<String, List<Character>> String_List = new F<String, List<Character>>() {
      public List<Character> f(String s) {
         return List.fromString(s);
      }
   };
   public static final F<String, Array<Character>> String_Array = new F<String, Array<Character>>() {
      public Array<Character> f(String s) {
         return List.fromString(s).toArray();
      }
   };
   public static final F<String, Option<Character>> String_Option = new F<String, Option<Character>>() {
      public Option<Character> f(String s) {
         return List.fromString(s).toOption();
      }
   };
   public static final F<String, Stream<Character>> String_Stream = new F<String, Stream<Character>>() {
      public Stream<Character> f(String s) {
         return List.fromString(s).toStream();
      }
   };
   public static final F<String, StringBuffer> String_StringBuffer = new F<String, StringBuffer>() {
      public StringBuffer f(String s) {
         return new StringBuffer(s);
      }
   };
   public static final F<String, StringBuilder> String_StringBuilder = new F<String, StringBuilder>() {
      public StringBuilder f(String s) {
         return new StringBuilder(s);
      }
   };
   public static final F<StringBuffer, List<Character>> StringBuffer_List = new F<StringBuffer, List<Character>>() {
      public List<Character> f(StringBuffer s) {
         return List.fromString(s.toString());
      }
   };
   public static final F<StringBuffer, Array<Character>> StringBuffer_Array = new F<StringBuffer, Array<Character>>() {
      public Array<Character> f(StringBuffer s) {
         return List.fromString(s.toString()).toArray();
      }
   };
   public static final F<StringBuffer, Stream<Character>> StringBuffer_Stream = new F<StringBuffer, Stream<Character>>() {
      public Stream<Character> f(StringBuffer s) {
         return List.fromString(s.toString()).toStream();
      }
   };
   public static final F<StringBuffer, Option<Character>> StringBuffer_Option = new F<StringBuffer, Option<Character>>() {
      public Option<Character> f(StringBuffer s) {
         return List.fromString(s.toString()).toOption();
      }
   };
   public static final F<StringBuffer, String> StringBuffer_String = new F<StringBuffer, String>() {
      public String f(StringBuffer s) {
         return s.toString();
      }
   };
   public static final F<StringBuffer, StringBuilder> StringBuffer_StringBuilder = new F<StringBuffer, StringBuilder>() {
      public StringBuilder f(StringBuffer s) {
         return new StringBuilder(s);
      }
   };
   public static final F<StringBuilder, List<Character>> StringBuilder_List = new F<StringBuilder, List<Character>>() {
      public List<Character> f(StringBuilder s) {
         return List.fromString(s.toString());
      }
   };
   public static final F<StringBuilder, Array<Character>> StringBuilder_Array = new F<StringBuilder, Array<Character>>() {
      public Array<Character> f(StringBuilder s) {
         return List.fromString(s.toString()).toArray();
      }
   };
   public static final F<StringBuilder, Stream<Character>> StringBuilder_Stream = new F<StringBuilder, Stream<Character>>() {
      public Stream<Character> f(StringBuilder s) {
         return List.fromString(s.toString()).toStream();
      }
   };
   public static final F<StringBuilder, Option<Character>> StringBuilder_Option = new F<StringBuilder, Option<Character>>() {
      public Option<Character> f(StringBuilder s) {
         return List.fromString(s.toString()).toOption();
      }
   };
   public static final F<StringBuilder, String> StringBuilder_String = new F<StringBuilder, String>() {
      public String f(StringBuilder s) {
         return s.toString();
      }
   };
   public static final F<StringBuilder, StringBuffer> StringBuilder_StringBuffer = new F<StringBuilder, StringBuffer>() {
      public StringBuffer f(StringBuilder s) {
         return new StringBuffer(s);
      }
   };

   private Conversions() {
      throw new UnsupportedOperationException();
   }

   public static <A> F<List<A>, Array<A>> List_Array() {
      return new F<List<A>, Array<A>>() {
         public Array<A> f(List<A> as) {
            return as.toArray();
         }
      };
   }

   public static <A> F<List<A>, Stream<A>> List_Stream() {
      return new F<List<A>, Stream<A>>() {
         public Stream<A> f(List<A> as) {
            return as.toStream();
         }
      };
   }

   public static <A> F<List<A>, Option<A>> List_Option() {
      return new F<List<A>, Option<A>>() {
         public Option<A> f(List<A> as) {
            return as.toOption();
         }
      };
   }

   public static <A, B> F<P1<A>, F<List<B>, Either<A, B>>> List_Either() {
      return new F<P1<A>, F<List<B>, Either<A, B>>>() {
         public F<List<B>, Either<A, B>> f(final P1<A> a) {
            return new F<List<B>, Either<A, B>>() {
               public Either<A, B> f(List<B> bs) {
                  return bs.toEither(a);
               }
            };
         }
      };
   }

   public static <A> F<Array<A>, List<A>> Array_List() {
      return new F<Array<A>, List<A>>() {
         public List<A> f(Array<A> as) {
            return as.toList();
         }
      };
   }

   public static <A> F<Array<A>, Stream<A>> Array_Stream() {
      return new F<Array<A>, Stream<A>>() {
         public Stream<A> f(Array<A> as) {
            return as.toStream();
         }
      };
   }

   public static <A> F<Array<A>, Option<A>> Array_Option() {
      return new F<Array<A>, Option<A>>() {
         public Option<A> f(Array<A> as) {
            return as.toOption();
         }
      };
   }

   public static <A, B> F<P1<A>, F<Array<B>, Either<A, B>>> Array_Either() {
      return new F<P1<A>, F<Array<B>, Either<A, B>>>() {
         public F<Array<B>, Either<A, B>> f(final P1<A> a) {
            return new F<Array<B>, Either<A, B>>() {
               public Either<A, B> f(Array<B> bs) {
                  return bs.toEither(a);
               }
            };
         }
      };
   }

   public static <A> F<Stream<A>, List<A>> Stream_List() {
      return new F<Stream<A>, List<A>>() {
         public List<A> f(Stream<A> as) {
            return as.toList();
         }
      };
   }

   public static <A> F<Stream<A>, Array<A>> Stream_Array() {
      return new F<Stream<A>, Array<A>>() {
         public Array<A> f(Stream<A> as) {
            return as.toArray();
         }
      };
   }

   public static <A> F<Stream<A>, Option<A>> Stream_Option() {
      return new F<Stream<A>, Option<A>>() {
         public Option<A> f(Stream<A> as) {
            return as.toOption();
         }
      };
   }

   public static <A, B> F<P1<A>, F<Stream<B>, Either<A, B>>> Stream_Either() {
      return new F<P1<A>, F<Stream<B>, Either<A, B>>>() {
         public F<Stream<B>, Either<A, B>> f(final P1<A> a) {
            return new F<Stream<B>, Either<A, B>>() {
               public Either<A, B> f(Stream<B> bs) {
                  return bs.toEither(a);
               }
            };
         }
      };
   }

   public static <A> F<Option<A>, List<A>> Option_List() {
      return new F<Option<A>, List<A>>() {
         public List<A> f(Option<A> o) {
            return o.toList();
         }
      };
   }

   public static <A> F<Option<A>, Array<A>> Option_Array() {
      return new F<Option<A>, Array<A>>() {
         public Array<A> f(Option<A> o) {
            return o.toArray();
         }
      };
   }

   public static <A> F<Option<A>, Stream<A>> Option_Stream() {
      return new F<Option<A>, Stream<A>>() {
         public Stream<A> f(Option<A> o) {
            return o.toStream();
         }
      };
   }

   public static <A, B> F<P1<A>, F<Option<B>, Either<A, B>>> Option_Either() {
      return new F<P1<A>, F<Option<B>, Either<A, B>>>() {
         public F<Option<B>, Either<A, B>> f(final P1<A> a) {
            return new F<Option<B>, Either<A, B>>() {
               public Either<A, B> f(Option<B> o) {
                  return o.toEither(a);
               }
            };
         }
      };
   }

   public static F<Effect0, P1<Unit>> Effect0_P1() {
      return Conversions$$Lambda$1.lambdaFactory$();
   }

   public static P1<Unit> Effect0_P1(Effect0 e) {
      return Effect.f(e);
   }

   public static <A> F<A, Unit> Effect1_F(Effect1<A> e) {
      return Effect.f(e);
   }

   public static <A> F<Effect1<A>, F<A, Unit>> Effect1_F() {
      return Conversions$$Lambda$2.lambdaFactory$();
   }

   public static IO<Unit> Effect_IO(Effect0 e) {
      return Conversions$$Lambda$3.lambdaFactory$(e);
   }

   public static F<Effect0, IO<Unit>> Effect_IO() {
      return Conversions$$Lambda$4.lambdaFactory$();
   }

   public static SafeIO<Unit> Effect_SafeIO(Effect0 e) {
      return Conversions$$Lambda$5.lambdaFactory$(e);
   }

   public static F<Effect0, SafeIO<Unit>> Effect_SafeIO() {
      return Conversions$$Lambda$6.lambdaFactory$();
   }

   public static <A, B> F<Either<A, B>, List<A>> Either_ListA() {
      return new F<Either<A, B>, List<A>>() {
         public List<A> f(Either<A, B> e) {
            return e.left().toList();
         }
      };
   }

   public static <A, B> F<Either<A, B>, List<B>> Either_ListB() {
      return new F<Either<A, B>, List<B>>() {
         public List<B> f(Either<A, B> e) {
            return e.right().toList();
         }
      };
   }

   public static <A, B> F<Either<A, B>, Array<A>> Either_ArrayA() {
      return new F<Either<A, B>, Array<A>>() {
         public Array<A> f(Either<A, B> e) {
            return e.left().toArray();
         }
      };
   }

   public static <A, B> F<Either<A, B>, Array<B>> Either_ArrayB() {
      return new F<Either<A, B>, Array<B>>() {
         public Array<B> f(Either<A, B> e) {
            return e.right().toArray();
         }
      };
   }

   public static <A, B> F<Either<A, B>, Stream<A>> Either_StreamA() {
      return new F<Either<A, B>, Stream<A>>() {
         public Stream<A> f(Either<A, B> e) {
            return e.left().toStream();
         }
      };
   }

   public static <A, B> F<Either<A, B>, Stream<B>> Either_StreamB() {
      return new F<Either<A, B>, Stream<B>>() {
         public Stream<B> f(Either<A, B> e) {
            return e.right().toStream();
         }
      };
   }

   public static <A, B> F<Either<A, B>, Option<A>> Either_OptionA() {
      return new F<Either<A, B>, Option<A>>() {
         public Option<A> f(Either<A, B> e) {
            return e.left().toOption();
         }
      };
   }

   public static <A, B> F<Either<A, B>, Option<B>> Either_OptionB() {
      return new F<Either<A, B>, Option<B>>() {
         public Option<B> f(Either<A, B> e) {
            return e.right().toOption();
         }
      };
   }

   public static <B> F<Either<Character, B>, String> Either_StringA() {
      return new F<Either<Character, B>, String>() {
         public String f(Either<Character, B> e) {
            return List.asString(e.left().toList());
         }
      };
   }

   public static <A> F<Either<A, Character>, String> Either_StringB() {
      return new F<Either<A, Character>, String>() {
         public String f(Either<A, Character> e) {
            return List.asString(e.right().toList());
         }
      };
   }

   public static <B> F<Either<Character, B>, StringBuffer> Either_StringBufferA() {
      return new F<Either<Character, B>, StringBuffer>() {
         public StringBuffer f(Either<Character, B> e) {
            return new StringBuffer(List.asString(e.left().toList()));
         }
      };
   }

   public static <A> F<Either<A, Character>, StringBuffer> Either_StringBufferB() {
      return new F<Either<A, Character>, StringBuffer>() {
         public StringBuffer f(Either<A, Character> e) {
            return new StringBuffer(List.asString(e.right().toList()));
         }
      };
   }

   public static <B> F<Either<Character, B>, StringBuilder> Either_StringBuilderA() {
      return new F<Either<Character, B>, StringBuilder>() {
         public StringBuilder f(Either<Character, B> e) {
            return new StringBuilder(List.asString(e.left().toList()));
         }
      };
   }

   public static <A> F<Either<A, Character>, StringBuilder> Either_StringBuilderB() {
      return new F<Either<A, Character>, StringBuilder>() {
         public StringBuilder f(Either<A, Character> e) {
            return new StringBuilder(List.asString(e.right().toList()));
         }
      };
   }

   public static <A> SafeIO<A> F_SafeIO(F<Unit, A> f) {
      return Conversions$$Lambda$7.lambdaFactory$(f);
   }

   public static <A> F<F<Unit, A>, SafeIO<A>> F_SafeIO() {
      return Conversions$$Lambda$8.lambdaFactory$();
   }

   public static <A> F<P1<A>, F<String, Either<A, Character>>> String_Either() {
      return new F<P1<A>, F<String, Either<A, Character>>>() {
         public F<String, Either<A, Character>> f(final P1<A> a) {
            return new F<String, Either<A, Character>>() {
               public Either<A, Character> f(String s) {
                  return List.fromString(s).toEither(a);
               }
            };
         }
      };
   }

   public static <A> F<P1<A>, F<StringBuffer, Either<A, Character>>> StringBuffer_Either() {
      return new F<P1<A>, F<StringBuffer, Either<A, Character>>>() {
         public F<StringBuffer, Either<A, Character>> f(final P1<A> a) {
            return new F<StringBuffer, Either<A, Character>>() {
               public Either<A, Character> f(StringBuffer s) {
                  return List.fromString(s.toString()).toEither(a);
               }
            };
         }
      };
   }

   public static <A> F<P1<A>, F<StringBuilder, Either<A, Character>>> StringBuilder_Either() {
      return new F<P1<A>, F<StringBuilder, Either<A, Character>>>() {
         public F<StringBuilder, Either<A, Character>> f(final P1<A> a) {
            return new F<StringBuilder, Either<A, Character>>() {
               public Either<A, Character> f(StringBuilder s) {
                  return List.fromString(s.toString()).toEither(a);
               }
            };
         }
      };
   }

   public static <A, B, Z extends Exception> SafeIO<Validation<Z, A>> Try_SafeIO(Try0<A, Z> t) {
      return F_SafeIO(Conversions$$Lambda$9.lambdaFactory$(t));
   }

   public static <A, B, Z extends Exception> F<Try0<A, Z>, SafeIO<Validation<Z, A>>> Try_SafeIO() {
      return Conversions$$Lambda$10.lambdaFactory$();
   }

   public static <A, B, Z extends IOException> IO<A> Try_IO(Try0<A, Z> t) {
      return Conversions$$Lambda$11.lambdaFactory$(t);
   }

   public static <A, B, Z extends IOException> F<Try0<A, Z>, IO<A>> Try_IO() {
      return Conversions$$Lambda$12.lambdaFactory$();
   }

   public static <A, B, Z extends IOException> F<A, Validation<Z, B>> Try_F(Try1<A, B, Z> t) {
      return Try.f(t);
   }

   public static <A, B, Z extends IOException> F<Try1<A, B, Z>, F<A, Validation<Z, B>>> Try_F() {
      return Conversions$$Lambda$13.lambdaFactory$();
   }

   public static <E extends Exception> P1<Validation<E, Unit>> TryEffect_P(TryEffect0<E> t) {
      return TryEffect.f(t);
   }

   // $FF: synthetic method
   private static F lambda$Try_F$104(Try1 t) {
      return Try_F(t);
   }

   // $FF: synthetic method
   private static IO lambda$Try_IO$103(Try0 t) {
      return Try_IO(t);
   }

   // $FF: synthetic method
   private static Object lambda$Try_IO$102(Try0 var0) throws IOException {
      return var0.f();
   }

   // $FF: synthetic method
   private static SafeIO lambda$Try_SafeIO$101(Try0 t) {
      return Try_SafeIO(t);
   }

   // $FF: synthetic method
   private static Validation lambda$Try_SafeIO$100(Try0 var0, Unit u) {
      return (Validation)Try.f(var0)._1();
   }

   // $FF: synthetic method
   private static SafeIO lambda$F_SafeIO$99(F f) {
      return F_SafeIO(f);
   }

   // $FF: synthetic method
   private static Object lambda$F_SafeIO$98(F var0) {
      return var0.f(Unit.unit());
   }

   // $FF: synthetic method
   private static SafeIO lambda$Effect_SafeIO$97(Effect0 e) {
      return Effect_SafeIO(e);
   }

   // $FF: synthetic method
   private static Unit lambda$Effect_SafeIO$96(Effect0 var0) {
      var0.f();
      return Unit.unit();
   }

   // $FF: synthetic method
   private static IO lambda$Effect_IO$95(Effect0 e) {
      return Effect_IO(e);
   }

   // $FF: synthetic method
   private static Unit lambda$Effect_IO$94(Effect0 var0) throws IOException {
      var0.f();
      return Unit.unit();
   }

   // $FF: synthetic method
   private static F lambda$Effect1_F$93(Effect1 e) {
      return Effect1_F(e);
   }

   // $FF: synthetic method
   private static P1 lambda$Effect0_P1$92(Effect0 e) {
      return Effect0_P1(e);
   }

   // $FF: synthetic method
   static P1 access$lambda$0(Effect0 var0) {
      return lambda$Effect0_P1$92(var0);
   }

   // $FF: synthetic method
   static F access$lambda$1(Effect1 var0) {
      return lambda$Effect1_F$93(var0);
   }

   // $FF: synthetic method
   static Unit access$lambda$2(Effect0 var0) {
      return lambda$Effect_IO$94(var0);
   }

   // $FF: synthetic method
   static IO access$lambda$3(Effect0 var0) {
      return lambda$Effect_IO$95(var0);
   }

   // $FF: synthetic method
   static Unit access$lambda$4(Effect0 var0) {
      return lambda$Effect_SafeIO$96(var0);
   }

   // $FF: synthetic method
   static SafeIO access$lambda$5(Effect0 var0) {
      return lambda$Effect_SafeIO$97(var0);
   }

   // $FF: synthetic method
   static Object access$lambda$6(F var0) {
      return lambda$F_SafeIO$98(var0);
   }

   // $FF: synthetic method
   static SafeIO access$lambda$7(F var0) {
      return lambda$F_SafeIO$99(var0);
   }

   // $FF: synthetic method
   static Validation access$lambda$8(Try0 var0, Unit var1) {
      return lambda$Try_SafeIO$100(var0, var1);
   }

   // $FF: synthetic method
   static SafeIO access$lambda$9(Try0 var0) {
      return lambda$Try_SafeIO$101(var0);
   }

   // $FF: synthetic method
   static Object access$lambda$10(Try0 var0) {
      return lambda$Try_IO$102(var0);
   }

   // $FF: synthetic method
   static IO access$lambda$11(Try0 var0) {
      return lambda$Try_IO$103(var0);
   }

   // $FF: synthetic method
   static F access$lambda$12(Try1 var0) {
      return lambda$Try_F$104(var0);
   }
}
