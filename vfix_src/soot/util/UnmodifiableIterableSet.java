package soot.util;

import java.util.Iterator;

public class UnmodifiableIterableSet<E> extends IterableSet<E> {
   public UnmodifiableIterableSet() {
   }

   public UnmodifiableIterableSet(IterableSet<E> original) {
      Iterator var2 = original.iterator();

      while(var2.hasNext()) {
         E e = var2.next();
         super.add(e);
      }

   }

   public boolean add(E o) {
      throw new RuntimeException("This set cannot be modified");
   }

   public boolean remove(Object o) {
      throw new RuntimeException("This set cannot be modified");
   }

   public boolean forceRemove(Object o) {
      return super.remove(o);
   }
}
