package org.apache.commons.collections.set;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.SortedSet;
import org.apache.commons.collections.Unmodifiable;
import org.apache.commons.collections.iterators.UnmodifiableIterator;

public final class UnmodifiableSortedSet extends AbstractSortedSetDecorator implements Unmodifiable, Serializable {
   private static final long serialVersionUID = -725356885467962424L;

   public static SortedSet decorate(SortedSet set) {
      return (SortedSet)(set instanceof Unmodifiable ? set : new UnmodifiableSortedSet(set));
   }

   private void writeObject(ObjectOutputStream out) throws IOException {
      out.defaultWriteObject();
      out.writeObject(super.collection);
   }

   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
      in.defaultReadObject();
      super.collection = (Collection)in.readObject();
   }

   private UnmodifiableSortedSet(SortedSet set) {
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

   public SortedSet subSet(Object fromElement, Object toElement) {
      SortedSet sub = this.getSortedSet().subSet(fromElement, toElement);
      return new UnmodifiableSortedSet(sub);
   }

   public SortedSet headSet(Object toElement) {
      SortedSet sub = this.getSortedSet().headSet(toElement);
      return new UnmodifiableSortedSet(sub);
   }

   public SortedSet tailSet(Object fromElement) {
      SortedSet sub = this.getSortedSet().tailSet(fromElement);
      return new UnmodifiableSortedSet(sub);
   }
}
