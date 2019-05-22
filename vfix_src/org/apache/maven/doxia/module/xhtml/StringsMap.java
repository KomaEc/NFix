package org.apache.maven.doxia.module.xhtml;

import java.util.HashMap;
import java.util.Map;

public class StringsMap extends HashMap {
   private Map map;

   public StringsMap(Map map) {
      this.map = map;
   }

   public String get(String key) {
      return (String)this.map.get(key);
   }
}
