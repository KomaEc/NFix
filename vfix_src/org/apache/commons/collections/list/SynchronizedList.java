package org.apache.commons.collections.list;

import java.util.Collection;
import java.util.List;
import java.util.ListIterator;
import org.apache.commons.collections.collection.SynchronizedCollection;

public class SynchronizedList extends SynchronizedCollection implements List {
   private static final long serialVersionUID = -1403835447328619437L;

   public static List decorate(List list) {
      return new SynchronizedList(list);
   }

   protected SynchronizedList(List list) {
      super(list);
   }

   protected SynchronizedList(List list, Object lock) {
      super(list, lock);
   }

   protected List getList() {
      return (List)super.collection;
   }

   public void add(int index, Object object) {
      Object var3 = super.lock;
      synchronized(var3) {
         this.getList().add(index, object);
      }
   }

   public boolean addAll(int index, Collection coll) {
      Object var3 = super.lock;
      synchronized(var3) {
         boolean var4 = this.getList().addAll(index, coll);
         return var4;
      }
   }

   public Object get(int index) {
      Object var2 = super.lock;
      synchronized(var2) {
         Object var3 = this.getList().get(index);
         return var3;
      }
   }

   public int indexOf(Object object) {
      Object var2 = super.lock;
      synchronized(var2) {
         int var3 = this.getList().indexOf(object);
         return var3;
      }
   }

   public int lastIndexOf(Object object) {
      Object var2 = super.lock;
      synchronized(var2) {
         int var3 = this.getList().lastIndexOf(object);
         return var3;
      }
   }

   public ListIterator listIterator() {
      return this.getList().listIterator();
   }

   public ListIterator listIterator(int index) {
      return this.getList().listIterator(index);
   }

   public Object remove(int index) {
      Object var2 = super.lock;
      synchronized(var2) {
         Object var3 = this.getList().remove(index);
         return var3;
      }
   }

   public Object set(int index, Object object) {
      Object var3 = super.lock;
      synchronized(var3) {
         Object var4 = this.getList().set(index, object);
         return var4;
      }
   }

   public List subList(int fromIndex, int toIndex) {
      Object var3 = super.lock;
      synchronized(var3) {
         List list = this.getList().subList(fromIndex, toIndex);
         SynchronizedList var5 = new SynchronizedList(list, super.lock);
         return var5;
      }
   }
}
