package soot.util;

import java.util.AbstractMap;
import java.util.AbstractQueue;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Map.Entry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class PriorityQueue<E> extends AbstractQueue<E> {
   private static final Logger logger = LoggerFactory.getLogger(PriorityQueue.class);
   final List<? extends E> universe;
   final int N;
   int min = Integer.MAX_VALUE;
   private Map<E, Integer> ordinalMap;

   int getOrdinal(Object o) {
      if (o == null) {
         throw new NullPointerException();
      } else {
         Integer i = (Integer)this.ordinalMap.get(o);
         if (i == null) {
            throw new NoSuchElementException();
         } else {
            return i;
         }
      }
   }

   abstract void addAll();

   abstract int nextSetBit(int var1);

   abstract boolean remove(int var1);

   abstract boolean add(int var1);

   abstract boolean contains(int var1);

   public final E peek() {
      return this.isEmpty() ? null : this.universe.get(this.min);
   }

   public final E poll() {
      if (this.isEmpty()) {
         return null;
      } else {
         E e = this.universe.get(this.min);
         this.remove(this.min);
         return e;
      }
   }

   public final boolean add(E e) {
      return this.offer(e);
   }

   public final boolean offer(E e) {
      return this.add(this.getOrdinal(e));
   }

   public final boolean remove(Object o) {
      if (o != null && !this.isEmpty()) {
         try {
            if (o.equals(this.peek())) {
               this.remove(this.min);
               return true;
            } else {
               return this.remove(this.getOrdinal(o));
            }
         } catch (NoSuchElementException var3) {
            logger.debug("" + var3.getMessage());
            return false;
         }
      } else {
         return false;
      }
   }

   public final boolean contains(Object o) {
      if (o == null) {
         return false;
      } else {
         try {
            return o.equals(this.peek()) ? true : this.contains(this.getOrdinal(o));
         } catch (NoSuchElementException var3) {
            logger.debug("" + var3.getMessage());
            return false;
         }
      }
   }

   public boolean isEmpty() {
      return this.min >= this.N;
   }

   PriorityQueue(List<? extends E> universe, Map<E, Integer> ordinalMap) {
      assert ordinalMap.size() == universe.size();

      this.universe = universe;
      this.ordinalMap = ordinalMap;
      this.N = universe.size();
   }

   public static <E> PriorityQueue<E> of(E[] universe) {
      return of(Arrays.asList(universe));
   }

   public static <E> PriorityQueue<E> noneOf(E[] universe) {
      return noneOf(Arrays.asList(universe));
   }

   public static <E> PriorityQueue<E> of(List<? extends E> universe) {
      PriorityQueue<E> q = noneOf(universe);
      q.addAll();
      return q;
   }

   public static <E> PriorityQueue<E> noneOf(List<? extends E> universe) {
      Map<E, Integer> ordinalMap = new HashMap(2 * universe.size() / 3);
      int i = 0;
      Iterator var3 = universe.iterator();

      Object e;
      do {
         if (!var3.hasNext()) {
            return newPriorityQueue(universe, ordinalMap);
         }

         e = var3.next();
         if (e == null) {
            throw new NullPointerException("null is not allowed");
         }
      } while(ordinalMap.put(e, i++) == null);

      throw new IllegalArgumentException("duplicate key found");
   }

   public static <E extends Numberable> PriorityQueue<E> of(List<? extends E> universe, boolean useNumberInterface) {
      PriorityQueue<E> q = noneOf(universe, useNumberInterface);
      q.addAll();
      return q;
   }

   public static <E extends Numberable> PriorityQueue<E> noneOf(final List<? extends E> universe, boolean useNumberInterface) {
      if (!useNumberInterface) {
         return noneOf(universe);
      } else {
         int i = 0;
         Iterator var3 = universe.iterator();

         while(var3.hasNext()) {
            E e = (Numberable)var3.next();
            e.setNumber(i++);
         }

         return newPriorityQueue(universe, new AbstractMap<E, Integer>() {
            public Integer get(Object key) {
               return ((Numberable)key).getNumber();
            }

            public int size() {
               return universe.size();
            }

            public Set<Entry<E, Integer>> entrySet() {
               throw new UnsupportedOperationException();
            }
         });
      }
   }

   private static <E> PriorityQueue<E> newPriorityQueue(List<? extends E> universe, Map<E, Integer> ordinalMap) {
      if (universe.size() <= 64) {
         return new SmallPriorityQueue(universe, ordinalMap);
      } else {
         return (PriorityQueue)(universe.size() <= 4096 ? new MediumPriorityQueue(universe, ordinalMap) : new LargePriorityQueue(universe, ordinalMap));
      }
   }

   abstract class Itr implements Iterator<E> {
      long expected = this.getExpected();
      int next;
      int now;

      Itr() {
         this.next = PriorityQueue.this.min;
         this.now = Integer.MAX_VALUE;
      }

      abstract long getExpected();

      public boolean hasNext() {
         return this.next < PriorityQueue.this.N;
      }

      public E next() {
         if (this.expected != this.getExpected()) {
            throw new ConcurrentModificationException();
         } else if (this.next >= PriorityQueue.this.N) {
            throw new NoSuchElementException();
         } else {
            this.now = this.next;
            this.next = PriorityQueue.this.nextSetBit(this.next + 1);
            return PriorityQueue.this.universe.get(this.now);
         }
      }

      public void remove() {
         if (this.now >= PriorityQueue.this.N) {
            throw new IllegalStateException();
         } else if (this.expected != this.getExpected()) {
            throw new ConcurrentModificationException();
         } else {
            PriorityQueue.this.remove(this.now);
            this.expected = this.getExpected();
            this.now = Integer.MAX_VALUE;
         }
      }
   }
}
