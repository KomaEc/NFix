package polyglot.util;

import java.util.ListIterator;

public class TypedListIterator implements ListIterator {
   private Class allowed_type;
   private boolean immutable;
   private ListIterator backing_iterator;

   public TypedListIterator(ListIterator iter, Class c, boolean immutable) {
      this.immutable = immutable;
      this.allowed_type = c;
      this.backing_iterator = iter;
   }

   public Class getAllowedType() {
      return this.allowed_type;
   }

   public void add(Object o) {
      this.tryIns(o);
      this.backing_iterator.add(o);
   }

   public void set(Object o) {
      this.tryIns(o);
      this.backing_iterator.set(o);
   }

   public boolean hasNext() {
      return this.backing_iterator.hasNext();
   }

   public boolean hasPrevious() {
      return this.backing_iterator.hasPrevious();
   }

   public Object next() {
      return this.backing_iterator.next();
   }

   public int nextIndex() {
      return this.backing_iterator.nextIndex();
   }

   public Object previous() {
      return this.backing_iterator.previous();
   }

   public int previousIndex() {
      return this.backing_iterator.previousIndex();
   }

   public void remove() {
      if (this.immutable) {
         throw new UnsupportedOperationException("Remove from an immutable TypedListIterator");
      } else {
         this.backing_iterator.remove();
      }
   }

   private final void tryIns(Object o) {
      if (this.immutable) {
         throw new UnsupportedOperationException("Add to an immutable TypedListIterator");
      } else if (this.allowed_type != null && !this.allowed_type.isAssignableFrom(o.getClass())) {
         String why = "Tried to add a " + o.getClass().getName() + " to a list of type " + this.allowed_type.getName();
         throw new UnsupportedOperationException(why);
      }
   }
}
