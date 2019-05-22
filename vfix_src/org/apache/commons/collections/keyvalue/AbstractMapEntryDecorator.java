package org.apache.commons.collections.keyvalue;

import java.util.Map.Entry;
import org.apache.commons.collections.KeyValue;

public abstract class AbstractMapEntryDecorator implements Entry, KeyValue {
   protected final Entry entry;

   public AbstractMapEntryDecorator(Entry entry) {
      if (entry == null) {
         throw new IllegalArgumentException("Map Entry must not be null");
      } else {
         this.entry = entry;
      }
   }

   protected Entry getMapEntry() {
      return this.entry;
   }

   public Object getKey() {
      return this.entry.getKey();
   }

   public Object getValue() {
      return this.entry.getValue();
   }

   public Object setValue(Object object) {
      return this.entry.setValue(object);
   }

   public boolean equals(Object object) {
      return object == this ? true : this.entry.equals(object);
   }

   public int hashCode() {
      return this.entry.hashCode();
   }

   public String toString() {
      return this.entry.toString();
   }
}
