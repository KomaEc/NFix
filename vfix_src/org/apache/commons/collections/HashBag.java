package org.apache.commons.collections;

import java.util.Collection;
import java.util.HashMap;

/** @deprecated */
public class HashBag extends DefaultMapBag implements Bag {
   public HashBag() {
      super(new HashMap());
   }

   public HashBag(Collection coll) {
      this();
      this.addAll(coll);
   }
}
