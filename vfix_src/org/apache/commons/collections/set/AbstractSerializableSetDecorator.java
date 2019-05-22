package org.apache.commons.collections.set;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

public abstract class AbstractSerializableSetDecorator extends AbstractSetDecorator implements Serializable {
   private static final long serialVersionUID = 1229469966212206107L;

   protected AbstractSerializableSetDecorator(Set set) {
      super(set);
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
