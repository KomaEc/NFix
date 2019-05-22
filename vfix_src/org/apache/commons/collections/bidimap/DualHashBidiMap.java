package org.apache.commons.collections.bidimap;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.collections.BidiMap;

public class DualHashBidiMap extends AbstractDualBidiMap implements Serializable {
   private static final long serialVersionUID = 721969328361808L;

   public DualHashBidiMap() {
      super(new HashMap(), new HashMap());
   }

   public DualHashBidiMap(Map map) {
      super(new HashMap(), new HashMap());
      this.putAll(map);
   }

   protected DualHashBidiMap(Map normalMap, Map reverseMap, BidiMap inverseBidiMap) {
      super(normalMap, reverseMap, inverseBidiMap);
   }

   protected BidiMap createBidiMap(Map normalMap, Map reverseMap, BidiMap inverseBidiMap) {
      return new DualHashBidiMap(normalMap, reverseMap, inverseBidiMap);
   }

   private void writeObject(ObjectOutputStream out) throws IOException {
      out.defaultWriteObject();
      out.writeObject(super.maps[0]);
   }

   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
      in.defaultReadObject();
      super.maps[0] = new HashMap();
      super.maps[1] = new HashMap();
      Map map = (Map)in.readObject();
      this.putAll(map);
   }
}
