package org.apache.tools.ant.types.resources;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;
import java.util.TreeMap;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.DataType;
import org.apache.tools.ant.types.ResourceCollection;
import org.apache.tools.ant.types.resources.comparators.DelegatedResourceComparator;
import org.apache.tools.ant.types.resources.comparators.ResourceComparator;

public class Sort extends BaseResourceCollectionWrapper {
   private DelegatedResourceComparator comp = new DelegatedResourceComparator();

   protected synchronized Collection getCollection() {
      ResourceCollection rc = this.getResourceCollection();
      Iterator iter = rc.iterator();
      if (!iter.hasNext()) {
         return Collections.EMPTY_SET;
      } else {
         Sort.SortedBag b = new Sort.SortedBag(this.comp);

         while(iter.hasNext()) {
            b.add(iter.next());
         }

         return b;
      }
   }

   public synchronized void add(ResourceComparator c) {
      if (this.isReference()) {
         throw this.noChildrenAllowed();
      } else {
         this.comp.add(c);
         FailFast.invalidate(this);
      }
   }

   protected synchronized void dieOnCircularReference(Stack stk, Project p) throws BuildException {
      if (!this.isChecked()) {
         if (this.isReference()) {
            super.dieOnCircularReference(stk, p);
         } else {
            DataType.invokeCircularReferenceCheck(this.comp, stk, p);
            this.setChecked(true);
         }

      }
   }

   private static class SortedBag extends AbstractCollection {
      private TreeMap t;
      private int size;

      SortedBag(Comparator c) {
         this.t = new TreeMap(c);
      }

      public synchronized Iterator iterator() {
         return new Sort.SortedBag.MyIterator();
      }

      public synchronized boolean add(Object o) {
         if (this.size < Integer.MAX_VALUE) {
            ++this.size;
         }

         Sort.SortedBag.MutableInt m = (Sort.SortedBag.MutableInt)((Sort.SortedBag.MutableInt)this.t.get(o));
         if (m == null) {
            m = new Sort.SortedBag.MutableInt();
            this.t.put(o, m);
         }

         m.value++;
         return true;
      }

      public synchronized int size() {
         return this.size;
      }

      private class MyIterator implements Iterator {
         private Iterator keyIter;
         private Object current;
         private int occurrence;

         private MyIterator() {
            this.keyIter = SortedBag.this.t.keySet().iterator();
         }

         public synchronized boolean hasNext() {
            return this.occurrence > 0 || this.keyIter.hasNext();
         }

         public synchronized Object next() {
            if (!this.hasNext()) {
               throw new NoSuchElementException();
            } else {
               if (this.occurrence == 0) {
                  this.current = this.keyIter.next();
                  this.occurrence = ((Sort.SortedBag.MutableInt)SortedBag.this.t.get(this.current)).value;
               }

               --this.occurrence;
               return this.current;
            }
         }

         public void remove() {
            throw new UnsupportedOperationException();
         }

         // $FF: synthetic method
         MyIterator(Object x1) {
            this();
         }
      }

      private class MutableInt {
         private int value;

         private MutableInt() {
            this.value = 0;
         }

         // $FF: synthetic method
         MutableInt(Object x1) {
            this();
         }
      }
   }
}
