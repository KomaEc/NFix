package org.apache.commons.collections.map;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Map;

public class CaseInsensitiveMap extends AbstractHashedMap implements Serializable, Cloneable {
   private static final long serialVersionUID = -7074655917369299456L;

   public CaseInsensitiveMap() {
      super(16, 0.75F, 12);
   }

   public CaseInsensitiveMap(int initialCapacity) {
      super(initialCapacity);
   }

   public CaseInsensitiveMap(int initialCapacity, float loadFactor) {
      super(initialCapacity, loadFactor);
   }

   public CaseInsensitiveMap(Map map) {
      super(map);
   }

   protected Object convertKey(Object key) {
      return key != null ? key.toString().toLowerCase() : AbstractHashedMap.NULL;
   }

   public Object clone() {
      return super.clone();
   }

   private void writeObject(ObjectOutputStream out) throws IOException {
      out.defaultWriteObject();
      this.doWriteObject(out);
   }

   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
      in.defaultReadObject();
      this.doReadObject(in);
   }
}
