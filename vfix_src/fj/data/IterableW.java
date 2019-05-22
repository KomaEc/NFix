package fj.data;

import fj.Equal;
import fj.F;
import fj.F2;
import fj.F3;
import fj.Function;
import fj.P1;
import fj.P2;
import java.util.Collection;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public final class IterableW<A> implements Iterable<A> {
   private final Iterable<A> i;

   private IterableW(Iterable<A> i) {
      this.i = i;
   }

   public static <A> IterableW<A> wrap(Iterable<A> a) {
      return new IterableW(a);
   }

   public static <A, T extends Iterable<A>> F<T, IterableW<A>> wrap() {
      return new F<T, IterableW<A>>() {
         public IterableW<A> f(T a) {
            return IterableW.wrap(a);
         }
      };
   }

   public static <A> IterableW<A> iterable(A a) {
      return wrap(Option.some(a));
   }

   public static <A, B> F<A, IterableW<B>> iterable(final F<A, B> f) {
      return new F<A, IterableW<B>>() {
         public IterableW<B> f(A a) {
            return IterableW.iterable(f.f(a));
         }
      };
   }

   public static <A, B> F<F<A, B>, F<A, IterableW<B>>> arrow() {
      return new F<F<A, B>, F<A, IterableW<B>>>() {
         public F<A, IterableW<B>> f(F<A, B> f) {
            return IterableW.iterable(f);
         }
      };
   }

   public <B, T extends Iterable<B>> IterableW<B> bind(final F<A, T> f) {
      return wrap(Stream.iterableStream(this).bind(new F<A, Stream<B>>() {
         public Stream<B> f(A a) {
            return Stream.iterableStream((Iterable)f.f(a));
         }
      }));
   }

   public <B> IterableW<B> apply(Iterable<F<A, B>> f) {
      return wrap(f).bind(new F<F<A, B>, Iterable<B>>() {
         public Iterable<B> f(F<A, B> f) {
            return IterableW.this.map(f);
         }
      });
   }

   public static <A, B, C> IterableW<C> bind(Iterable<A> a, Iterable<B> b, F<A, F<B, C>> f) {
      return wrap(b).apply(wrap(a).map(f));
   }

   public static <A, B, C> F<Iterable<A>, F<Iterable<B>, IterableW<C>>> liftM2(final F<A, F<B, C>> f) {
      return Function.curry(new F2<Iterable<A>, Iterable<B>, IterableW<C>>() {
         public IterableW<C> f(Iterable<A> ca, Iterable<B> cb) {
            return IterableW.bind(ca, cb, f);
         }
      });
   }

   public static <A, T extends Iterable<A>> IterableW<IterableW<A>> sequence(Iterable<T> as) {
      final Stream<T> ts = Stream.iterableStream(as);
      return ts.isEmpty() ? iterable((Object)wrap(Option.none())) : wrap((Iterable)ts.head()).bind(new F<A, Iterable<IterableW<A>>>() {
         public Iterable<IterableW<A>> f(final A a) {
            return IterableW.sequence((Iterable)ts.tail().map(IterableW.wrap())._1()).bind(new F<IterableW<A>, Iterable<IterableW<A>>>() {
               public Iterable<IterableW<A>> f(final IterableW<A> as) {
                  return IterableW.iterable((Object)IterableW.wrap(Stream.cons(a, new P1<Stream<A>>() {
                     public Stream<A> _1() {
                        return Stream.iterableStream(as);
                     }
                  })));
               }
            });
         }
      });
   }

   public static <A, B, T extends Iterable<B>> F<IterableW<A>, F<F<A, T>, IterableW<B>>> bind() {
      return new F<IterableW<A>, F<F<A, T>, IterableW<B>>>() {
         public F<F<A, T>, IterableW<B>> f(final IterableW<A> a) {
            return new F<F<A, T>, IterableW<B>>() {
               public IterableW<B> f(F<A, T> f) {
                  return a.bind(f);
               }
            };
         }
      };
   }

   public static <A, T extends Iterable<A>> IterableW<A> join(Iterable<T> as) {
      F<T, T> id = Function.identity();
      return wrap(as).bind(id);
   }

   public static <A, T extends Iterable<A>> F<Iterable<T>, IterableW<A>> join() {
      return new F<Iterable<T>, IterableW<A>>() {
         public IterableW<A> f(Iterable<T> a) {
            return IterableW.join(a);
         }
      };
   }

   public <B> IterableW<B> map(F<A, B> f) {
      return this.bind(iterable(f));
   }

   public static <A, B> F<F<A, B>, F<IterableW<A>, IterableW<B>>> map() {
      return new F<F<A, B>, F<IterableW<A>, IterableW<B>>>() {
         public F<IterableW<A>, IterableW<B>> f(final F<A, B> f) {
            return new F<IterableW<A>, IterableW<B>>() {
               public IterableW<B> f(IterableW<A> a) {
                  return a.map(f);
               }
            };
         }
      };
   }

   public <B> B foldLeft(F<B, F<A, B>> f, B z) {
      B p = z;

      Object x;
      for(Iterator var4 = this.iterator(); var4.hasNext(); p = ((F)f.f(p)).f(x)) {
         x = var4.next();
      }

      return p;
   }

   public A foldLeft1(F2<A, A, A> f) {
      return this.foldLeft1(Function.curry(f));
   }

   public A foldLeft1(F<A, F<A, A>> f) {
      return Stream.iterableStream(this).foldLeft1(f);
   }

   public <B> B foldRight(final F2<A, B, B> f, B z) {
      F<B, B> id = Function.identity();
      return ((F)this.foldLeft(Function.curry(new F3<F<B, B>, A, B, B>() {
         public B f(F<B, B> k, A a, B b) {
            return k.f(f.f(a, b));
         }
      }), id)).f(z);
   }

   public Iterator<A> iterator() {
      return this.i.iterator();
   }

   public <B> IterableW<B> zapp(Iterable<F<A, B>> fs) {
      return wrap(Stream.iterableStream(this).zapp(Stream.iterableStream(fs)));
   }

   public <B, C> Iterable<C> zipWith(Iterable<B> bs, F<A, F<B, C>> f) {
      return wrap(Stream.iterableStream(this).zipWith(Stream.iterableStream(bs), f));
   }

   public <B, C> Iterable<C> zipWith(Iterable<B> bs, F2<A, B, C> f) {
      return this.zipWith(bs, Function.curry(f));
   }

   public <B> Iterable<P2<A, B>> zip(Iterable<B> bs) {
      return wrap(Stream.iterableStream(this).zip(Stream.iterableStream(bs)));
   }

   public Iterable<P2<A, Integer>> zipIndex() {
      return wrap(Stream.iterableStream(this).zipIndex());
   }

   public java.util.List<A> toStandardList() {
      return new java.util.List<A>() {
         public int size() {
            return Stream.iterableStream(IterableW.this).length();
         }

         public boolean isEmpty() {
            return Stream.iterableStream(IterableW.this).isEmpty();
         }

         public boolean contains(Object o) {
            return Stream.iterableStream(IterableW.this).exists(Equal.anyEqual().eq(o));
         }

         public Iterator<A> iterator() {
            return IterableW.this.iterator();
         }

         public Object[] toArray() {
            return Array.iterableArray(Stream.iterableStream(IterableW.this)).array();
         }

         public <T> T[] toArray(T[] a) {
            return Stream.iterableStream(IterableW.this).toCollection().toArray(a);
         }

         public boolean add(A a) {
            return false;
         }

         public boolean remove(Object o) {
            return false;
         }

         public boolean containsAll(Collection<?> c) {
            return Stream.iterableStream(IterableW.this).toCollection().containsAll(c);
         }

         public boolean addAll(Collection<? extends A> c) {
            return false;
         }

         public boolean addAll(int index, Collection<? extends A> c) {
            return false;
         }

         public boolean removeAll(Collection<?> c) {
            return false;
         }

         public boolean retainAll(Collection<?> c) {
            return false;
         }

         public void clear() {
            throw new UnsupportedOperationException("Modifying an immutable List.");
         }

         public A get(int index) {
            return Stream.iterableStream(IterableW.this).index(index);
         }

         public A set(int index, A element) {
            throw new UnsupportedOperationException("Modifying an immutable List.");
         }

         public void add(int index, A element) {
            throw new UnsupportedOperationException("Modifying an immutable List.");
         }

         public A remove(int index) {
            throw new UnsupportedOperationException("Modifying an immutable List.");
         }

         public int indexOf(Object o) {
            int i = -1;
            Iterator var3 = IterableW.this.iterator();

            Object a;
            do {
               if (!var3.hasNext()) {
                  return i;
               }

               a = var3.next();
               ++i;
            } while(!a.equals(o));

            return i;
         }

         public int lastIndexOf(Object o) {
            int i = -1;
            int last = -1;
            Iterator var4 = IterableW.this.iterator();

            while(var4.hasNext()) {
               A a = var4.next();
               ++i;
               if (a.equals(o)) {
                  last = i;
               }
            }

            return last;
         }

         public ListIterator<A> listIterator() {
            return this.toListIterator(IterableW.this.toZipper());
         }

         public ListIterator<A> listIterator(int index) {
            return this.toListIterator(IterableW.this.toZipper().bind((F)Zipper.move().f(index)));
         }

         public java.util.List<A> subList(int fromIndex, int toIndex) {
            return IterableW.wrap(Stream.iterableStream(IterableW.this).drop(fromIndex).take(toIndex - fromIndex)).toStandardList();
         }

         private ListIterator<A> toListIterator(final Option<Zipper<A>> z) {
            return new ListIterator<A>() {
               private Option<Zipper<A>> pz = z;

               public boolean hasNext() {
                  return this.pz.isSome() && !((Zipper)this.pz.some()).atEnd();
               }

               public A next() {
                  if (this.pz.isSome()) {
                     this.pz = ((Zipper)this.pz.some()).next();
                     if (this.pz.isSome()) {
                        return ((Zipper)this.pz.some()).focus();
                     } else {
                        throw new NoSuchElementException();
                     }
                  } else {
                     throw new NoSuchElementException();
                  }
               }

               public boolean hasPrevious() {
                  return this.pz.isSome() && !((Zipper)this.pz.some()).atStart();
               }

               public A previous() {
                  this.pz = ((Zipper)this.pz.some()).previous();
                  return ((Zipper)this.pz.some()).focus();
               }

               public int nextIndex() {
                  return ((Zipper)this.pz.some()).index() + (((Zipper)this.pz.some()).atEnd() ? 0 : 1);
               }

               public int previousIndex() {
                  return ((Zipper)this.pz.some()).index() - (((Zipper)this.pz.some()).atStart() ? 0 : 1);
               }

               public void remove() {
                  throw new UnsupportedOperationException("Remove on immutable ListIterator");
               }

               public void set(A a) {
                  throw new UnsupportedOperationException("Set on immutable ListIterator");
               }

               public void add(A a) {
                  throw new UnsupportedOperationException("Add on immutable ListIterator");
               }
            };
         }
      };
   }

   public Option<Zipper<A>> toZipper() {
      return Zipper.fromStream(Stream.iterableStream(this));
   }
}
