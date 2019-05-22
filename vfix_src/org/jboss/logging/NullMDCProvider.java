package org.jboss.logging;

import java.util.Map;

public class NullMDCProvider implements MDCProvider {
   public Object get(String key) {
      return null;
   }

   public Map<String, Object> getMap() {
      return null;
   }

   public void put(String key, Object val) {
   }

   public void remove(String key) {
   }
}
