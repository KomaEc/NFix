package bsh.collection;

import bsh.BshIterator;
import java.util.Collection;
import java.util.Iterator;

public class CollectionIterator implements BshIterator {
   private Iterator iterator;

   public CollectionIterator(Object var1) {
      this.iterator = this.createIterator(var1);
   }

   protected Iterator createIterator(Object var1) {
      if (var1 == null) {
         throw new NullPointerException("Object arguments passed to the CollectionIterator constructor cannot be null.");
      } else if (var1 instanceof Iterator) {
         return (Iterator)var1;
      } else if (var1 instanceof Collection) {
         return ((Collection)var1).iterator();
      } else {
         throw new IllegalArgumentException("Cannot enumerate object of type " + var1.getClass());
      }
   }

   public Object next() {
      return this.iterator.next();
   }

   public boolean hasNext() {
      return this.iterator.hasNext();
   }
}
