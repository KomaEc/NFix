package org.apache.commons.collections.keyvalue;

import java.util.Map.Entry;
import org.apache.commons.collections.KeyValue;

public final class DefaultMapEntry extends AbstractMapEntry {
   public DefaultMapEntry(Object key, Object value) {
      super(key, value);
   }

   public DefaultMapEntry(KeyValue pair) {
      super(pair.getKey(), pair.getValue());
   }

   public DefaultMapEntry(Entry entry) {
      super(entry.getKey(), entry.getValue());
   }
}
