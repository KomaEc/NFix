package org.codehaus.groovy.tools.gse;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

public class StringSetMap extends LinkedHashMap<String, Set<String>> {
   public StringSetMap() {
   }

   public StringSetMap(StringSetMap other) {
      Iterator i$ = other.keySet().iterator();

      while(i$.hasNext()) {
         String key = (String)i$.next();
         this.get(key).addAll(other.get(key));
      }

   }

   public Set<String> get(Object o) {
      String name = (String)o;
      Set<String> set = (Set)super.get(name);
      if (set == null) {
         set = new LinkedHashSet();
         this.put(name, set);
      }

      return (Set)set;
   }

   public void makeTransitiveHull() {
      Set<String> nameSet = new TreeSet(this.keySet());
      new StringSetMap(this);
      Iterator i$ = nameSet.iterator();

      while(i$.hasNext()) {
         String k = (String)i$.next();
         StringSetMap delta = new StringSetMap();
         Iterator i$ = nameSet.iterator();

         String i;
         while(i$.hasNext()) {
            i = (String)i$.next();
            Iterator i$ = nameSet.iterator();

            while(i$.hasNext()) {
               String j = (String)i$.next();
               Set<String> iSet = this.get(i);
               if (iSet.contains(k) && this.get(k).contains(j)) {
                  delta.get(i).add(j);
               }
            }
         }

         i$ = nameSet.iterator();

         while(i$.hasNext()) {
            i = (String)i$.next();
            this.get(i).addAll(delta.get(i));
         }
      }

   }
}
