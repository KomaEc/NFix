package org.apache.commons.collections.set;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import org.apache.commons.collections.Unmodifiable;
import org.apache.commons.collections.iterators.UnmodifiableIterator;

public final class UnmodifiableSet extends AbstractSerializableSetDecorator implements Unmodifiable {
   private static final long serialVersionUID = 6499119872185240161L;

   public static Set decorate(Set set) {
      return (Set)(set instanceof Unmodifiable ? set : new UnmodifiableSet(set));
   }

   private UnmodifiableSet(Set set) {
      super(set);
   }

   public Iterator iterator() {
      return UnmodifiableIterator.decorate(this.getCollection().iterator());
   }

   public boolean add(Object object) {
      throw new UnsupportedOperationException();
   }

   public boolean addAll(Collection coll) {
      throw new UnsupportedOperationException();
   }

   public void clear() {
      throw new UnsupportedOperationException();
   }

   public boolean remove(Object object) {
      throw new UnsupportedOperationException();
   }

   public boolean removeAll(Collection coll) {
      throw new UnsupportedOperationException();
   }

   public boolean retainAll(Collection coll) {
      throw new UnsupportedOperationException();
   }
}
