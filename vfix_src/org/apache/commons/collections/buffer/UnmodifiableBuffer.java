package org.apache.commons.collections.buffer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import org.apache.commons.collections.Buffer;
import org.apache.commons.collections.Unmodifiable;
import org.apache.commons.collections.iterators.UnmodifiableIterator;

public final class UnmodifiableBuffer extends AbstractBufferDecorator implements Unmodifiable, Serializable {
   private static final long serialVersionUID = 1832948656215393357L;

   public static Buffer decorate(Buffer buffer) {
      return (Buffer)(buffer instanceof Unmodifiable ? buffer : new UnmodifiableBuffer(buffer));
   }

   private UnmodifiableBuffer(Buffer buffer) {
      super(buffer);
   }

   private void writeObject(ObjectOutputStream out) throws IOException {
      out.defaultWriteObject();
      out.writeObject(super.collection);
   }

   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
      in.defaultReadObject();
      super.collection = (Collection)in.readObject();
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

   public Object remove() {
      throw new UnsupportedOperationException();
   }
}
