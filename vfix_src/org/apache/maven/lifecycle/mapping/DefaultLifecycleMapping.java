package org.apache.maven.lifecycle.mapping;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class DefaultLifecycleMapping implements LifecycleMapping {
   private List lifecycles;
   private Map lifecycleMap;
   /** @deprecated */
   private Map phases;

   public List getOptionalMojos(String lifecycle) {
      if (this.lifecycleMap == null) {
         this.lifecycleMap = new HashMap();
         if (this.lifecycles != null) {
            Iterator i = this.lifecycles.iterator();

            while(i.hasNext()) {
               Lifecycle l = (Lifecycle)i.next();
               this.lifecycleMap.put(l.getId(), l);
            }
         }
      }

      Lifecycle l = (Lifecycle)this.lifecycleMap.get(lifecycle);
      return l != null ? l.getOptionalMojos() : null;
   }

   public Map getPhases(String lifecycle) {
      if (this.lifecycleMap == null) {
         this.lifecycleMap = new HashMap();
         if (this.lifecycles != null) {
            Iterator i = this.lifecycles.iterator();

            while(i.hasNext()) {
               Lifecycle l = (Lifecycle)i.next();
               this.lifecycleMap.put(l.getId(), l);
            }
         }
      }

      Lifecycle l = (Lifecycle)this.lifecycleMap.get(lifecycle);
      Map mappings = null;
      if (l == null) {
         if ("default".equals(lifecycle)) {
            mappings = this.phases;
         }
      } else {
         mappings = l.getPhases();
      }

      return mappings;
   }
}
