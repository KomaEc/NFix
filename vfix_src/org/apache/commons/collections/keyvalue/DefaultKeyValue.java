package org.apache.commons.collections.keyvalue;

import java.util.Map.Entry;
import org.apache.commons.collections.KeyValue;

public class DefaultKeyValue extends AbstractKeyValue {
   public DefaultKeyValue() {
      super((Object)null, (Object)null);
   }

   public DefaultKeyValue(Object key, Object value) {
      super(key, value);
   }

   public DefaultKeyValue(KeyValue pair) {
      super(pair.getKey(), pair.getValue());
   }

   public DefaultKeyValue(Entry entry) {
      super(entry.getKey(), entry.getValue());
   }

   public Object setKey(Object key) {
      if (key == this) {
         throw new IllegalArgumentException("DefaultKeyValue may not contain itself as a key.");
      } else {
         Object old = super.key;
         super.key = key;
         return old;
      }
   }

   public Object setValue(Object value) {
      if (value == this) {
         throw new IllegalArgumentException("DefaultKeyValue may not contain itself as a value.");
      } else {
         Object old = super.value;
         super.value = value;
         return old;
      }
   }

   public Entry toMapEntry() {
      return new DefaultMapEntry(this);
   }

   public boolean equals(Object obj) {
      if (obj == this) {
         return true;
      } else if (!(obj instanceof DefaultKeyValue)) {
         return false;
      } else {
         DefaultKeyValue other = (DefaultKeyValue)obj;
         return (this.getKey() == null ? other.getKey() == null : this.getKey().equals(other.getKey())) && (this.getValue() == null ? other.getValue() == null : this.getValue().equals(other.getValue()));
      }
   }

   public int hashCode() {
      return (this.getKey() == null ? 0 : this.getKey().hashCode()) ^ (this.getValue() == null ? 0 : this.getValue().hashCode());
   }
}
