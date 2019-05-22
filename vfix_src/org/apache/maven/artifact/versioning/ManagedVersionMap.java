package org.apache.maven.artifact.versioning;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ManagedVersionMap extends HashMap {
   public ManagedVersionMap(Map map) {
      if (map != null) {
         this.putAll(map);
      }

   }

   public String toString() {
      StringBuffer buffer = new StringBuffer("ManagedVersionMap\n");
      Iterator iter = this.keySet().iterator();

      while(iter.hasNext()) {
         String key = (String)iter.next();
         buffer.append(key).append("=").append(this.get(key));
         if (iter.hasNext()) {
            buffer.append("\n");
         }
      }

      return buffer.toString();
   }
}
