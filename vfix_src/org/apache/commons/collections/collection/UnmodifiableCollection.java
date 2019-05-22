package org.apache.commons.collections.collection;

import java.util.Collection;
import java.util.Iterator;
import org.apache.commons.collections.Unmodifiable;
import org.apache.commons.collections.iterators.UnmodifiableIterator;

public final class UnmodifiableCollection extends AbstractSerializableCollectionDecorator implements Unmodifiable {
   private static final long serialVersionUID = -239892006883819945L;

   public static Collection decorate(Collection coll) {
      return (Collection)(coll instanceof Unmodifiable ? coll : new UnmodifiableCollection(coll));
   }

   private UnmodifiableCollection(Collection coll) {
      super(coll);
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
