package org.apache.commons.collections;

import java.util.Map.Entry;

/** @deprecated */
public class DefaultMapEntry implements Entry, KeyValue {
   private Object key;
   private Object value;

   public DefaultMapEntry() {
   }

   public DefaultMapEntry(Entry entry) {
      this.key = entry.getKey();
      this.value = entry.getValue();
   }

   public DefaultMapEntry(Object key, Object value) {
      this.key = key;
      this.value = value;
   }

   public Object getKey() {
      return this.key;
   }

   public void setKey(Object key) {
      this.key = key;
   }

   public Object getValue() {
      return this.value;
   }

   public Object setValue(Object value) {
      Object answer = this.value;
      this.value = value;
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

   public String toString() {
      return "" + this.getKey() + "=" + this.getValue();
   }
}
