package soot.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MapNumberer<T> implements Numberer<T> {
   Map<T, Integer> map = new HashMap();
   ArrayList<T> al = new ArrayList();
   int nextIndex = 1;

   public void add(T o) {
      if (!this.map.containsKey(o)) {
         this.map.put(o, new Integer(this.nextIndex));
         this.al.add(o);
         ++this.nextIndex;
      }

   }

   public T get(long number) {
      return this.al.get((int)number);
   }

   public long get(Object o) {
      if (o == null) {
         return 0L;
      } else {
         Integer i = (Integer)this.map.get(o);
         if (i == null) {
            throw new RuntimeException("couldn't find " + o);
         } else {
            return (long)i;
         }
      }
   }

   public int size() {
      return this.nextIndex - 1;
   }

   public MapNumberer() {
      this.al.add((Object)null);
   }

   public boolean contains(Object o) {
      return this.map.containsKey(o);
   }
}
