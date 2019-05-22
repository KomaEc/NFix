package org.jboss.logging;

import java.util.Map;

public interface MDCProvider {
   void put(String var1, Object var2);

   Object get(String var1);

   void remove(String var1);

   Map<String, Object> getMap();
}
