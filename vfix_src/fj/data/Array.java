package fj.data;

import fj.F;
import fj.F2;
import fj.Function;
import fj.P;
import fj.P1;
import fj.P2;
import fj.Unit;
import fj.function.Effect1;
import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

public final class Array<A> implements Iterable<A> {
   private final Object[] a;

   private Array(Object[] a) {
      this.a = a;
   }

   public Iterator<A> iterator() {
      return this.toCollection().iterator();
   }

   public A get(int index) {
      return this.a[index];
   }

   public Unit set(int index, A a) {
      this.a[index] = a;
      return Unit.unit();
   }

   public int length() {
      return this.a.length;
   }

   public Array<A>.ImmutableProjection<A> immutable() {
      return new Array.ImmutableProjection(this);
   }

   public boolean isEmpty() {
      return this.a.length == 0;
   }

   public boolean isNotEmpty() {
      return this.a.length != 0;
   }

   public A[] array(Class<A[]> c) {
      return copyOf(this.a, this.a.length, c);
   }

   public Object[] array() {
      return copyOf(this.a, this.a.length);
   }

   public Option<A> toOption() {
      return this.a.length == 0 ? Option.none() : Option.some(this.a[0]);
   }

   public <X> Either<X, A> toEither(P1<X> x) {
      return this.a.length == 0 ? Either.left(x._1()) : Either.right(this.a[0]);
   }

   public List<A> toList() {
      List<A> x = List.nil();

      for(int i = this.a.length - 1; i >= 0; --i) {
         x = x.cons(this.a[i]);
      }

      return x;
   }

   public Stream<A> toStream() {
      return Stream.unfold(new F<Integer, Option<P2<A, Integer>>>() {
         public Option<P2<A, Integer>> f(Integer o) {
            return Array.this.a.length > o ? Option.some(P.p(Array.this.a[o], o + 1)) : Option.none();
         }
      }, 0);
   }

   public <B> Array<B> map(F<A, B> f) {
      Object[] bs = new Object[this.a.length];

      for(int i = 0; i < this.a.length; ++i) {
         bs[i] = f.f(this.a[i]);
      }

      return new Array(bs);
   }

   public Array<A> filter(F<A, Boolean> f) {
      List<A> x = List.nil();

      for(int i = this.a.length - 1; i >= 0; --i) {
         if ((Boolean)f.f(this.a[i])) {
            x = x.cons(this.a[i]);
         }
      }

      return x.toArray();
   }

   public Unit foreach(F<A, Unit> f) {
      Object[] var2 = this.a;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Object x = var2[var4];
         f.f(x);
      }

      return Unit.unit();
   }

   public void foreachDoEffect(Effect1<A> f) {
      Object[] var2 = this.a;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Object x = var2[var4];
         f.f(x);
      }

   }

   public <B> B foldRight(F<A, F<B, B>> f, B b) {
      B x = b;

      for(int i = this.a.length - 1; i >= 0; --i) {
         x = ((F)f.f(this.a[i])).f(x);
      }

      return x;
   }

   public <B> B foldRight(F2<A, B, B> f, B b) {
      return this.foldRight(Function.curry(f), b);
   }

   public <B> B foldLeft(F<B, F<A, B>> f, B b) {
      B x = b;
      Object[] var4 = this.a;
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         Object aa = var4[var6];
         x = ((F)f.f(x)).f(aa);
      }

      return x;
   }

   public <B> B foldLeft(F2<B, A, B> f, B b) {
      return this.foldLeft(Function.curry(f), b);
   }

   public <B> Array<B> scanLeft(F<B, F<A, B>> f, B b) {
      Object[] bs = new Object[this.a.length];
      B x = b;

      for(int i = 0; i < this.a.length; ++i) {
         x = ((F)f.f(x)).f(this.a[i]);
         bs[i] = x;
      }

      return new Array(bs);
   }

   public <B> Array<B> scanLeft(F2<B, A, B> f, B b) {
      return this.scanLeft(Function.curry(f), b);
   }

   public Array<A> scanLeft1(F<A, F<A, A>> f) {
      Object[] bs = new Object[this.a.length];
      A x = this.get(0);
      bs[0] = x;

      for(int i = 1; i < this.a.length; ++i) {
         x = ((F)f.f(x)).f(this.a[i]);
         bs[i] = x;
      }

      return new Array(bs);
   }

   public Array<A> scanLeft1(F2<A, A, A> f) {
      return this.scanLeft1(Function.curry(f));
   }

   public <B> Array<B> scanRight(F<A, F<B, B>> f, B b) {
      Object[] bs = new Object[this.a.length];
      B x = b;

      for(int i = this.a.length - 1; i >= 0; --i) {
         x = ((F)f.f(this.a[i])).f(x);
         bs[i] = x;
      }

      return new Array(bs);
   }

   public <B> Array<B> scanRight(F2<A, B, B> f, B b) {
      return this.scanRight(Function.curry(f), b);
   }

   public Array<A> scanRight1(F<A, F<A, A>> f) {
      Object[] bs = new Object[this.a.length];
      A x = this.get(this.length() - 1);
      bs[this.length() - 1] = x;

      for(int i = this.a.length - 2; i >= 0; --i) {
         x = ((F)f.f(this.a[i])).f(x);
         bs[i] = x;
      }

      return new Array(bs);
   }

   public Array<A> scanRight1(F2<A, A, A> f) {
      return this.scanRight1(Function.curry(f));
   }

   public <B> Array<B> bind(F<A, Array<B>> f) {
      List<Array<B>> x = List.nil();
      int len = 0;

      for(int i = this.a.length - 1; i >= 0; --i) {
         Array<B> bs = (Array)f.f(this.a[i]);
         len += bs.length();
         x = x.cons((Object)bs);
      }

      final Object[] bs = new Object[len];
      x.foreach(new F<Array<B>, Unit>() {
         private int i;

         public Unit f(Array<B> x) {
            System.arraycopy(x.a, 0, bs, this.i, x.a.length);
            this.i += x.a.length;
            return Unit.unit();
         }
      });
      return new Array(bs);
   }

   public <B> Array<B> sequence(Array<B> bs) {
      F<A, Array<B>> c = Function.constant(bs);
      return this.bind(c);
   }

   public <B, C> Array<C> bind(Array<B> sb, F<A, F<B, C>> f) {
      return sb.apply(this.map(f));
   }

   public <B, C> Array<C> bind(Array<B> sb, F2<A, B, C> f) {
      return this.bind(sb, Function.curry(f));
   }

   public <B> Array<B> apply(Array<F<A, B>> lf) {
      return lf.bind(new F<F<A, B>, Array<B>>() {
         public Array<B> f(final F<A, B> f) {
            return Array.this.map(new F<A, B>() {
               public B f(A a) {
                  return f.f(a);
               }
            });
         }
      });
   }

   public Array<A> reverse() {
      Object[] x = new Object[this.a.length];

      for(int i = 0; i < this.a.length; ++i) {
         x[this.a.length - 1 - i] = this.a[i];
      }

      return new Array(x);
   }

   public Array<A> append(Array<A> aas) {
      Object[] x = new Object[this.a.length + aas.a.length];
      System.arraycopy(this.a, 0, x, 0, this.a.length);
      System.arraycopy(aas.a, 0, x, this.a.length, aas.a.length);
      return new Array(x);
   }

   public static <A> Array<A> empty() {
      return new Array(new Object[0]);
   }

   public static <A> Array<A> array(A... a) {
      return new Array(a);
   }

   static <A> Array<A> mkArray(Object[] a) {
      return new Array(a);
   }

   public static <A> Array<A> single(A a) {
      return new Array(new Object[]{a});
   }

   public static <A> F<A[], Array<A>> wrap() {
      return new F<A[], Array<A>>() {
         public Array<A> f(A[] as) {
            return Array.array(as);
         }
      };
   }

   public static <A, B> F<F<A, B>, F<Array<A>, Array<B>>> map() {
      return Function.curry(new F2<F<A, B>, Array<A>, Array<B>>() {
         public Array<B> f(F<A, B> abf, Array<A> array) {
            return array.map(abf);
         }
      });
   }

   public static <A> Array<A> join(Array<Array<A>> o) {
      F<Array<A>, Array<A>> id = Function.identity();
      return o.bind(id);
   }

   public static <A> F<Array<Array<A>>, Array<A>> join() {
      return new F<Array<Array<A>>, Array<A>>() {
         public Array<A> f(Array<Array<A>> as) {
            return Array.join(as);
         }
      };
   }

   public boolean forall(F<A, Boolean> f) {
      Object[] var2 = this.a;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Object x = var2[var4];
         if (!(Boolean)f.f(x)) {
            return false;
         }
      }

      return true;
   }

   public boolean exists(F<A, Boolean> f) {
      Object[] var2 = this.a;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Object x = var2[var4];
         if ((Boolean)f.f(x)) {
            return true;
         }
      }

      return false;
   }

   public Option<A> find(F<A, Boolean> f) {
      Object[] var2 = this.a;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Object x = var2[var4];
         if ((Boolean)f.f(x)) {
            return Option.some(x);
         }
      }

      return Option.none();
   }

   public static Array<Integer> range(int from, int to) {
      if (from >= to) {
         return empty();
      } else {
         Array<Integer> a = new Array(new Integer[to - from]);

         for(int i = from; i < to; ++i) {
            a.set(i - from, i);
         }

         return a;
      }
   }

   public <B, C> Array<C> zipWith(Array<B> bs, F<A, F<B, C>> f) {
      int len = Math.min(this.a.length, bs.length());
      Array<C> x = new Array(new Object[len]);

      for(int i = 0; i < len; ++i) {
         x.set(i, ((F)f.f(this.get(i))).f(bs.get(i)));
      }

      return x;
   }

   public <B, C> Array<C> zipWith(Array<B> bs, F2<A, B, C> f) {
      return this.zipWith(bs, Function.curry(f));
   }

   public <B> Array<P2<A, B>> zip(Array<B> bs) {
      F<A, F<B, P2<A, B>>> __2 = P.p2();
      return this.zipWith(bs, __2);
   }

   public Array<P2<A, Integer>> zipIndex() {
      return this.zipWith(range(0, this.length()), new F<A, F<Integer, P2<A, Integer>>>() {
         public F<Integer, P2<A, Integer>> f(final A a) {
            return new F<Integer, P2<A, Integer>>() {
               public P2<A, Integer> f(Integer i) {
                  return P.p(a, i);
               }
            };
         }
      });
   }

   public Collection<A> toCollection() {
      return new AbstractCollection<A>() {
         public Iterator<A> iterator() {
            return new Iterator<A>() {
               private int i;

               public boolean hasNext() {
                  return this.i < Array.this.a.length;
               }

               public A next() {
                  if (this.i >= Array.this.a.length) {
                     throw new NoSuchElementException();
                  } else {
                     A aa = Array.this.a[this.i];
                     ++this.i;
                     return aa;
                  }
               }

               public void remove() {
                  throw new UnsupportedOperationException();
               }
            };
         }

         public int size() {
            return Array.this.a.length;
         }
      };
   }

   public static <A> Array<A> iterableArray(Iterable<A> i) {
      return List.iterableList(i).toArray();
   }

   public static <A, B> P2<Array<A>, Array<B>> unzip(Array<P2<A, B>> xs) {
      int len = xs.length();
      Array<A> aa = new Array(new Object[len]);
      Array<B> ab = new Array(new Object[len]);

      for(int i = len - 1; i >= 0; --i) {
         P2<A, B> p = (P2)xs.get(i);
         aa.set(i, p._1());
         ab.set(i, p._2());
      }

      return P.p(aa, ab);
   }

   public static <T, U> T[] copyOf(U[] a, int len, Class<? extends T[]> newType) {
      T[] copy = newType == Object[].class ? (Object[])(new Object[len]) : (Object[])((Object[])java.lang.reflect.Array.newInstance(newType.getComponentType(), len));
      System.arraycopy(a, 0, copy, 0, Math.min(a.length, len));
      return copy;
   }

   public static <T> T[] copyOf(T[] a, int len) {
      return (Object[])copyOf(a, len, a.getClass());
   }

   public static char[] copyOfRange(char[] a, int from, int to) {
      int len = to - from;
      if (len < 0) {
         throw new IllegalArgumentException(from + " > " + to);
      } else {
         char[] copy = new char[len];
         System.arraycopy(a, from, copy, 0, Math.min(a.length - from, len));
         return copy;
      }
   }

   public final class ImmutableProjection<A> implements Iterable<A> {
      private final Array<A> a;

      private ImmutableProjection(Array<A> a) {
         this.a = a;
      }

      public Iterator<A> iterator() {
         return this.a.iterator();
      }

      public A get(int index) {
         return this.a.get(index);
      }

      public int length() {
         return this.a.length();
      }

      public boolean isEmpty() {
         return this.a.isEmpty();
      }

      public boolean isNotEmpty() {
         return this.a.isNotEmpty();
      }

      public Option<A> toOption() {
         return this.a.toOption();
      }

      public <X> Either<X, A> toEither(P1<X> x) {
         return this.a.toEither(x);
      }

      public List<A> toList() {
         return this.a.toList();
      }

      public Stream<A> toStream() {
         return this.a.toStream();
      }

      public <B> Array<B> map(F<A, B> f) {
         return this.a.map(f);
      }

      public Array<A> filter(F<A, Boolean> f) {
         return this.a.filter(f);
      }

      public Unit foreach(F<A, Unit> f) {
         return this.a.foreach(f);
      }

      public <B> B foldRight(F<A, F<B, B>> f, B b) {
         return this.a.foldRight(f, b);
      }

      public <B> B foldLeft(F<B, F<A, B>> f, B b) {
         return this.a.foldLeft(f, b);
      }

      public <B> Array<B> bind(F<A, Array<B>> f) {
         return this.a.bind(f);
      }

      public <B> Array<B> sequence(Array<B> bs) {
         return this.a.sequence(bs);
      }

      public <B> Array<B> apply(Array<F<A, B>> lf) {
         return this.a.apply(lf);
      }

      public Array<A> reverse() {
         return this.a.reverse();
      }

      public Array<A> append(Array<A> aas) {
         return this.a.append(aas);
      }

      public Collection<A> toCollection() {
         return this.a.toCollection();
      }

      // $FF: synthetic method
      ImmutableProjection(Array x1, Object x2) {
         this(x1);
      }
   }
}
