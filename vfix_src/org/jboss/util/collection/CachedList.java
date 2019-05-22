package org.jboss.util.collection;

import java.lang.ref.ReferenceQueue;
import java.util.AbstractList;
import java.util.LinkedList;
import java.util.List;
import org.jboss.util.Objects;
import org.jboss.util.SoftObject;

public class CachedList extends AbstractList {
   protected final ReferenceQueue queue;
   protected final List list;

   public CachedList(List list) {
      this.queue = new ReferenceQueue();
      this.list = list;
   }

   public CachedList() {
      this(new LinkedList());
   }

   private Object getObject(int index) {
      Object obj = this.list.get(index);
      return Objects.deref(obj);
   }

   public Object get(int index) {
      this.maintain();
      return this.getObject(index);
   }

   public int size() {
      this.maintain();
      return this.list.size();
   }

   public Object set(int index, Object obj) {
      this.maintain();
      SoftObject soft = SoftObject.create(obj, this.queue);
      soft = (SoftObject)this.list.set(index, soft);
      return Objects.deref(soft);
   }

   public void add(int index, Object obj) {
      this.maintain();
      SoftObject soft = SoftObject.create(obj, this.queue);
      this.list.add(index, soft);
   }

   public Object remove(int index) {
      this.maintain();
      Object obj = this.list.remove(index);
      return Objects.deref(obj);
   }

   private void maintain() {
      int count = 0;

      SoftObject obj;
      while((obj = (SoftObject)this.queue.poll()) != null) {
         ++count;
         this.list.remove(obj);
      }

      if (count != 0) {
         System.err.println("vm reclaimed " + count + " objects");
      }

   }
}
