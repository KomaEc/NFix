package com.gzoltar.shaded.org.pitest.functional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public abstract class FCollection {
   public static <A> void forEach(Iterable<? extends A> as, SideEffect1<A> e) {
      Iterator i$ = as.iterator();

      while(i$.hasNext()) {
         A a = i$.next();
         e.apply(a);
      }

   }

   public static <A, B> void mapTo(Iterable<? extends A> as, F<A, B> f, Collection<? super B> bs) {
      if (as != null) {
         Iterator i$ = as.iterator();

         while(i$.hasNext()) {
            A a = i$.next();
            bs.add(f.apply(a));
         }
      }

   }

   public static <A, B> FunctionalList<B> map(Iterable<? extends A> as, F<A, B> f) {
      FunctionalList<B> bs = emptyList();
      mapTo(as, f, bs);
      return bs;
   }

   public static <A, B> void flatMapTo(Iterable<? extends A> as, F<A, ? extends Iterable<B>> f, Collection<? super B> bs) {
      if (as != null) {
         Iterator i$ = as.iterator();

         while(i$.hasNext()) {
            A a = i$.next();
            Iterator i$ = ((Iterable)f.apply(a)).iterator();

            while(i$.hasNext()) {
               B each = i$.next();
               bs.add(each);
            }
         }
      }

   }

   public static <A, B> FunctionalList<B> flatMap(Iterable<? extends A> as, F<A, ? extends Iterable<B>> f) {
      FunctionalList<B> bs = emptyList();
      flatMapTo(as, f, bs);
      return bs;
   }

   private static <T> FunctionalList<T> emptyList() {
      return new MutableList();
   }

   public static <T> FunctionalList<T> filter(Iterable<? extends T> xs, F<T, Boolean> predicate) {
      FunctionalList<T> dest = emptyList();
      filter(xs, predicate, dest);
      return dest;
   }

   public static <T> void filter(Iterable<? extends T> xs, F<T, Boolean> predicate, Collection<? super T> dest) {
      Iterator i$ = xs.iterator();

      while(i$.hasNext()) {
         T x = i$.next();
         if ((Boolean)predicate.apply(x)) {
            dest.add(x);
         }
      }

   }

   public static <T> boolean contains(Iterable<? extends T> xs, F<T, Boolean> predicate) {
      Iterator i$ = xs.iterator();

      Object x;
      do {
         if (!i$.hasNext()) {
            return false;
         }

         x = i$.next();
      } while(!(Boolean)predicate.apply(x));

      return true;
   }

   public static <A, B> A fold(F2<A, B, A> f, A z, Iterable<? extends B> xs) {
      A p = z;

      Object x;
      for(Iterator i$ = xs.iterator(); i$.hasNext(); p = f.apply(p, x)) {
         x = i$.next();
      }

      return p;
   }

   public static <T> FunctionalCollection<T> flatten(Iterable<? extends Iterable<? extends T>> ts) {
      MutableList<T> list = new MutableList();
      Iterator i$ = ts.iterator();

      while(i$.hasNext()) {
         Iterable<? extends T> it = (Iterable)i$.next();
         Iterator i$ = it.iterator();

         while(i$.hasNext()) {
            T each = i$.next();
            list.add(each);
         }
      }

      return list;
   }

   public static <T> FunctionalList<List<T>> splitToLength(int targetLength, Iterable<T> ts) {
      FunctionalList<List<T>> list = new MutableList();
      List<T> temp = new ArrayList();
      int i = 0;

      for(Iterator i$ = ts.iterator(); i$.hasNext(); ++i) {
         T each = i$.next();
         if (i == targetLength) {
            list.add(temp);
            temp = new ArrayList();
            i = 0;
         }

         temp.add(each);
      }

      if (!temp.isEmpty()) {
         list.add(temp);
      }

      return list;
   }

   public static <A, B> Map<A, Collection<B>> bucket(Iterable<B> bs, F<B, A> f) {
      Map<A, Collection<B>> bucketed = new HashMap();

      Object each;
      Object existing;
      for(Iterator i$ = bs.iterator(); i$.hasNext(); ((Collection)existing).add(each)) {
         each = i$.next();
         A key = f.apply(each);
         existing = (Collection)bucketed.get(key);
         if (existing == null) {
            existing = new ArrayList();
            bucketed.put(key, existing);
         }
      }

      return bucketed;
   }
}
