package org.jboss.util.collection;

import java.lang.ref.ReferenceQueue;
import java.util.AbstractSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import org.jboss.util.NullArgumentException;
import org.jboss.util.WeakObject;

public class WeakSet extends AbstractSet implements Set {
   protected final ReferenceQueue queue;
   protected final Set set;

   public WeakSet(Set set) {
      this.queue = new ReferenceQueue();
      if (set == null) {
         throw new NullArgumentException("set");
      } else {
         if (set.size() != 0) {
            Object[] elements = set.toArray();
            set.clear();

            for(int i = 0; i < elements.length; ++i) {
               this.add(elements[i]);
            }
         }

         this.set = set;
      }
   }

   public WeakSet() {
      this(new HashSet());
   }

   protected final void maintain() {
      WeakObject weak;
      while((weak = (WeakObject)this.queue.poll()) != null) {
         this.set.remove(weak);
      }

   }

   public int size() {
      this.maintain();
      return this.set.size();
   }

   public Iterator iterator() {
      return new Iterator() {
         Iterator iter;
         Object UNKNOWN;
         Object next;

         {
            this.iter = WeakSet.this.set.iterator();
            this.UNKNOWN = new Object();
            this.next = this.UNKNOWN;
         }

         public boolean hasNext() {
            if (this.next != this.UNKNOWN) {
               return true;
            } else {
               WeakObject weak;
               Object obj;
               do {
                  if (!this.iter.hasNext()) {
                     return false;
                  }

                  weak = (WeakObject)this.iter.next();
                  obj = null;
               } while(weak != null && (obj = weak.get()) == null);

               this.next = obj;
               return true;
            }
         }

         public Object next() {
            if (this.next == this.UNKNOWN && !this.hasNext()) {
               throw new NoSuchElementException();
            } else {
               Object obj = this.next;
               this.next = this.UNKNOWN;
               return obj;
            }
         }

         public void remove() {
            this.iter.remove();
         }
      };
   }

   public boolean add(Object obj) {
      this.maintain();
      return this.set.add(WeakObject.create(obj, this.queue));
   }

   public boolean isEmpty() {
      this.maintain();
      return this.set.isEmpty();
   }

   public boolean contains(Object obj) {
      this.maintain();
      return this.set.contains(WeakObject.create(obj));
   }

   public boolean remove(Object obj) {
      this.maintain();
      return this.set.remove(WeakObject.create(obj));
   }

   public void clear() {
      this.set.clear();
   }

   public Object clone() {
      this.maintain();

      try {
         return super.clone();
      } catch (CloneNotSupportedException var2) {
         throw new InternalError();
      }
   }
}
