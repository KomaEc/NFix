package org.apache.commons.collections.keyvalue;

import java.util.Map.Entry;

public abstract class AbstractMapEntry extends AbstractKeyValue implements Entry {
   protected AbstractMapEntry(Object key, Object value) {
      super(key, value);
   }

   public Object setValue(Object value) {
      Object answer = super.value;
      super.value = value;
      return answer;
   }

   public boolean equals(Object obj) {
      if (obj == this) {
         return true;
      } else if (!(obj instanceof Entry)) {
         return false;
      } else {
         Entry other = (Entry)obj;
         return (this.getKey() == null ? other.getKey() == null : this.getKey().equals(other.getKey())) && (this.getValue() == null ? other.getValue() == null : this.getValue().equals(other.getValue()));
      }
   }

   public int hashCode() {
      return (this.getKey() == null ? 0 : this.getKey().hashCode()) ^ (this.getValue() == null ? 0 : this.getValue().hashCode());
   }
}
