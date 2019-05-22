package org.jboss.util.collection;

import java.lang.ref.ReferenceQueue;
import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import org.jboss.util.Objects;
import org.jboss.util.SoftObject;

public class CachedCollection extends AbstractCollection {
   protected final ReferenceQueue queue = new ReferenceQueue();
   protected final Collection collection;

   public CachedCollection(Collection collection) {
      this.collection = collection;
   }

   public Iterator iterator() {
      this.maintain();
      return new CachedCollection.MyIterator(this.collection.iterator());
   }

   public int size() {
      this.maintain();
      return this.collection.size();
   }

   public boolean add(Object obj) {
      this.maintain();
      SoftObject soft = SoftObject.create(obj, this.queue);
      return this.collection.add(soft);
   }

   private void maintain() {
      int count = 0;

      SoftObject obj;
      while((obj = (SoftObject)this.queue.poll()) != null) {
         ++count;
         this.collection.remove(obj);
      }

      if (count != 0) {
         System.err.println("vm reclaimed " + count + " objects");
      }

   }

   private final class MyIterator implements Iterator {
      private final Iterator iter;

      public MyIterator(Iterator iter) {
         this.iter = iter;
      }

      public boolean hasNext() {
         CachedCollection.this.maintain();
         return this.iter.hasNext();
      }

      private Object nextObject() {
         Object obj = this.iter.next();
         return Objects.deref(obj);
      }

      public Object next() {
         CachedCollection.this.maintain();
         return this.nextObject();
      }

      public void remove() {
         CachedCollection.this.maintain();
         this.iter.remove();
      }
   }
}
