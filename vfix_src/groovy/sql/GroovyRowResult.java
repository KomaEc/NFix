package groovy.sql;

import groovy.lang.GroovyObjectSupport;
import groovy.lang.MissingPropertyException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class GroovyRowResult extends GroovyObjectSupport implements Map {
   private final Map result;

   public GroovyRowResult(Map result) {
      this.result = result;
   }

   public Object getProperty(String property) {
      try {
         Object value = this.result.get(property);
         if (value != null) {
            return value;
         } else if (this.result.containsKey(property)) {
            return null;
         } else {
            String propertyUpper = property.toUpperCase();
            value = this.result.get(propertyUpper);
            if (value != null) {
               return value;
            } else if (this.result.containsKey(propertyUpper)) {
               return null;
            } else {
               throw new MissingPropertyException(property, GroovyRowResult.class);
            }
         }
      } catch (Exception var4) {
         throw new MissingPropertyException(property, GroovyRowResult.class, var4);
      }
   }

   public Object getAt(int index) {
      try {
         if (index < 0) {
            index += this.result.size();
         }

         Iterator it = this.result.values().iterator();
         int i = 0;

         Object obj;
         for(obj = null; obj == null && it.hasNext(); ++i) {
            if (i == index) {
               obj = it.next();
            } else {
               it.next();
            }
         }

         return obj;
      } catch (Exception var5) {
         throw new MissingPropertyException(Integer.toString(index), GroovyRowResult.class, var5);
      }
   }

   public String toString() {
      return this.result.toString();
   }

   public void clear() {
      this.result.clear();
   }

   public boolean containsKey(Object key) {
      return this.result.containsKey(key);
   }

   public boolean containsValue(Object value) {
      return this.result.containsValue(value);
   }

   public Set entrySet() {
      return this.result.entrySet();
   }

   public boolean equals(Object o) {
      return this.result.equals(o);
   }

   public Object get(Object property) {
      return property instanceof String ? this.getProperty((String)property) : null;
   }

   public int hashCode() {
      return this.result.hashCode();
   }

   public boolean isEmpty() {
      return this.result.isEmpty();
   }

   public Set keySet() {
      return this.result.keySet();
   }

   public Object put(Object key, Object value) {
      return this.result.put(key, value);
   }

   public void putAll(Map t) {
      this.result.putAll(t);
   }

   public Object remove(Object key) {
      return this.result.remove(key);
   }

   public int size() {
      return this.result.size();
   }

   public Collection values() {
      return this.result.values();
   }
}
