package org.apache.commons.collections.bag;

import java.util.Set;
import org.apache.commons.collections.Bag;
import org.apache.commons.collections.collection.SynchronizedCollection;
import org.apache.commons.collections.set.SynchronizedSet;

public class SynchronizedBag extends SynchronizedCollection implements Bag {
   private static final long serialVersionUID = 8084674570753837109L;

   public static Bag decorate(Bag bag) {
      return new SynchronizedBag(bag);
   }

   protected SynchronizedBag(Bag bag) {
      super(bag);
   }

   protected SynchronizedBag(Bag bag, Object lock) {
      super(bag, lock);
   }

   protected Bag getBag() {
      return (Bag)super.collection;
   }

   public boolean add(Object object, int count) {
      Object var3 = super.lock;
      synchronized(var3) {
         boolean var4 = this.getBag().add(object, count);
         return var4;
      }
   }

   public boolean remove(Object object, int count) {
      Object var3 = super.lock;
      synchronized(var3) {
         boolean var4 = this.getBag().remove(object, count);
         return var4;
      }
   }

   public Set uniqueSet() {
      Object var1 = super.lock;
      synchronized(var1) {
         Set set = this.getBag().uniqueSet();
         SynchronizedBag.SynchronizedBagSet var3 = new SynchronizedBag.SynchronizedBagSet(set, super.lock);
         return var3;
      }
   }

   public int getCount(Object object) {
      Object var2 = super.lock;
      synchronized(var2) {
         int var3 = this.getBag().getCount(object);
         return var3;
      }
   }

   class SynchronizedBagSet extends SynchronizedSet {
      SynchronizedBagSet(Set set, Object lock) {
         super(set, lock);
      }
   }
}
