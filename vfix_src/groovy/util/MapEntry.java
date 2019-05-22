package groovy.util;

import java.util.Map.Entry;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class MapEntry implements Entry {
   private Object key;
   private Object value;

   public MapEntry(Object key, Object value) {
      this.key = key;
      this.value = value;
   }

   public boolean equals(Object that) {
      return that instanceof MapEntry ? this.equals((MapEntry)that) : false;
   }

   public boolean equals(MapEntry that) {
      return DefaultTypeTransformation.compareEqual(this.key, that.key) && DefaultTypeTransformation.compareEqual(this.value, that.value);
   }

   public int hashCode() {
      return this.hash(this.key) ^ this.hash(this.value);
   }

   public String toString() {
      return "" + this.key + ":" + this.value;
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
      this.value = value;
      return value;
   }

   protected int hash(Object object) {
      return object == null ? '몾' : object.hashCode();
   }
}
