package com.gzoltar.shaded.org.pitest.functional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public abstract class FArray {
   public static <T> void filter(T[] xs, F<T, Boolean> predicate, Collection<T> dest) {
      if (xs != null) {
         Object[] arr$ = xs;
         int len$ = xs.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            T x = arr$[i$];
            if ((Boolean)predicate.apply(x)) {
               dest.add(x);
            }
         }
      }

   }

   public static <T> List<T> filter(T[] xs, F<T, Boolean> predicate) {
      List<T> dest = new ArrayList();
      filter(xs, predicate, dest);
      return dest;
   }

   public static <T> boolean contains(T[] xs, F<T, Boolean> predicate) {
      Object[] arr$ = xs;
      int len$ = xs.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         T x = arr$[i$];
         if ((Boolean)predicate.apply(x)) {
            return true;
         }
      }

      return false;
   }

   public static <A, B> void flatMapTo(A[] as, F<A, ? extends Iterable<B>> f, Collection<? super B> bs) {
      if (as != null) {
         Object[] arr$ = as;
         int len$ = as.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            A a = arr$[i$];
            Iterator i$ = ((Iterable)f.apply(a)).iterator();

            while(i$.hasNext()) {
               B each = i$.next();
               bs.add(each);
            }
         }
      }

   }

   public static <A, B> FunctionalList<B> flatMap(A[] as, F<A, ? extends Iterable<B>> f) {
      FunctionalList<B> bs = emptyList();
      flatMapTo(as, f, bs);
      return bs;
   }

   private static <T> FunctionalList<T> emptyList() {
      return new MutableList();
   }
}
