package fj.data;

import fj.Bottom;
import fj.F;
import fj.F1Functions;
import fj.F2;
import fj.Function;
import fj.P;
import fj.P1;
import fj.P2;
import fj.Try;
import fj.Unit;
import fj.function.Try0;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Arrays;

public class IOFunctions {
   private static final int DEFAULT_BUFFER_SIZE = 4096;
   public static final F<java.io.Reader, IO<Unit>> closeReader = new F<java.io.Reader, IO<Unit>>() {
      public IO<Unit> f(java.io.Reader r) {
         return IOFunctions.closeReader(r);
      }
   };
   public static final BufferedReader stdinBufferedReader;

   public static <A> Try0<A, IOException> toTry(IO<A> io) {
      return IOFunctions$$Lambda$1.lambdaFactory$(io);
   }

   public static <A> P1<Validation<IOException, A>> p(IO<A> io) {
      return Try.f(toTry(io));
   }

   public static <A> IO<A> io(P1<A> p) {
      return IOFunctions$$Lambda$2.lambdaFactory$(p);
   }

   public static <A> IO<A> io(Try0<A, ? extends IOException> t) {
      return IOFunctions$$Lambda$3.lambdaFactory$(t);
   }

   public static IO<Unit> closeReader(final java.io.Reader r) {
      return new IO<Unit>() {
         public Unit run() throws IOException {
            r.close();
            return Unit.unit();
         }
      };
   }

   public static <A> IO<Iteratee.IterV<String, A>> enumFileLines(File f, Option<Charset> encoding, Iteratee.IterV<String, A> i) {
      return bracket(bufferedReader(f, encoding), Function.vary(closeReader), Function.partialApply2(lineReader(), i));
   }

   public static <A> IO<Iteratee.IterV<char[], A>> enumFileCharChunks(File f, Option<Charset> encoding, Iteratee.IterV<char[], A> i) {
      return bracket(fileReader(f, encoding), Function.vary(closeReader), Function.partialApply2(charChunkReader(), i));
   }

   public static <A> IO<Iteratee.IterV<Character, A>> enumFileChars(File f, Option<Charset> encoding, Iteratee.IterV<Character, A> i) {
      return bracket(fileReader(f, encoding), Function.vary(closeReader), Function.partialApply2(charChunkReader2(), i));
   }

   public static IO<BufferedReader> bufferedReader(File f, Option<Charset> encoding) {
      return map(fileReader(f, encoding), new F<java.io.Reader, BufferedReader>() {
         public BufferedReader f(java.io.Reader a) {
            return new BufferedReader(a);
         }
      });
   }

   public static IO<java.io.Reader> fileReader(final File f, final Option<Charset> encoding) {
      return new IO<java.io.Reader>() {
         public java.io.Reader run() throws IOException {
            FileInputStream fis = new FileInputStream(f);
            return encoding.isNone() ? new InputStreamReader(fis) : new InputStreamReader(fis, (Charset)encoding.some());
         }
      };
   }

   public static final <A, B, C> IO<C> bracket(final IO<A> init, final F<A, IO<B>> fin, final F<A, IO<C>> body) {
      return new IO<C>() {
         public C run() throws IOException {
            Object a = init.run();

            Object var2;
            try {
               var2 = ((IO)body.f(a)).run();
            } catch (IOException var6) {
               throw var6;
            } finally {
               fin.f(a);
            }

            return var2;
         }
      };
   }

   public static final <A> IO<A> unit(final A a) {
      return new IO<A>() {
         public A run() throws IOException {
            return a;
         }
      };
   }

   public static final <A> IO<A> lazy(P1<A> p) {
      return IOFunctions$$Lambda$4.lambdaFactory$(p);
   }

   public static final <A> IO<A> lazy(F<Unit, A> f) {
      return IOFunctions$$Lambda$5.lambdaFactory$(f);
   }

   public static final <A> SafeIO<A> lazySafe(F<Unit, A> f) {
      return IOFunctions$$Lambda$6.lambdaFactory$(f);
   }

   public static final <A> SafeIO<A> lazySafe(P1<A> f) {
      return IOFunctions$$Lambda$7.lambdaFactory$(f);
   }

   public static <A> F<BufferedReader, F<Iteratee.IterV<String, A>, IO<Iteratee.IterV<String, A>>>> lineReader() {
      final F<Iteratee.IterV<String, A>, Boolean> isDone = new F<Iteratee.IterV<String, A>, Boolean>() {
         final F<P2<A, Iteratee.Input<String>>, P1<Boolean>> done = Function.constant(P.p(true));
         final F<F<Iteratee.Input<String>, Iteratee.IterV<String, A>>, P1<Boolean>> cont = Function.constant(P.p(false));

         public Boolean f(Iteratee.IterV<String, A> i) {
            return (Boolean)((P1)i.fold(this.done, this.cont))._1();
         }
      };
      return new F<BufferedReader, F<Iteratee.IterV<String, A>, IO<Iteratee.IterV<String, A>>>>() {
         public F<Iteratee.IterV<String, A>, IO<Iteratee.IterV<String, A>>> f(final BufferedReader r) {
            return new F<Iteratee.IterV<String, A>, IO<Iteratee.IterV<String, A>>>() {
               final F<P2<A, Iteratee.Input<String>>, P1<Iteratee.IterV<String, A>>> done = Bottom.errorF("iteratee is done");

               public IO<Iteratee.IterV<String, A>> f(final Iteratee.IterV<String, A> it) {
                  return new IO<Iteratee.IterV<String, A>>() {
                     public Iteratee.IterV<String, A> run() throws IOException {
                        Iteratee.IterV i;
                        F cont;
                        for(i = it; !(Boolean)isDone.f(i); i = (Iteratee.IterV)((P1)i.fold(done, cont))._1()) {
                           String s = r.readLine();
                           if (s == null) {
                              return i;
                           }

                           Iteratee.Input<String> input = Iteratee.Input.el(s);
                           cont = F1Functions.lazy(Function.apply(input));
                        }

                        return i;
                     }
                  };
               }
            };
         }
      };
   }

   public static <A> F<java.io.Reader, F<Iteratee.IterV<char[], A>, IO<Iteratee.IterV<char[], A>>>> charChunkReader() {
      final F<Iteratee.IterV<char[], A>, Boolean> isDone = new F<Iteratee.IterV<char[], A>, Boolean>() {
         final F<P2<A, Iteratee.Input<char[]>>, P1<Boolean>> done = Function.constant(P.p(true));
         final F<F<Iteratee.Input<char[]>, Iteratee.IterV<char[], A>>, P1<Boolean>> cont = Function.constant(P.p(false));

         public Boolean f(Iteratee.IterV<char[], A> i) {
            return (Boolean)((P1)i.fold(this.done, this.cont))._1();
         }
      };
      return new F<java.io.Reader, F<Iteratee.IterV<char[], A>, IO<Iteratee.IterV<char[], A>>>>() {
         public F<Iteratee.IterV<char[], A>, IO<Iteratee.IterV<char[], A>>> f(final java.io.Reader r) {
            return new F<Iteratee.IterV<char[], A>, IO<Iteratee.IterV<char[], A>>>() {
               final F<P2<A, Iteratee.Input<char[]>>, P1<Iteratee.IterV<char[], A>>> done = Bottom.errorF("iteratee is done");

               public IO<Iteratee.IterV<char[], A>> f(final Iteratee.IterV<char[], A> it) {
                  return new IO<Iteratee.IterV<char[], A>>() {
                     public Iteratee.IterV<char[], A> run() throws IOException {
                        Iteratee.IterV i;
                        F cont;
                        for(i = it; !(Boolean)isDone.f(i); i = (Iteratee.IterV)((P1)i.fold(done, cont))._1()) {
                           char[] buffer = new char[4096];
                           int numRead = r.read(buffer);
                           if (numRead == -1) {
                              return i;
                           }

                           if (numRead < buffer.length) {
                              buffer = Arrays.copyOfRange(buffer, 0, numRead);
                           }

                           Iteratee.Input<char[]> input = Iteratee.Input.el(buffer);
                           cont = F1Functions.lazy(Function.apply(input));
                        }

                        return i;
                     }
                  };
               }
            };
         }
      };
   }

   public static <A> F<java.io.Reader, F<Iteratee.IterV<Character, A>, IO<Iteratee.IterV<Character, A>>>> charChunkReader2() {
      final F<Iteratee.IterV<Character, A>, Boolean> isDone = new F<Iteratee.IterV<Character, A>, Boolean>() {
         final F<P2<A, Iteratee.Input<Character>>, P1<Boolean>> done = Function.constant(P.p(true));
         final F<F<Iteratee.Input<Character>, Iteratee.IterV<Character, A>>, P1<Boolean>> cont = Function.constant(P.p(false));

         public Boolean f(Iteratee.IterV<Character, A> i) {
            return (Boolean)((P1)i.fold(this.done, this.cont))._1();
         }
      };
      return new F<java.io.Reader, F<Iteratee.IterV<Character, A>, IO<Iteratee.IterV<Character, A>>>>() {
         public F<Iteratee.IterV<Character, A>, IO<Iteratee.IterV<Character, A>>> f(final java.io.Reader r) {
            return new F<Iteratee.IterV<Character, A>, IO<Iteratee.IterV<Character, A>>>() {
               final F<P2<A, Iteratee.Input<Character>>, Iteratee.IterV<Character, A>> done = Bottom.errorF("iteratee is done");

               public IO<Iteratee.IterV<Character, A>> f(final Iteratee.IterV<Character, A> it) {
                  return new IO<Iteratee.IterV<Character, A>>() {
                     public Iteratee.IterV<Character, A> run() throws IOException {
                        Iteratee.IterV i = it;

                        while(!(Boolean)isDone.f(i)) {
                           char[] buffer = new char[4096];
                           int numRead = r.read(buffer);
                           if (numRead == -1) {
                              return i;
                           }

                           if (numRead < buffer.length) {
                              buffer = Arrays.copyOfRange(buffer, 0, numRead);
                           }

                           for(int c = 0; c < buffer.length; ++c) {
                              Iteratee.Input<Character> input = Iteratee.Input.el(buffer[c]);
                              F<F<Iteratee.Input<Character>, Iteratee.IterV<Character, A>>, Iteratee.IterV<Character, A>> cont = Function.apply(input);
                              i = (Iteratee.IterV)i.fold(done, cont);
                           }
                        }

                        return i;
                     }
                  };
               }
            };
         }
      };
   }

   public static final <A, B> IO<B> map(final IO<A> io, final F<A, B> f) {
      return new IO<B>() {
         public B run() throws IOException {
            return f.f(io.run());
         }
      };
   }

   public static final <A, B> IO<B> bind(final IO<A> io, final F<A, IO<B>> f) {
      return new IO<B>() {
         public B run() throws IOException {
            return ((IO)f.f(io.run())).run();
         }
      };
   }

   public static <A> IO<List<A>> sequence(List<IO<A>> list) {
      F2<IO<A>, IO<List<A>>, IO<List<A>>> f2 = IOFunctions$$Lambda$8.lambdaFactory$();
      return (IO)list.foldRight((F2)f2, unit(List.nil()));
   }

   public static <A> IO<Stream<A>> sequence(Stream<IO<A>> stream) {
      F2<IO<Stream<A>>, IO<A>, IO<Stream<A>>> f2 = IOFunctions$$Lambda$9.lambdaFactory$();
      return (IO)stream.foldLeft((F2)f2, unit(Stream.nil()));
   }

   public static <A, B> IO<List<B>> traverse(List<A> list, F<A, IO<B>> f) {
      F2<A, IO<List<B>>, IO<List<B>>> f2 = IOFunctions$$Lambda$10.lambdaFactory$(f);
      return (IO)list.foldRight((F2)f2, unit(List.nil()));
   }

   public static <A> IO<A> join(IO<IO<A>> io1) {
      return bind(io1, IOFunctions$$Lambda$11.lambdaFactory$());
   }

   public static <A> SafeIO<Validation<IOException, A>> toSafeIO(IO<A> io) {
      return IOFunctions$$Lambda$12.lambdaFactory$(io);
   }

   public static <A, B> IO<B> append(IO<A> io1, IO<B> io2) {
      return IOFunctions$$Lambda$13.lambdaFactory$(io1, io2);
   }

   public static <A, B> IO<A> left(IO<A> io1, IO<B> io2) {
      return IOFunctions$$Lambda$14.lambdaFactory$(io1, io2);
   }

   public static <A, B> IO<B> flatMap(IO<A> io, F<A, IO<B>> f) {
      return bind(io, f);
   }

   static <A> IO<Stream<A>> sequenceWhile(final Stream<IO<A>> stream, final F<A, Boolean> f) {
      return new IO<Stream<A>>() {
         public Stream<A> run() throws IOException {
            boolean loop = true;
            Stream<IO<A>> input = stream;
            Stream result = Stream.nil();

            while(loop) {
               if (input.isEmpty()) {
                  loop = false;
               } else {
                  A a = ((IO)input.head()).run();
                  if (!(Boolean)f.f(a)) {
                     loop = false;
                  } else {
                     input = (Stream)input.tail()._1();
                     result = result.cons(a);
                  }
               }
            }

            return result.reverse();
         }
      };
   }

   public static <A, B> IO<B> apply(IO<A> io, IO<F<A, B>> iof) {
      return bind(iof, IOFunctions$$Lambda$15.lambdaFactory$(io));
   }

   public static <A, B, C> IO<C> liftM2(IO<A> ioa, IO<B> iob, F2<A, B, C> f) {
      return bind(ioa, IOFunctions$$Lambda$16.lambdaFactory$(iob, f));
   }

   public static <A> IO<List<A>> replicateM(IO<A> ioa, int n) {
      return sequence(List.replicate(n, ioa));
   }

   public static <A> IO<State<BufferedReader, Validation<IOException, String>>> readerState() {
      return IOFunctions$$Lambda$17.lambdaFactory$();
   }

   public static IO<String> stdinReadLine() {
      return IOFunctions$$Lambda$18.lambdaFactory$();
   }

   public static IO<Unit> stdoutPrintln(String s) {
      return IOFunctions$$Lambda$19.lambdaFactory$(s);
   }

   // $FF: synthetic method
   private static Unit lambda$stdoutPrintln$135(String var0) throws IOException {
      System.out.println(var0);
      return Unit.unit();
   }

   // $FF: synthetic method
   private static String lambda$stdinReadLine$134() throws IOException {
      return stdinBufferedReader.readLine();
   }

   // $FF: synthetic method
   private static State lambda$readerState$133() throws IOException {
      return State.unit(IOFunctions$$Lambda$20.lambdaFactory$());
   }

   // $FF: synthetic method
   private static P2 lambda$null$132(BufferedReader r) {
      return P.p(r, Try.f(IOFunctions$$Lambda$21.lambdaFactory$()).f(r));
   }

   // $FF: synthetic method
   private static String lambda$null$131(BufferedReader r2) throws IOException {
      return r2.readLine();
   }

   // $FF: synthetic method
   private static IO lambda$liftM2$130(IO var0, F2 var1, Object a) {
      return map(var0, IOFunctions$$Lambda$22.lambdaFactory$(var1, a));
   }

   // $FF: synthetic method
   private static Object lambda$null$129(F2 var0, Object var1, Object b) {
      return var0.f(var1, b);
   }

   // $FF: synthetic method
   private static IO lambda$apply$128(IO var0, F f) {
      return map(var0, IOFunctions$$Lambda$23.lambdaFactory$(f));
   }

   // $FF: synthetic method
   private static Object lambda$null$127(F var0, Object a) {
      return var0.f(a);
   }

   // $FF: synthetic method
   private static Object lambda$left$126(IO var0, IO var1) throws IOException {
      A a = var0.run();
      var1.run();
      return a;
   }

   // $FF: synthetic method
   private static Object lambda$append$125(IO var0, IO var1) throws IOException {
      var0.run();
      return var1.run();
   }

   // $FF: synthetic method
   private static Validation lambda$toSafeIO$124(IO var0) {
      return (Validation)Try.f(IOFunctions$$Lambda$24.lambdaFactory$(var0))._1();
   }

   // $FF: synthetic method
   private static Object lambda$null$123(IO var0) throws IOException {
      return var0.run();
   }

   // $FF: synthetic method
   private static IO lambda$join$122(IO io2) {
      return io2;
   }

   // $FF: synthetic method
   private static IO lambda$traverse$121(F var0, Object a, IO acc) {
      return bind(acc, IOFunctions$$Lambda$25.lambdaFactory$(var0, a));
   }

   // $FF: synthetic method
   private static IO lambda$null$120(F var0, Object var1, List bs) {
      return map((IO)var0.f(var1), IOFunctions$$Lambda$26.lambdaFactory$(bs));
   }

   // $FF: synthetic method
   private static List lambda$null$119(List var0, Object b) {
      return var0.append(List.list(b));
   }

   // $FF: synthetic method
   private static IO lambda$sequence$118(IO ioList, IO io) {
      return bind(ioList, IOFunctions$$Lambda$27.lambdaFactory$(io));
   }

   // $FF: synthetic method
   private static IO lambda$null$117(IO var0, Stream xs) {
      return map(var0, IOFunctions$$Lambda$28.lambdaFactory$(xs));
   }

   // $FF: synthetic method
   private static Stream lambda$null$116(Stream var0, Object x) {
      return Stream.cons(x, P.lazy(IOFunctions$$Lambda$29.lambdaFactory$(var0)));
   }

   // $FF: synthetic method
   private static Stream lambda$null$115(Stream var0, Unit u) {
      return var0;
   }

   // $FF: synthetic method
   private static IO lambda$sequence$114(IO io, IO ioList) {
      return bind(ioList, IOFunctions$$Lambda$30.lambdaFactory$(io));
   }

   // $FF: synthetic method
   private static IO lambda$null$113(IO var0, List xs) {
      return map(var0, IOFunctions$$Lambda$31.lambdaFactory$(xs));
   }

   // $FF: synthetic method
   private static List lambda$null$112(List var0, Object x) {
      return List.cons(x, var0);
   }

   // $FF: synthetic method
   private static Object lambda$lazySafe$111(P1 var0) {
      return var0._1();
   }

   // $FF: synthetic method
   private static Object lambda$lazySafe$110(F var0) {
      return var0.f(Unit.unit());
   }

   // $FF: synthetic method
   private static Object lambda$lazy$109(F var0) throws IOException {
      return var0.f(Unit.unit());
   }

   // $FF: synthetic method
   private static Object lambda$lazy$108(P1 var0) throws IOException {
      return var0._1();
   }

   // $FF: synthetic method
   private static Object lambda$io$107(Try0 var0) throws IOException {
      return var0.f();
   }

   // $FF: synthetic method
   private static Object lambda$io$106(P1 var0) throws IOException {
      return var0._1();
   }

   // $FF: synthetic method
   private static Object lambda$toTry$105(IO var0) throws IOException {
      return var0.run();
   }

   static {
      stdinBufferedReader = new BufferedReader(new InputStreamReader(System.in));
   }

   // $FF: synthetic method
   static Object access$lambda$0(IO var0) {
      return lambda$toTry$105(var0);
   }

   // $FF: synthetic method
   static Object access$lambda$1(P1 var0) {
      return lambda$io$106(var0);
   }

   // $FF: synthetic method
   static Object access$lambda$2(Try0 var0) {
      return lambda$io$107(var0);
   }

   // $FF: synthetic method
   static Object access$lambda$3(P1 var0) {
      return lambda$lazy$108(var0);
   }

   // $FF: synthetic method
   static Object access$lambda$4(F var0) {
      return lambda$lazy$109(var0);
   }

   // $FF: synthetic method
   static Object access$lambda$5(F var0) {
      return lambda$lazySafe$110(var0);
   }

   // $FF: synthetic method
   static Object access$lambda$6(P1 var0) {
      return lambda$lazySafe$111(var0);
   }

   // $FF: synthetic method
   static IO access$lambda$7(IO var0, IO var1) {
      return lambda$sequence$114(var0, var1);
   }

   // $FF: synthetic method
   static IO access$lambda$8(IO var0, IO var1) {
      return lambda$sequence$118(var0, var1);
   }

   // $FF: synthetic method
   static IO access$lambda$9(F var0, Object var1, IO var2) {
      return lambda$traverse$121(var0, var1, var2);
   }

   // $FF: synthetic method
   static IO access$lambda$10(IO var0) {
      return lambda$join$122(var0);
   }

   // $FF: synthetic method
   static Validation access$lambda$11(IO var0) {
      return lambda$toSafeIO$124(var0);
   }

   // $FF: synthetic method
   static Object access$lambda$12(IO var0, IO var1) {
      return lambda$append$125(var0, var1);
   }

   // $FF: synthetic method
   static Object access$lambda$13(IO var0, IO var1) {
      return lambda$left$126(var0, var1);
   }

   // $FF: synthetic method
   static IO access$lambda$14(IO var0, F var1) {
      return lambda$apply$128(var0, var1);
   }

   // $FF: synthetic method
   static IO access$lambda$15(IO var0, F2 var1, Object var2) {
      return lambda$liftM2$130(var0, var1, var2);
   }

   // $FF: synthetic method
   static State access$lambda$16() {
      return lambda$readerState$133();
   }

   // $FF: synthetic method
   static String access$lambda$17() {
      return lambda$stdinReadLine$134();
   }

   // $FF: synthetic method
   static Unit access$lambda$18(String var0) {
      return lambda$stdoutPrintln$135(var0);
   }

   // $FF: synthetic method
   static P2 access$lambda$19(BufferedReader var0) {
      return lambda$null$132(var0);
   }

   // $FF: synthetic method
   static String access$lambda$20(BufferedReader var0) {
      return lambda$null$131(var0);
   }

   // $FF: synthetic method
   static Object access$lambda$21(F2 var0, Object var1, Object var2) {
      return lambda$null$129(var0, var1, var2);
   }

   // $FF: synthetic method
   static Object access$lambda$22(F var0, Object var1) {
      return lambda$null$127(var0, var1);
   }

   // $FF: synthetic method
   static Object access$lambda$23(IO var0) {
      return lambda$null$123(var0);
   }

   // $FF: synthetic method
   static IO access$lambda$24(F var0, Object var1, List var2) {
      return lambda$null$120(var0, var1, var2);
   }

   // $FF: synthetic method
   static List access$lambda$25(List var0, Object var1) {
      return lambda$null$119(var0, var1);
   }

   // $FF: synthetic method
   static IO access$lambda$26(IO var0, Stream var1) {
      return lambda$null$117(var0, var1);
   }

   // $FF: synthetic method
   static Stream access$lambda$27(Stream var0, Object var1) {
      return lambda$null$116(var0, var1);
   }

   // $FF: synthetic method
   static Stream access$lambda$28(Stream var0, Unit var1) {
      return lambda$null$115(var0, var1);
   }

   // $FF: synthetic method
   static IO access$lambda$29(IO var0, List var1) {
      return lambda$null$113(var0, var1);
   }

   // $FF: synthetic method
   static List access$lambda$30(List var0, Object var1) {
      return lambda$null$112(var0, var1);
   }
}
