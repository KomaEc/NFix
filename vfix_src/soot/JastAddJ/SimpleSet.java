package soot.JastAddJ;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;

public interface SimpleSet {
   SimpleSet emptySet = new SimpleSet() {
      public int size() {
         return 0;
      }

      public boolean isEmpty() {
         return true;
      }

      public SimpleSet add(Object o) {
         return o instanceof SimpleSet ? (SimpleSet)o : (new SimpleSet.SimpleSetImpl()).add(o);
      }

      public boolean contains(Object o) {
         return false;
      }

      public Iterator iterator() {
         return Collections.EMPTY_LIST.iterator();
      }

      public boolean isSingleton() {
         return false;
      }

      public boolean isSingleton(Object o) {
         return false;
      }
   };
   SimpleSet fullSet = new SimpleSet() {
      public int size() {
         throw new Error("Operation size not supported on the full set");
      }

      public boolean isEmpty() {
         return false;
      }

      public SimpleSet add(Object o) {
         return this;
      }

      public boolean contains(Object o) {
         return true;
      }

      public Iterator iterator() {
         throw new Error("Operation iterator not support on the full set");
      }

      public boolean isSingleton() {
         return false;
      }

      public boolean isSingleton(Object o) {
         return false;
      }
   };

   int size();

   boolean isEmpty();

   SimpleSet add(Object var1);

   Iterator iterator();

   boolean contains(Object var1);

   boolean isSingleton();

   boolean isSingleton(Object var1);

   public static class SimpleSetImpl implements SimpleSet {
      private HashSet internalSet;

      public SimpleSetImpl() {
         this.internalSet = new HashSet(4);
      }

      public SimpleSetImpl(Collection c) {
         this.internalSet = new HashSet(c.size());
         this.internalSet.addAll(c);
      }

      private SimpleSetImpl(SimpleSet.SimpleSetImpl set) {
         this.internalSet = new HashSet(set.internalSet);
      }

      public int size() {
         return this.internalSet.size();
      }

      public boolean isEmpty() {
         return this.internalSet.isEmpty();
      }

      public SimpleSet add(Object o) {
         if (this.internalSet.contains(o)) {
            return this;
         } else {
            SimpleSet.SimpleSetImpl set = new SimpleSet.SimpleSetImpl(this);
            set.internalSet.add(o);
            return set;
         }
      }

      public Iterator iterator() {
         return this.internalSet.iterator();
      }

      public boolean contains(Object o) {
         return this.internalSet.contains(o);
      }

      public boolean isSingleton() {
         return this.internalSet.size() == 1;
      }

      public boolean isSingleton(Object o) {
         return this.isSingleton() && this.contains(o);
      }
   }
}
