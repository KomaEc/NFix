package org.apache.commons.collections.collection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;

public abstract class AbstractSerializableCollectionDecorator extends AbstractCollectionDecorator implements Serializable {
   private static final long serialVersionUID = 6249888059822088500L;

   protected AbstractSerializableCollectionDecorator(Collection coll) {
      super(coll);
   }

   private void writeObject(ObjectOutputStream out) throws IOException {
      out.defaultWriteObject();
      out.writeObject(super.collection);
   }

   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
      in.defaultReadObject();
      super.collection = (Collection)in.readObject();
   }
}
