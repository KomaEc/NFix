package org.apache.commons.collections.iterators;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import org.apache.commons.collections.list.UnmodifiableList;

public class CollatingIterator implements Iterator {
   private Comparator comparator;
   private ArrayList iterators;
   private ArrayList values;
   private BitSet valueSet;
   private int lastReturned;

   public CollatingIterator() {
      this((Comparator)null, 2);
   }

   public CollatingIterator(Comparator comp) {
      this(comp, 2);
   }

   public CollatingIterator(Comparator comp, int initIterCapacity) {
      this.comparator = null;
      this.iterators = null;
      this.values = null;
      this.valueSet = null;
      this.lastReturned = -1;
      this.iterators = new ArrayList(initIterCapacity);
      this.setComparator(comp);
   }

   public CollatingIterator(Comparator comp, Iterator a, Iterator b) {
      this(comp, 2);
      this.addIterator(a);
      this.addIterator(b);
   }

   public CollatingIterator(Comparator comp, Iterator[] iterators) {
      this(comp, iterators.length);

      for(int i = 0; i < iterators.length; ++i) {
         this.addIterator(iterators[i]);
      }

   }

   public CollatingIterator(Comparator comp, Collection iterators) {
      this(comp, iterators.size());
      Iterator it = iterators.iterator();

      while(it.hasNext()) {
         Iterator item = (Iterator)it.next();
         this.addIterator(item);
      }

   }

   public void addIterator(Iterator iterator) {
      this.checkNotStarted();
      if (iterator == null) {
         throw new NullPointerException("Iterator must not be null");
      } else {
         this.iterators.add(iterator);
      }
   }

   public void setIterator(int index, Iterator iterator) {
      this.checkNotStarted();
      if (iterator == null) {
         throw new NullPointerException("Iterator must not be null");
      } else {
         this.iterators.set(index, iterator);
      }
   }

   public List getIterators() {
      return UnmodifiableList.decorate(this.iterators);
   }

   public Comparator getComparator() {
      return this.comparator;
   }

   public void setComparator(Comparator comp) {
      this.checkNotStarted();
      this.comparator = comp;
   }

   public boolean hasNext() {
      this.start();
      return this.anyValueSet(this.valueSet) || this.anyHasNext(this.iterators);
   }

   public Object next() throws NoSuchElementException {
      if (!this.hasNext()) {
         throw new NoSuchElementException();
      } else {
         int leastIndex = this.least();
         if (leastIndex == -1) {
            throw new NoSuchElementException();
         } else {
            Object val = this.values.get(leastIndex);
            this.clear(leastIndex);
            this.lastReturned = leastIndex;
            return val;
         }
      }
   }

   public void remove() {
      if (this.lastReturned == -1) {
         throw new IllegalStateException("No value can be removed at present");
      } else {
         Iterator it = (Iterator)this.iterators.get(this.lastReturned);
         it.remove();
      }
   }

   private void start() {
      if (this.values == null) {
         this.values = new ArrayList(this.iterators.size());
         this.valueSet = new BitSet(this.iterators.size());

         for(int i = 0; i < this.iterators.size(); ++i) {
            this.values.add((Object)null);
            this.valueSet.clear(i);
         }
      }

   }

   private boolean set(int i) {
      Iterator it = (Iterator)this.iterators.get(i);
      if (it.hasNext()) {
         this.values.set(i, it.next());
         this.valueSet.set(i);
         return true;
      } else {
         this.values.set(i, (Object)null);
         this.valueSet.clear(i);
         return false;
      }
   }

   private void clear(int i) {
      this.values.set(i, (Object)null);
      this.valueSet.clear(i);
   }

   private void checkNotStarted() throws IllegalStateException {
      if (this.values != null) {
         throw new IllegalStateException("Can't do that after next or hasNext has been called.");
      }
   }

   private int least() {
      int leastIndex = -1;
      Object leastObject = null;

      for(int i = 0; i < this.values.size(); ++i) {
         if (!this.valueSet.get(i)) {
            this.set(i);
         }

         if (this.valueSet.get(i)) {
            if (leastIndex == -1) {
               leastIndex = i;
               leastObject = this.values.get(i);
            } else {
               Object curObject = this.values.get(i);
               if (this.comparator.compare(curObject, leastObject) < 0) {
                  leastObject = curObject;
                  leastIndex = i;
               }
            }
         }
      }

      return leastIndex;
   }

   private boolean anyValueSet(BitSet set) {
      for(int i = 0; i < set.size(); ++i) {
         if (set.get(i)) {
            return true;
         }
      }

      return false;
   }

   private boolean anyHasNext(ArrayList iters) {
      for(int i = 0; i < iters.size(); ++i) {
         Iterator it = (Iterator)iters.get(i);
         if (it.hasNext()) {
            return true;
         }
      }

      return false;
   }
}
