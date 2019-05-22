package org.jboss.util.collection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.NoSuchElementException;

public class SerializableEnumeration extends ArrayList implements Enumeration {
   private static final long serialVersionUID = 8678951571196067510L;
   private int index = 0;

   public SerializableEnumeration() {
   }

   public SerializableEnumeration(Collection c) {
      super(c);
   }

   public SerializableEnumeration(int initialCapacity) {
      super(initialCapacity);
   }

   public boolean hasMoreElements() {
      return this.index < this.size();
   }

   public Object nextElement() throws NoSuchElementException {
      try {
         Object nextObj = this.get(this.index);
         ++this.index;
         return nextObj;
      } catch (IndexOutOfBoundsException var2) {
         throw new NoSuchElementException();
      }
   }

   private void writeObject(ObjectOutputStream out) throws IOException {
      out.defaultWriteObject();
   }

   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
      in.defaultReadObject();
   }
}
