package groovy.lang;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class SpreadMap extends HashMap {
   private Map mapData;
   private int hashCode;

   public SpreadMap(Object[] values) {
      this.mapData = new HashMap(values.length / 2);
      int i = 0;

      while(i < values.length) {
         this.mapData.put(values[i++], values[i++]);
      }

   }

   public SpreadMap(Map map) {
      this.mapData = map;
   }

   public Object get(Object obj) {
      return this.mapData.get(obj);
   }

   public Object put(Object key, Object value) {
      throw new RuntimeException("SpreadMap: " + this + " is an immutable map, and so (" + key + ": " + value + ") cannot be added.");
   }

   public Object remove(Object key) {
      throw new RuntimeException("SpreadMap: " + this + " is an immutable map, and so the key (" + key + ") cannot be deleteded.");
   }

   public void putAll(Map t) {
      throw new RuntimeException("SpreadMap: " + this + " is an immutable map, and so the map (" + t + ") cannot be put in this spreadMap.");
   }

   public int size() {
      return this.mapData.keySet().size();
   }

   public boolean equals(Object that) {
      return that instanceof SpreadMap ? this.equals((SpreadMap)that) : false;
   }

   public boolean equals(SpreadMap that) {
      if (that == null) {
         return false;
      } else if (this.size() == that.size()) {
         SpreadMap other = that;
         Iterator iter = this.mapData.keySet().iterator();

         Object key;
         do {
            if (!iter.hasNext()) {
               return true;
            }

            key = iter.next();
         } while(DefaultTypeTransformation.compareEqual(this.get(key), other.get(key)));

         return false;
      } else {
         return false;
      }
   }

   public int hashCode() {
      int hash;
      if (this.hashCode == 0) {
         for(Iterator iter = this.mapData.keySet().iterator(); iter.hasNext(); this.hashCode ^= hash) {
            Object key = iter.next();
            hash = key != null ? key.hashCode() : '몾';
         }
      }

      return this.hashCode;
   }

   public String toString() {
      if (this.mapData.isEmpty()) {
         return "*:[:]";
      } else {
         StringBuffer buff = new StringBuffer("*:[");
         Iterator iter = this.mapData.keySet().iterator();

         while(iter.hasNext()) {
            Object key = iter.next();
            buff.append(key + ":" + this.mapData.get(key));
            if (iter.hasNext()) {
               buff.append(", ");
            }
         }

         buff.append("]");
         return buff.toString();
      }
   }
}
