package soot.jimple.toolkits.annotation.arraycheck;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class BoundedPriorityList implements Collection {
   protected final List fulllist;
   protected ArrayList worklist;

   public BoundedPriorityList(List list) {
      this.fulllist = list;
      this.worklist = new ArrayList(list);
   }

   public boolean isEmpty() {
      return this.worklist.isEmpty();
   }

   public Object removeFirst() {
      return this.worklist.remove(0);
   }

   public boolean add(Object toadd) {
      if (this.contains(toadd)) {
         return false;
      } else {
         int index = this.fulllist.indexOf(toadd);
         ListIterator worklistIter = this.worklist.listIterator();

         int tmpidx;
         do {
            if (!worklistIter.hasNext()) {
               return false;
            }

            Object tocomp = worklistIter.next();
            tmpidx = this.fulllist.indexOf(tocomp);
         } while(index >= tmpidx);

         worklistIter.add(toadd);
         return true;
      }
   }

   public boolean addAll(Collection c) {
      boolean addedSomething = false;

      Object o;
      for(Iterator iter = c.iterator(); iter.hasNext(); addedSomething |= this.add(o)) {
         o = iter.next();
      }

      return addedSomething;
   }

   public boolean addAll(int index, Collection c) {
      throw new RuntimeException("Not supported. You should use addAll(Collection) to keep priorities.");
   }

   public void clear() {
      this.worklist.clear();
   }

   public boolean contains(Object o) {
      return this.worklist.contains(o);
   }

   public boolean containsAll(Collection c) {
      return this.worklist.containsAll(c);
   }

   public Iterator iterator() {
      return this.worklist.iterator();
   }

   public boolean remove(Object o) {
      return this.worklist.remove(o);
   }

   public boolean removeAll(Collection c) {
      return this.worklist.removeAll(c);
   }

   public boolean retainAll(Collection c) {
      return this.worklist.retainAll(c);
   }

   public int size() {
      return this.worklist.size();
   }

   public Object[] toArray() {
      return this.worklist.toArray();
   }

   public Object[] toArray(Object[] a) {
      return this.worklist.toArray(a);
   }

   public String toString() {
      return this.worklist.toString();
   }

   public boolean equals(Object obj) {
      return this.worklist.equals(obj);
   }

   public int hashCode() {
      return this.worklist.hashCode();
   }
}
