package soot.javaToJimple;

import java.util.HashMap;

public class BiMap {
   HashMap<Object, Object> keyVal;
   HashMap<Object, Object> valKey;

   public void put(Object key, Object val) {
      if (this.keyVal == null) {
         this.keyVal = new HashMap();
      }

      if (this.valKey == null) {
         this.valKey = new HashMap();
      }

      this.keyVal.put(key, val);
      this.valKey.put(val, key);
   }

   public Object getKey(Object val) {
      return this.valKey == null ? null : this.valKey.get(val);
   }

   public Object getVal(Object key) {
      return this.keyVal == null ? null : this.keyVal.get(key);
   }

   public boolean containsKey(Object key) {
      return this.keyVal == null ? false : this.keyVal.containsKey(key);
   }

   public boolean containsVal(Object val) {
      return this.valKey == null ? false : this.valKey.containsKey(val);
   }
}
